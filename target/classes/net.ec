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
 * A socket belongs to the pool that was current when it was made: it is closed when the pool is freed, if `close`
 * didn't close it before. What is read is allocated in the current pool. Failures are OsErrors with the reason; a
 * timeout has the code ERR_TIMEOUT. To serve many clients at once, give each its own thread (core:thread.ec).
 *
 * Opening a connection makes the program ignore SIGPIPE, for good: writing to a peer that has gone is then an error,
 * and doesn't end the program. Everything is plain TCP and UDP; there is no TLS.
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
priv fn addressOf(sa: @apr_sockaddr_t): Address do
    val buffer = new Char[64]
    apr_sockaddr_ip_getbuf(buffer.data !as CString, buffer.len, sa)
    ret Address { fromC(buffer.data !as CString), sa.port as Int }

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
    val status = apr_sockaddr_info_get(addrof(first!), cstr(host), APR_UNSPEC, port as apr_port_t, 0, pool)
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
    ret null if apr_getnameinfo(addrof(name!), sa, 0) != APR_SUCCESS
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
        val status = apr_poll(waiting, 1, addrof(ready), microsOf(timeout))
        ret ready > 0 if status == APR_SUCCESS
        ret false if APR_STATUS_IS_TIMEUP(status) != 0
        throw osError(status, "wait for a socket") if APR_STATUS_IS_EINTR(status) == 0

// ---------------------------------------------------------------------------------------------------
// TCP connections

/** How many bytes a {@link Connection} reads ahead, for {@link readLine}. */
priv const READ_AHEAD: Size = 8192

/** An open TCP connection, from {@link connect} or {@link accept}. Close it with {@link close}. */
rec Connection as
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

/** The {@link Address} of one end of `socket`. */
priv fn endOf(socket: @apr_socket_t, which: apr_interface_e): Address throws OsError do
    var sa: @apr_sockaddr_t? = null
    val status = apr_socket_addr_get(addrof(sa!), which, socket)
    throw osError(status, "find a socket's address") if status != APR_SUCCESS
    ret addressOf(sa!)

/** A {@link Connection} on `socket`, which is connected. */
priv fn connectionOn(socket: @apr_socket_t): @Connection throws OsError do
    apr_signal(SIGPIPE, SIG_IGN)
    apr_socket_opt_set(socket, APR_TCP_NODELAY, 1)
    ret new Connection { socket, try endOf(socket, :remote), try endOf(socket, :local), new Char[READ_AHEAD], 0, 0 }

/**
 * Opens a TCP connection. When `host` has several addresses, they are tried in turn.
 * @param host a name or an IP address
 * @param port the port
 * @param timeout how long to wait for each address; null for as long as the system does. Reads and writes aren't
 *     limited by it: see {@link setTimeout}
 * @throws when the name has no address, or none of them answers
 */
fn connect(host: String, port: Int, timeout: time.Duration? = null): @Connection throws OsError do
    var at: @apr_sockaddr_t? = try resolve(host, port)
    var status = APR_SUCCESS
    while at != null do
        val sa = at
        var socket: @apr_socket_t? = null
        val made = apr_socket_create(addrof(socket!), sa.family, SOCK_STREAM, APR_PROTO_TCP, pool)
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
fn setTimeout(c: @Connection, timeout: time.Duration?) do
    apr_socket_timeout_set(c.socket, microsOf(timeout))

/**
 * Sends all of `data`.
 * @throws when the connection fails or a timeout passes; part of `data` may have been sent by then
 */
fn send(c: @Connection, data: String) throws OsError do
    var done: Size = 0
    while done < data.len do
        val part = data[done..<data.len]
        var length = part.len
        val status = apr_socket_send(c.socket, part.data !as CString, addrof(length))
        done += length
        throw osError(status, "send to ${text(c.remote)}") if status != APR_SUCCESS

/** Takes up to `n` bytes of what was read ahead, as a new String in the current pool. */
priv fn takeAhead(c: @Connection, n: Size): String do
    val count = if n < c.end - c.start do n else c.end - c.start
    val out = clone(c.ahead[c.start..<c.start + count])
    c.start += count
    ret out !as String

/**
 * Reads ahead, when nothing is left of that.
 * @return false at the end of the connection
 * @throws when reading fails or a timeout passes
 */
priv fn fill(c: @Connection): Bool throws OsError do
    ret true if c.start < c.end
    var length = c.ahead.len
    val status = apr_socket_recv(c.socket, c.ahead.data !as CString, addrof(length))
    c.start = 0
    c.end = length
    throw osError(status, "read from ${text(c.remote)}") if status != APR_SUCCESS and APR_STATUS_IS_EOF(status) == 0
    ret length > 0

/**
 * Reads what has arrived, as soon as there is something: up to `n` bytes, and often fewer. TCP keeps the order of the
 * bytes, but not where one `send` ended: see {@link readExactly} and {@link readLine}.
 * @param c the connection
 * @param n the most to read
 * @return the bytes; "" when the other end has closed the connection and nothing is left
 * @throws when reading fails or a timeout passes
 */
fn read(c: @Connection, n: Size = 65536): String throws OsError do
    ret takeAhead(c, n) if c.start < c.end
    val buffer = new Char[n]
    var length = n
    val status = apr_socket_recv(c.socket, buffer.data !as CString, addrof(length))
    throw osError(status, "read from ${text(c.remote)}") if status != APR_SUCCESS and APR_STATUS_IS_EOF(status) == 0
    ret buffer[0..<length] !as String

/**
 * Reads exactly `n` bytes, for a message whose length is known.
 * @throws when the connection ends before that, with the code ERR_IO; or reading fails, or a timeout passes
 */
fn readExactly(c: @Connection, n: Size): String throws OsError do
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
fn readLine(c: @Connection, limit: Size? = null): String? throws OsError do
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
fn readAll(c: @Connection): String throws OsError do
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
fn waitReadable(c: @Connection, timeout: time.Duration? = null): Bool throws OsError do
    ret true if c.start < c.end
    ret try waitFor(c.socket, timeout)

/**
 * Tells the other end that nothing more will be sent, which ends its reads, and goes on reading its answer.
 * @throws when the connection has already failed
 */
fn finishSending(c: @Connection) throws OsError do
    val status = apr_socket_shutdown(c.socket, :write)
    throw osError(status, "finish sending to ${text(c.remote)}") if status != APR_SUCCESS

/** Closes the connection. Don't use `c` after this. */
fn close(c: @Connection) do
    apr_socket_close(c.socket)

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
    val made = apr_socket_create(addrof(socket!), sa.family, SOCK_STREAM, APR_PROTO_TCP, pool)
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
fn accept(l: Listener, timeout: time.Duration? = null): @Connection throws OsError do
    // apr_socket_accept doesn't wait by itself when a socket has a timeout, so that isn't used here.
    val ready = try waitFor(l.socket, timeout)
    throw OsError { ERR_TIMEOUT, "no connection came to ${text(l.address)} in time", APR_TIMEUP } if not ready
    var socket: @apr_socket_t? = null
    val status = apr_socket_accept(addrof(socket!), l.socket, pool)
    throw osError(status, "accept a connection on ${text(l.address)}") if status != APR_SUCCESS
    ret try connectionOn(socket!)

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
    val made = apr_socket_create(addrof(socket!), sa.family, SOCK_DGRAM, APR_PROTO_UDP, pool)
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
    val status = apr_socket_sendto(u.socket, sa, 0, data.data !as CString, addrof(length))
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
    val status = apr_socket_recvfrom(source, u.socket, 0, buffer.data !as CString, addrof(length))
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
