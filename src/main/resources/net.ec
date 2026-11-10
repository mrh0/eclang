/**
 * Networking: TCP clients and servers, UDP, DNS lookups and timeouts. Built on APR (apr_network_io.h, apr_poll.h).
 *
 *     use "core:net.ec"
 *     val c = try net.connect("example.com", 80, time.seconds(5))    // gives up after 5 seconds
 *     defer net.close(c)
 *     net.setTimeout(c, time.seconds(10))                            // for each read and write from now on
 *     try net.send(c, "GET / HTTP/1.0\r\nHost: example.com\r\n\r\n")
 *     io.log(try net.readLine(c) ?? "no answer")
 *
 *     val server = try net.listen(8080)                              // on all addresses; 0 lets the system choose
 *     while true do
 *         pool do                                                    // what one connection uses is freed after it
 *             val client = try net.accept(server)
 *             try net.send(client, "hello ${client.remote.host}\n")
 *             net.close(client)
 *
 *     val u = try net.udp()
 *     try net.sendTo(u, "127.0.0.1", 9000, "ping")
 *     val d = try net.receiveFrom(u)                                 // d.data, d.source
 *
 *     for ip in try net.lookup("example.com") do
 *         io.log(ip)
 *
 *     val p = try net.poller()                                       // waits for many sockets at once
 *     try p.add(server, 0)                                           // a token of your own for each socket
 *     for e in try p.wait(time.seconds(1)) do
 *         io.log("${e.token} is ready")                              // accept, or read what has arrived
 *
 *     use "core:tls.ec"                                              // TLS through OpenSSL: links -lssl -lcrypto
 *     val s = try net.connect("example.com", 443, time.seconds(5))
 *     try net.secure(s, "example.com")                               // checks the certificate; then read and send as usual
 *
 * A socket belongs to the pool that was current when it was made: it is closed when the pool is freed, if `close`
 * didn't close it before. What is read is allocated in the current pool. Failures are OsErrors with the reason; a
 * timeout has the code ERR_TIMEOUT. To serve many clients at once, give each its own thread (core:thread.ec), or wait
 * for all of them with a {@link Poller} and read what has arrived with {@link readMore}, which never waits.
 *
 * Opening a connection makes the program ignore SIGPIPE, for good: writing to a peer that has gone is then an error,
 * and doesn't end the program. TLS needs OpenSSL, so it is in core:tls.ec, which a program that wants it uses: then
 * {@link secure} works, and so do `https:` URLs in core:http.ec. Without it, `secure` fails with an error that says so.
 */
module "net"
use "core:errors.ec"
use "core:string.ec"
use "core:time.ec"
use "core:c/signal.ec"
use "core:c/stdio.ec"
use "core:apr/apr.ec"
use "core:apr/errno.ec"
use "core:apr/network_io.ec"
use "core:apr/poll.ec"
use "core:apr/signal.ec"

// ---------------------------------------------------------------------------------------------------
// Addresses

/** Where a socket is: an IP address and a port. */
rec Address as
    /** The IP address as text: "127.0.0.1", "::1". */
    host: String
    /** The port, from 0 to 65535. */
    port: Int

/** `a` the way it is written in a URL: "127.0.0.1:80", "[::1]:80". */
fn text(a: Address): String do
    ret "[${a.host}]:${a.port}" if a.host.contains(':')
    ret "${a.host}:${a.port}"

/** The {@link Address} of APR's `sa`. */
priv fn addressOf(sa: @apr_sockaddr_t): Address = addressOf(sa, pool)

/** The {@link Address} of APR's `sa`, in the pool `mem`. */
priv fn addressOf(sa: @apr_sockaddr_t, mem: Pool): Address do
    val buffer = new Char[64]
    apr_sockaddr_ip_getbuf(buffer.data !as CString, buffer.len, sa)
    ret Address { clone(fromC(buffer.data !as CString), mem), sa.port as Int }

/**
 * Checks a port number.
 * @throws when it isn't one
 */
priv fn checkPort(port: Int) throws OsError do
    throw OsError { ERR_ARGUMENT, "${port} is not a port: a port is a number from 0 to 65535", APR_EINVAL } if port < 0 or port > 65535

/**
 * The addresses of `host`, linked by their `next`.
 * @throws when it has none
 */
priv fn resolve(host: String, port: Int): @apr_sockaddr_t throws OsError do
    try checkPort(port)
    var first: @apr_sockaddr_t? = null
    val status = apr_sockaddr_info_get(mut first, cstr(host), APR_UNSPEC, port as apr_port_t, 0, pool)
    throw osError(status, "look up '${host}'") if status != APR_SUCCESS
    ret first!

/**
 * The IP addresses of a name, as DNS and the system's hosts file give them: `net.lookup("localhost")`.
 * @return the addresses, in the order to try them; at least one
 * @throws when the name has none
 */
fn lookup(host: String): String[] throws OsError do
    val first = try resolve(host, 0)
    var count: Size = 0
    var at: @apr_sockaddr_t? = first
    while at != null do
        count += 1
        at = at.next
    val all = new String[count]
    var i: Size = 0
    at = first
    while at != null do
        all[i] = addressOf(at).host
        i += 1
        at = at.next
    ret all

/**
 * The name of an IP address, as reverse DNS gives it: `net.reverseLookup("127.0.0.1")`.
 * @return the name, or null when the address has none
 * @throws when `ip` isn't an IP address
 */
fn reverseLookup(ip: String): String? throws OsError do
    val sa = try resolve(ip, 0)
    var name: CString? = null
    ret null if apr_getnameinfo(mut name, sa, 0) != APR_SUCCESS
    if val found = name do
        ret fromC(found)
    ret null

// ---------------------------------------------------------------------------------------------------
// Timeouts and waiting

/** APR's timeout for `d`: microseconds, and -1 for none, which waits as long as it takes. */
priv fn microsOf(d: time.Duration?): apr_interval_time_t do
    if val length = d do
        ret if length.micros < 0 do 0 else length.micros
    ret -1

/**
 * Waits until `socket` has something to read, or the peer closed it.
 * @return false when `timeout` passed first
 * @throws when waiting fails
 */
priv fn waitFor(socket: @apr_socket_t, timeout: time.Duration?): Bool throws OsError do
    val waiting = new apr_pollfd_t
    waiting.p = pool
    waiting.desc_type = APR_POLL_SOCKET
    waiting.reqevents = APR_POLLIN as Short
    waiting.rtnevents = 0
    waiting.desc.s = socket
    waiting.client_data = null
    var ready = 0
    while true do
        val status = apr_poll(waiting, 1, mut ready, microsOf(timeout))
        ret ready > 0 if status == APR_SUCCESS
        ret false if APR_STATUS_IS_TIMEUP(status) != 0
        throw osError(status, "wait for a socket") if APR_STATUS_IS_EINTR(status) == 0

// ---------------------------------------------------------------------------------------------------
// TCP connections

/** How many bytes a {@link Connection} reads ahead, for {@link readLine}. */
priv const READ_AHEAD: Size = 8192

/** An open TCP connection, from {@link connect} or {@link accept}. Close it with {@link close}. */
mut ref rec Connection as
    /** The APR socket, for calling apr_socket_* functions directly. */
    socket: @apr_socket_t
    /** The other end. */
    remote: Address
    /** This end. */
    local: Address
    /** What was read ahead: the bytes from {@code start} to {@code end} are still to be read. */
    ahead: Char[]
    /** Where in {@code ahead} the next byte is. */
    start: Size
    /** Where in {@code ahead} the bytes end. */
    end: Size
    /** The pool it belongs to, which closes it when it is freed. */
    mem: Pool
    /** What reads and writes go through when it isn't plain TCP: TLS, from {@link secure}. */
    transport: Transport? = null

/** The {@link Address} of one end of `socket`. */
priv fn endOf(socket: @apr_socket_t, which: apr_interface_e): Address throws OsError = try endOf(socket, which, pool)

/** The {@link Address} of one end of `socket`, in the pool `mem`. */
priv fn endOf(socket: @apr_socket_t, which: apr_interface_e, mem: Pool): Address throws OsError do
    var sa: @apr_sockaddr_t? = null
    val status = apr_socket_addr_get(mut sa, which, socket)
    throw osError(status, "find a socket's address") if status != APR_SUCCESS
    ret addressOf(sa!, mem)

/** A {@link Connection} on `socket`, which is connected. */
priv fn connectionOn(socket: @apr_socket_t): Connection throws OsError = try connectionOn(socket, pool)

/** A {@link Connection} on `socket`, which is connected, in the pool `mem`. */
priv fn connectionOn(socket: @apr_socket_t, mem: Pool): Connection throws OsError do
    apr_signal(SIGPIPE, SIG_IGN)
    apr_socket_opt_set(socket, APR_TCP_NODELAY, 1)
    ret new Connection { socket, try endOf(socket, :remote, mem), try endOf(socket, :local, mem), new Char[READ_AHEAD] in mem, 0, 0, mem } in mem

/**
 * Opens a TCP connection. When `host` has several addresses, they are tried in turn.
 * @param host a name or an IP address
 * @param port the port
 * @param timeout how long to wait for each address; null for as long as the system does. Reads and writes aren't
 *     limited by it: see {@link setTimeout}
 * @throws when the name has no address, or none of them answers
 */
fn connect(host: String, port: Int, timeout: time.Duration? = null): Connection throws OsError do
    var at: @apr_sockaddr_t? = try resolve(host, port)
    var status = APR_SUCCESS
    while at != null do
        val sa = at
        var socket: @apr_socket_t? = null
        val made = apr_socket_create(mut socket, sa.family, SOCK_STREAM, APR_PROTO_TCP, pool)
        throw osError(made, "make a socket") if made != APR_SUCCESS
        val s = socket!
        apr_socket_timeout_set(s, microsOf(timeout))
        status = apr_socket_connect(s, sa)
        if status == APR_SUCCESS do
            apr_socket_timeout_set(s, -1)
            ret try connectionOn(s)
        apr_socket_close(s)
        at = sa.next
    throw osError(status, "connect to ${host}:${port}")

/**
 * How long each read and write of `c` may wait from now on; after that it fails with the code ERR_TIMEOUT. The
 * connection can still be used.
 * @param timeout the time, or null to wait as long as it takes, as a new connection does
 */
fn setTimeout(c: Connection, timeout: time.Duration?) do
    apr_socket_timeout_set(c.socket, microsOf(timeout))

/** Reads up to `length` bytes into `buffer`, through the transport when there is one, and sets `length` to how many. */
priv fn receive(c: Connection, buffer: CString, mut length: Size): apr_status_t do
    if val t = c.transport do
        var n = length
        val status = t.recv(t.state, buffer, addrof n)
        length = n
        ret status
    ret apr_socket_recv(c.socket, buffer, mut length)

/** Sends up to `length` bytes of `buffer`, through the transport when there is one, and sets `length` to how many. */
priv fn sendSome(c: Connection, buffer: CString, mut length: Size): apr_status_t do
    if val t = c.transport do
        var n = length
        val status = t.send(t.state, buffer, addrof n)
        length = n
        ret status
    ret apr_socket_send(c.socket, buffer, mut length)

/** The error for `status` from doing `what` on `c`, with the transport's own words when it has some. */
priv fn failure(c: Connection, status: apr_status_t, what: String): OsError do
    if val t = c.transport do
        val problem = t.problem(t.state)
        ret OsError { statusKind(status), "${what}: ${problem}", status } if problem.len > 0
    ret osError(status, what)

/** How many bytes the transport holds that the socket's readiness doesn't show (decrypted TLS records). */
priv fn held(c: Connection): Size do
    if val t = c.transport do
        ret t.pending(t.state)
    ret 0

/**
 * Sends all of `data`.
 * @throws when the connection fails or a timeout passes; part of `data` may have been sent by then
 */
fn send(c: Connection, data: String) throws OsError do
    var done: Size = 0
    while done < data.len do
        val part = data[done..<data.len]
        var length = part.len
        val status = sendSome(c, part.data !as CString, mut length)
        done += length
        throw failure(c, status, "send to ${text(c.remote)}") if status != APR_SUCCESS

/** Takes up to `n` bytes of what was read ahead, as a new String in the current pool. */
priv fn takeAhead(c: Connection, n: Size): String do
    val count = if n < c.end - c.start do n else c.end - c.start
    val out = clone(c.ahead[c.start..<c.start + count])
    c.start += count
    ret out !as String

/**
 * Reads ahead, when nothing is left of that.
 * @return false at the end of the connection
 * @throws when reading fails or a timeout passes
 */
priv fn fill(c: Connection): Bool throws OsError do
    ret true if c.start < c.end
    var length = c.ahead.len
    val status = receive(c, c.ahead.data !as CString, mut length)
    c.start = 0
    c.end = length
    throw failure(c, status, "read from ${text(c.remote)}") if status != APR_SUCCESS and APR_STATUS_IS_EOF(status) == 0
    ret length > 0

/**
 * Reads what has arrived, as soon as there is something: up to `n` bytes, and often fewer. TCP keeps the order of the
 * bytes, but not where one `send` ended: see {@link readExactly} and {@link readLine}.
 * @param c the connection
 * @param n the most to read
 * @return the bytes; "" when the other end has closed the connection and nothing is left
 * @throws when reading fails or a timeout passes
 */
fn read(c: Connection, n: Size = 65536): String throws OsError do
    ret takeAhead(c, n) if c.start < c.end
    val buffer = new Char[n]
    var length = n
    val status = receive(c, buffer.data !as CString, mut length)
    throw failure(c, status, "read from ${text(c.remote)}") if status != APR_SUCCESS and APR_STATUS_IS_EOF(status) == 0
    ret buffer[0..<length] !as String

/**
 * Reads exactly `n` bytes, for a message whose length is known.
 * @throws when the connection ends before that, with the code ERR_IO; or reading fails, or a timeout passes
 */
fn readExactly(c: Connection, n: Size): String throws OsError do
    val out = new Char[n]
    var got: Size = 0
    while got < n do
        val part = try read(c, n - got)
        throw OsError { ERR_IO, "the connection to ${text(c.remote)} ended after ${got} of ${n} bytes", APR_EOF } if part.len == 0
        for i in 0..<part.len do
            out[got + i] = part[i]
        got += part.len
    ret out !as String

/**
 * Reads the next line.
 * @param c the connection
 * @param limit the most bytes a line may have, for what comes from someone who can't be trusted; null for no limit
 * @return the line, without its line break ("\n" or "\r\n"); null when the connection has ended. A last line without a
 *     line break is given too
 * @throws when reading fails or a timeout passes, or a line is longer than `limit`, with the code ERR_RANGE
 */
fn readLine(c: Connection, limit: Size? = null): String? throws OsError do
    val line = stringBuilder(128)
    var got = false
    while true do
        val more = try fill(c)
        break if not more
        got = true
        var i = c.start
        while i < c.end and c.ahead[i] != '\n' do
            i += 1
        val found = i < c.end
        line.append(c.ahead[c.start..<i] !as String)
        c.start = if found do i + 1 else i
        if val most = limit do
            // A "\r" at the end may be the start of the line break, which isn't part of the line.
            val length = if line.len > 0 and line.buffer[line.len - 1] == '\r' do line.len - 1 else line.len
            throw OsError { ERR_RANGE, "a line from ${text(c.remote)} is longer than ${most} bytes", APR_EINVAL } if length > most
        ret removeSuffix(line.build(), "\r") if found
    ret null if not got
    ret removeSuffix(line.build(), "\r")

/**
 * Reads until the other end closes the connection.
 * @throws when reading fails or a timeout passes
 */
fn readAll(c: Connection): String throws OsError do
    val all = stringBuilder(4096)
    while true do
        val part = try read(c)
        break if part.len == 0
        all.append(part)
    ret all.build()

/**
 * Waits until {@link read} has something to give, or the other end has closed the connection.
 * @param c the connection
 * @param timeout how long to wait; null for as long as it takes
 * @return false when the time passed first
 * @throws when waiting fails
 */
fn waitReadable(c: Connection, timeout: time.Duration? = null): Bool throws OsError do
    ret true if c.start < c.end or held(c) > 0
    ret try waitFor(c.socket, timeout)

/**
 * Reads what has already arrived into the connection's buffer, without waiting: for a loop that serves many
 * connections with a {@link Poller}. What it read is what {@link read} and {@link readLine} give next, and
 * {@link buffered} shows it. The buffer holds 8 KB.
 * @return how many bytes it added: 0 when nothing had arrived, or the buffer is full; null when the other end has closed
 *     the connection
 * @throws when reading fails
 */
fn readMore(c: Connection): Size? throws OsError do
    if c.start > 0 do
        val left = c.end - c.start
        for i in 0..<left do
            c.ahead[i] = c.ahead[c.start + i]
        c.start = 0
        c.end = left
    var saved: apr_interval_time_t = -1
    apr_socket_timeout_get(c.socket, mut saved)
    apr_socket_timeout_set(c.socket, 0)
    defer apr_socket_timeout_set(c.socket, saved)
    var added: Size = 0
    while c.end < c.ahead.len do
        var length = c.ahead.len - c.end
        val status = receive(c, c.ahead[c.end..<c.ahead.len].data !as CString, mut length)
        c.end += length
        added += length
        if status != APR_SUCCESS do
            break if APR_STATUS_IS_EAGAIN(status) != 0
            if APR_STATUS_IS_EOF(status) != 0 do
                ret if added > 0 do added else null
            throw failure(c, status, "read from ${text(c.remote)}")
        ret null if length == 0 and added == 0
        break if length == 0
    ret added

/** Whether the connection's buffer is full, so that {@link readMore} can't add to it before something is taken. */
fn isBufferFull(c: Connection): Bool = c.end - c.start == c.ahead.len

/**
 * What has been read ahead and not taken yet: what {@link read} gives next. It is a view of the connection's buffer, which
 * the next read changes: clone it to keep it.
 */
fn buffered(c: Connection): String = c.ahead[c.start..<c.end] !as String

/**
 * Tells the other end that nothing more will be sent, which ends its reads, and goes on reading its answer.
 * @throws when the connection has already failed
 */
fn finishSending(c: Connection) throws OsError do
    val status = apr_socket_shutdown(c.socket, :write)
    throw osError(status, "finish sending to ${text(c.remote)}") if status != APR_SUCCESS

/** Closes the connection, ending its TLS session first if it has one. Don't use `c` after this. */
fn close(c: Connection) do
    if val t = c.transport do
        t.close(t.state)
    apr_socket_close(c.socket)

// ---------------------------------------------------------------------------------------------------
// TLS (needs core:tls.ec)

/**
 * How a {@link Connection} reads and writes when it isn't plain TCP: a TLS session, which core:tls.ec makes. Each
 * function is given {@code state}, and returns an APR status as apr_socket_recv and apr_socket_send do.
 */
rec Transport as
    /** The layer's own state (an OpenSSL SSL). */
    state: @Any
    /** Reads up to `@length` bytes into the buffer and sets `@length` to how many; APR_EOF at the end. */
    recv: (@Any, CString, @Size) => apr_status_t
    /** Sends up to `@length` bytes of the buffer and sets `@length` to how many. */
    send: (@Any, CString, @Size) => apr_status_t
    /** How many bytes it holds ready to read, which the socket's readiness doesn't show. */
    pending: (@Any) => Size
    /** Takes the handshake a step further: APR_SUCCESS when it is done, an EAGAIN status while it waits for the peer. */
    handshake: (@Any) => apr_status_t
    /** Ends the session, telling the peer when that needn't wait. */
    close: (@Any) => None
    /** What went wrong last, in words; "" when nothing did. */
    problem: (@Any) => String

/** How {@link secure} checks the server it connects to. */
rec TlsOptions as
    /** A file of PEM certificates to trust instead of the system's: for a server with a certificate of your own. */
    caFile: String? = null
    /**
     * Whether to check the server's certificate and that it is for the host. Leave it on: without it, anyone between
     * the two can read and change everything. Turn it off only to test against a server of your own.
     */
    verify: Bool = true

/** What makes a client connection secure: {@code net.tlsClient}. */
type TlsClient = (Connection, String, TlsOptions) => None throws OsError

/** Set by core:tls.ec when a program uses it: gives a client connection its {@link Transport}. */
var tlsClient: TlsClient? = null

/** What makes accepted connections secure, from `tls.server` in core:tls.ec. */
rec TlsServer as
    /** The OpenSSL context, with the certificate and key. */
    context: @Any
    /** Gives an accepted connection its {@link Transport}, before the handshake. */
    begin: (@Any, Connection) => None throws OsError

/**
 * Checks that the program can make TLS connections: that it uses core:tls.ec.
 * @throws when it doesn't, with the code ERR_UNSUPPORTED and what to do
 */
fn requireTls() throws OsError do
    throw OsError { ERR_UNSUPPORTED, "TLS needs OpenSSL: add `use \"core:tls.ec\"` to the program (it links -lssl and -lcrypto, from a package such as libssl-dev)", APR_ENOTIMPL } if tlsClient == null

/**
 * Finishes the handshake of `c`, which has a transport, waiting as long as the connection's timeout allows.
 * @throws when it fails: a certificate that isn't valid, a peer that doesn't speak TLS, or a timeout
 */
priv fn finishHandshake(c: Connection, what: String) throws OsError do
    val t = c.transport!
    while true do
        val status = t.handshake(t.state)
        ret if status == APR_SUCCESS
        if APR_STATUS_IS_EAGAIN(status) == 0 do
            val error = failure(c, status, what)
            c.transport = null
            throw error
        // The socket doesn't wait (a timeout of 0): wait here.
        val ready = try waitFor(c.socket, null)
        throw OsError { ERR_TIMEOUT, "${what}: no answer", APR_TIMEUP } if not ready

/**
 * Makes `c` a TLS connection, as a client of `host`: the handshake, and a check that the server's certificate is
 * valid and for `host` (unless `options` say otherwise). Then reads and writes of `c` are encrypted. It waits as long
 * as the connection's timeout ({@link setTimeout}) allows. Needs `use "core:tls.ec"`.
 * @param c a connection that has sent and read nothing yet
 * @param host the name the server's certificate must be for, also sent to the server (SNI)
 * @param options which certificates to trust
 * @throws when the program doesn't use core:tls.ec, or the handshake or the check fails
 */
fn secure(c: Connection, host: String, options: TlsOptions = TlsOptions {}) throws OsError do
    try requireTls()
    val client = tlsClient!
    throw OsError { ERR_STATE, "the connection to ${text(c.remote)} is already secure", APR_EINVAL } if c.transport != null
    try client(c, host, options)
    try finishHandshake(c, "TLS handshake with ${host}")

/**
 * Makes an accepted connection a TLS connection, as the server: the handshake, waiting as long as the connection's
 * timeout allows.
 * @param c a connection from {@link accept} that has sent and read nothing yet
 * @param server the certificate and key, from `tls.server`
 * @throws when the handshake fails
 */
fn secure(c: Connection, server: TlsServer) throws OsError do
    try beginSecure(c, server)
    try finishHandshake(c, "TLS handshake with ${text(c.remote)}")

/**
 * Starts making an accepted connection secure, for a loop with a {@link Poller}: {@link handshake} goes on with it as the
 * client's messages arrive.
 * @throws when TLS can't be set up for it
 */
fn beginSecure(c: Connection, server: TlsServer) throws OsError do
    throw OsError { ERR_STATE, "the connection to ${text(c.remote)} is already secure", APR_EINVAL } if c.transport != null
    try server.begin(server.context, c)

/**
 * Takes the handshake of `c` as far as what has arrived allows, without waiting: for a loop with a {@link Poller}.
 * @return true when it is done (also for a connection without TLS), false when it waits for the peer: wait until `c`
 *     is readable and call it again
 * @throws when it fails
 */
fn handshake(c: Connection): Bool throws OsError do
    if val t = c.transport do
        var saved: apr_interval_time_t = -1
        apr_socket_timeout_get(c.socket, mut saved)
        apr_socket_timeout_set(c.socket, 0)
        defer apr_socket_timeout_set(c.socket, saved)
        val status = t.handshake(t.state)
        ret true if status == APR_SUCCESS
        ret false if APR_STATUS_IS_EAGAIN(status) != 0
        val error = failure(c, status, "TLS handshake with ${text(c.remote)}")
        c.transport = null
        throw error
    ret true

/** Whether reads and writes of `c` go through TLS. */
fn isSecure(c: Connection): Bool = c.transport != null

// ---------------------------------------------------------------------------------------------------
// TCP servers

/** A socket that waits for connections, from {@link listen}. Close it with {@link close}. */
rec Listener as
    /** The APR socket, for calling apr_socket_* functions directly. */
    socket: @apr_socket_t
    /** Where it listens. With port 0 this tells the port the system chose. */
    address: Address

/**
 * Starts listening for TCP connections.
 * @param port the port; 0 lets the system choose a free one, which `address.port` tells
 * @param host the address to listen on: "127.0.0.1" for this computer only, "::" for IPv6; all IPv4 addresses by default
 * @param backlog how many connections may wait for {@link accept}
 * @throws when the port is in use, or needs privileges this program doesn't have
 */
fn listen(port: Int, host: String = "0.0.0.0", backlog: Int = 128): Listener throws OsError do
    val sa = try resolve(host, port)
    var socket: @apr_socket_t? = null
    val made = apr_socket_create(mut socket, sa.family, SOCK_STREAM, APR_PROTO_TCP, pool)
    throw osError(made, "make a socket") if made != APR_SUCCESS
    val s = socket!
    apr_socket_opt_set(s, APR_SO_REUSEADDR, 1)
    val bound = apr_socket_bind(s, sa)
    if bound != APR_SUCCESS do
        apr_socket_close(s)
        throw osError(bound, "listen on port ${port} of ${host}")
    val listening = apr_socket_listen(s, backlog)
    if listening != APR_SUCCESS do
        apr_socket_close(s)
        throw osError(listening, "listen on port ${port} of ${host}")
    ret Listener { s, try endOf(s, :local) }

/**
 * Waits for the next connection. It belongs to the current pool: with a `pool do` block around each connection, a
 * server can run forever.
 * @param l the listener
 * @param timeout how long to wait; null for as long as it takes
 * @throws when accepting fails, or the time passes, with the code ERR_TIMEOUT
 */
fn accept(l: Listener, timeout: time.Duration? = null): Connection throws OsError = try accept(l, timeout, pool)

/**
 * Waits for the next connection, which belongs to the pool `mem`: a pool of its own (`new Pool`) lets a loop that
 * serves many connections free each when it ends.
 * @param l the listener
 * @param timeout how long to wait; null for as long as it takes, `time.micros(0)` not at all
 * @param mem the pool of the connection
 * @throws when accepting fails, or the time passes, with the code ERR_TIMEOUT
 */
fn accept(l: Listener, timeout: time.Duration?, mem: Pool): Connection throws OsError do
    // apr_socket_accept doesn't wait by itself when a socket has a timeout, so that isn't used here.
    val ready = try waitFor(l.socket, timeout)
    throw OsError { ERR_TIMEOUT, "no connection came to ${text(l.address)} in time", APR_TIMEUP } if not ready
    var socket: @apr_socket_t? = null
    val status = apr_socket_accept(mut socket, l.socket, mem)
    throw osError(status, "accept a connection on ${text(l.address)}") if status != APR_SUCCESS
    ret try connectionOn(socket!, mem)

/**
 * Waits until {@link accept} has a connection to give.
 * @param l the listener
 * @param timeout how long to wait; null for as long as it takes
 * @return false when the time passed first
 * @throws when waiting fails
 */
fn waitReadable(l: Listener, timeout: time.Duration? = null): Bool throws OsError = try waitFor(l.socket, timeout)

/** Stops listening. Connections it accepted stay open. Don't use `l` after this. */
fn close(l: Listener) do
    apr_socket_close(l.socket)

// ---------------------------------------------------------------------------------------------------
// UDP

/** A UDP socket, from {@link udp}. Close it with {@link close}. */
rec UdpSocket as
    /** The APR socket, for calling apr_socket_* functions directly. */
    socket: @apr_socket_t
    /** Where it receives. With port 0 this tells the port the system chose. */
    address: Address

/** A message from {@link receiveFrom}. */
rec Datagram as
    /** What it holds. */
    data: String
    /** Who sent it, for an answer. */
    source: Address

/**
 * Makes a UDP socket. UDP sends single messages: they can get lost, doubled or out of order, and nothing tells.
 * @param port the port to receive on; 0 lets the system choose a free one, which `address.port` tells
 * @param host the address to receive on: "::" for IPv6; all IPv4 addresses by default. It can only send to addresses of
 *     the same kind
 * @throws when the port is in use
 */
fn udp(port: Int = 0, host: String = "0.0.0.0"): UdpSocket throws OsError do
    val sa = try resolve(host, port)
    var socket: @apr_socket_t? = null
    val made = apr_socket_create(mut socket, sa.family, SOCK_DGRAM, APR_PROTO_UDP, pool)
    throw osError(made, "make a socket") if made != APR_SUCCESS
    val s = socket!
    val bound = apr_socket_bind(s, sa)
    if bound != APR_SUCCESS do
        apr_socket_close(s)
        throw osError(bound, "bind port ${port} of ${host}")
    ret UdpSocket { s, try endOf(s, :local) }

/**
 * Sends one message.
 * @param u the socket
 * @param host where to: a name or an IP address
 * @param port its port
 * @param data the message; up to about 65000 bytes, and less than 1400 to be safe
 * @throws when the name has no address, or the message can't be sent. That it arrives isn't known
 */
fn sendTo(u: UdpSocket, host: String, port: Int, data: String) throws OsError do
    val sa = try resolve(host, port)
    var length = data.len
    val status = apr_socket_sendto(u.socket, sa, 0, data.data !as CString, mut length)
    throw osError(status, "send to ${host}:${port}") if status != APR_SUCCESS

/**
 * Waits for one message.
 * @param u the socket
 * @param n the most to read: the rest of a longer message is lost
 * @throws when receiving fails, or a timeout from {@link setTimeout} passes, with the code ERR_TIMEOUT
 */
fn receiveFrom(u: UdpSocket, n: Size = 65536): Datagram throws OsError do
    val buffer = new Char[n]
    var length = n
    val source = new apr_sockaddr_t
    val status = apr_socket_recvfrom(source, u.socket, 0, buffer.data !as CString, mut length)
    throw osError(status, "receive on ${text(u.address)}") if status != APR_SUCCESS
    ret Datagram { buffer[0..<length] !as String, addressOf(source) }

/**
 * How long {@link receiveFrom} may wait from now on.
 * @param u the socket
 * @param timeout the time, or null to wait as long as it takes
 */
fn setTimeout(u: UdpSocket, timeout: time.Duration?) do
    apr_socket_timeout_set(u.socket, microsOf(timeout))

/**
 * Waits until {@link receiveFrom} has a message to give.
 * @param u the socket
 * @param timeout how long to wait; null for as long as it takes
 * @return false when the time passed first
 * @throws when waiting fails
 */
fn waitReadable(u: UdpSocket, timeout: time.Duration? = null): Bool throws OsError = try waitFor(u.socket, timeout)

/** Closes the socket. Don't use `u` after this. */
fn close(u: UdpSocket) do
    apr_socket_close(u.socket)


// ---------------------------------------------------------------------------------------------------
// Waiting for many sockets

/** A socket that a {@link Poller} found ready. */
rec Event as
    /** The token it was added with. */
    token: Long
    /** Whether it has something to read (a connection to accept, for a listener), or the other end closed it. */
    readable: Bool
    /** Whether it can be written to without waiting. */
    writable: Bool
    /** Whether the other end hung up, or the socket failed: reading tells which. */
    closed: Bool

/**
 * Waits for many sockets at once (epoll on Linux): for a server with more clients than it has threads. Each socket is
 * added with a token, a number of your own (an index, a key of a Map) that {@link wait} gives back for it.
 *
 *     val p = try net.poller()
 *     try p.add(listener, 0)
 *     while true do
 *         for e in try p.wait() do
 *             if e.token == 0 do
 *                 val c = try net.accept(listener, time.micros(0), new Pool)
 *                 try p.add(c, nextToken)                  // and keep c under nextToken
 *             else do
 *                 ...                                      // net.readMore of the connection, never waiting
 *
 * A connection's {@link buffered} bytes don't make it ready: read them before waiting again. Remove a socket before
 * closing it. {@link wake} makes a wait in another thread return; on Linux, adding and removing can be done from other
 * threads too. A Poller belongs to the current pool, and is destroyed with it.
 */
mut ref rec Poller as
    /** The APR pollset, for calling apr_pollset_* functions directly. */
    set: @apr_pollset_t
    /** The most sockets it can hold. */
    capacity: Size
    /** How many sockets it holds. */
    count: Size

/**
 * A new Poller, in the current pool.
 * @param capacity the most sockets it can hold
 * @throws when the system can't make one
 */
fn poller(capacity: Size = 1024): Poller throws OsError do
    var set: @apr_pollset_t? = null
    var status = apr_pollset_create(mut set, capacity as apr_uint32_t, pool, (APR_POLLSET_THREADSAFE | APR_POLLSET_WAKEABLE) as apr_uint32_t)
    if status != APR_SUCCESS do
        // Not every method can be used from several threads.
        status = apr_pollset_create(mut set, capacity as apr_uint32_t, pool, APR_POLLSET_WAKEABLE as apr_uint32_t)
    throw osError(status, "make a poller") if status != APR_SUCCESS
    ret new Poller { set!, capacity, 0 }

/** What APR needs to know of `socket`. */
priv fn descriptor(socket: @apr_socket_t, token: Long, readable: Bool, writable: Bool): apr_pollfd_t do
    var d: apr_pollfd_t
    d.p = pool
    d.desc_type = APR_POLL_SOCKET
    d.reqevents = ((if readable do APR_POLLIN else 0) | (if writable do APR_POLLOUT else 0)) as Short
    d.rtnevents = 0
    d.desc.s = socket
    d.client_data = token as unsafe @Any
    ret d

/** Adds `socket` to `p`. */
priv fn watch(p: Poller, socket: @apr_socket_t, token: Long, readable: Bool, writable: Bool) throws OsError do
    throw OsError { ERR_RANGE, "this poller is full: it holds at most ${p.capacity} sockets", APR_ENOMEM } if p.count >= p.capacity
    var d = descriptor(socket, token, readable, writable)
    val status = apr_pollset_add(p.set, addrof d)
    throw osError(status, "add a socket to a poller") if status != APR_SUCCESS
    p.count += 1

/** Removes `socket` from `p`; nothing happens when it isn't in it. */
priv fn unwatch(p: Poller, socket: @apr_socket_t) do
    var d = descriptor(socket, 0, true, false)
    if apr_pollset_remove(p.set, addrof d) == APR_SUCCESS do
        p.count -= 1

/**
 * Watches a connection.
 * @param p the poller
 * @param c the connection
 * @param token what {@link wait} gives back for it
 * @param readable whether to wait for something to read
 * @param writable whether to wait for room to write, for a program that writes a little at a time
 * @throws when the poller is full, or `c` is in it already
 */
fn add(p: Poller, c: Connection, token: Long, readable: Bool = true, writable: Bool = false) throws OsError = try watch(p, c.socket, token, readable, writable)

/**
 * Watches a listener: it is readable when there is a connection to {@link accept}.
 * @throws when the poller is full, or `l` is in it already
 */
fn add(p: Poller, l: Listener, token: Long) throws OsError = try watch(p, l.socket, token, true, false)

/**
 * Watches a UDP socket: it is readable when there is a message to {@link receiveFrom}.
 * @throws when the poller is full, or `u` is in it already
 */
fn add(p: Poller, u: UdpSocket, token: Long) throws OsError = try watch(p, u.socket, token, true, false)

/** Stops watching a connection. Nothing happens when it isn't watched. */
fn remove(p: Poller, c: Connection) do
    unwatch(p, c.socket)

/** Stops watching a listener. Nothing happens when it isn't watched. */
fn remove(p: Poller, l: Listener) do
    unwatch(p, l.socket)

/** Stops watching a UDP socket. Nothing happens when it isn't watched. */
fn remove(p: Poller, u: UdpSocket) do
    unwatch(p, u.socket)

/**
 * Waits until some of the sockets are ready, the time passes, or {@link wake} is called.
 * @param p the poller
 * @param timeout how long to wait; null for as long as it takes
 * @return the sockets that are ready, in the current pool; none when the time passed or `wake` was called
 * @throws when waiting fails
 */
fn wait(p: Poller, timeout: time.Duration? = null): Event[] throws OsError do
    var count: apr_int32_t = 0
    var found: @apr_pollfd_t? = null
    val status = apr_pollset_poll(p.set, microsOf(timeout), mut count, mut found)
    if status != APR_SUCCESS do
        ret new Event[0] if APR_STATUS_IS_TIMEUP(status) != 0 or APR_STATUS_IS_EINTR(status) != 0
        throw osError(status, "wait for sockets")
    val events = new Event[count as Size]
    if val first = found do
        for i in 0..<(count as Size) do
            val d = unsafe first[i]
            val got = d.rtnevents as Int
            events[i] = Event {
                token = d.client_data !as Long,
                readable = got & (APR_POLLIN | APR_POLLHUP | APR_POLLERR) != 0,
                writable = got & APR_POLLOUT != 0,
                closed = got & (APR_POLLHUP | APR_POLLERR) != 0
            }
    ret events

/** Makes a {@link wait} of `p` return now, from another thread. */
fn wake(p: Poller) do
    apr_pollset_wakeup(p.set)

/** Destroys the poller; the sockets in it stay open. Don't use `p` after this. */
fn close(p: Poller) do
    apr_pollset_destroy(p.set)
