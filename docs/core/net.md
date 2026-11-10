# Module `net`

Networking: TCP clients and servers, UDP, DNS lookups and timeouts. Built on APR (apr_network_io.h, apr_poll.h).

    use "core:net.ec"
    val c = try net.connect("example.com", 80, time.seconds(5))    // gives up after 5 seconds
    defer net.close(c)
    net.setTimeout(c, time.seconds(10))                            // for each read and write from now on
    try net.send(c, "GET / HTTP/1.0\r\nHost: example.com\r\n\r\n")
    io.log(try net.readLine(c) ?? "no answer")

    val server = try net.listen(8080)                              // on all addresses; 0 lets the system choose
    while true do
        pool do                                                    // what one connection uses is freed after it
            val client = try net.accept(server)
            try net.send(client, "hello ${client.remote.host}\n")
            net.close(client)

    val u = try net.udp()
    try net.sendTo(u, "127.0.0.1", 9000, "ping")
    val d = try net.receiveFrom(u)                                 // d.data, d.source

    for ip in try net.lookup("example.com") do
        io.log(ip)

    val p = try net.poller()                                       // waits for many sockets at once
    try p.add(server, 0)                                           // a token of your own for each socket
    for e in try p.wait(time.seconds(1)) do
        io.log("${e.token} is ready")                              // accept, or read what has arrived

    use "core:tls.ec"                                              // TLS through OpenSSL: links -lssl -lcrypto
    val s = try net.connect("example.com", 443, time.seconds(5))
    try net.secure(s, "example.com")                               // checks the certificate; then read and send as usual

A socket belongs to the pool that was current when it was made: it is closed when the pool is freed, if `close`
didn't close it before. What is read is allocated in the current pool. Failures are OsErrors with the reason; a
timeout has the code ERR_TIMEOUT. To serve many clients at once, give each its own thread (core:thread.ec), or wait
for all of them with a [`Poller`](#net.Poller) and read what has arrived with [`readMore`](#net.readMore), which never waits.

Opening a connection makes the program ignore SIGPIPE, for good: writing to a peer that has gone is then an error,
and doesn't end the program. TLS needs OpenSSL, so it is in core:tls.ec, which a program that wants it uses: then
[`secure`](#net.secure) works, and so do `https:` URLs in core:http.ec. Without it, `secure` fails with an error that says so.

## Types

### <a id="net.Address"></a>`Address`

```ec
rec Address
```

Where a socket is: an IP address and a port.

**Fields**

- <a id="net.Address.host"></a>`host: String` — The IP address as text: "127.0.0.1", "::1".
- <a id="net.Address.port"></a>`port: Int` — The port, from 0 to 65535.

### <a id="net.Connection"></a>`Connection`

```ec
mut ref rec Connection
```

An open TCP connection, from [`connect`](#net.connect) or [`accept`](#net.accept). Close it with [`close`](#net.close).

**Fields**

- <a id="net.Connection.socket"></a>`socket: @apr_socket_t` — The APR socket, for calling apr_socket_* functions directly.
- <a id="net.Connection.remote"></a>`remote: Address` — The other end.
- <a id="net.Connection.local"></a>`local: Address` — This end.
- <a id="net.Connection.ahead"></a>`ahead: Char[]` — What was read ahead: the bytes from `start` to `end` are still to be read.
- <a id="net.Connection.start"></a>`start: Size` — Where in `ahead` the next byte is.
- <a id="net.Connection.end"></a>`end: Size` — Where in `ahead` the bytes end.
- <a id="net.Connection.mem"></a>`mem: Pool` — The pool it belongs to, which closes it when it is freed.
- <a id="net.Connection.transport"></a>`transport: Transport? = null` — What reads and writes go through when it isn't plain TCP: TLS, from [`secure`](#net.secure).

### <a id="net.Transport"></a>`Transport`

```ec
rec Transport
```

How a [`Connection`](#net.Connection) reads and writes when it isn't plain TCP: a TLS session, which core:tls.ec makes. Each
function is given `state`, and returns an APR status as apr_socket_recv and apr_socket_send do.

**Fields**

- <a id="net.Transport.state"></a>`state: @Any` — The layer's own state (an OpenSSL SSL).
- <a id="net.Transport.recv"></a>`recv: (@Any, CString, @Size) => apr_status_t` — Reads up to `@length` bytes into the buffer and sets `@length` to how many; APR_EOF at the end.
- <a id="net.Transport.send"></a>`send: (@Any, CString, @Size) => apr_status_t` — Sends up to `@length` bytes of the buffer and sets `@length` to how many.
- <a id="net.Transport.pending"></a>`pending: (@Any) => Size` — How many bytes it holds ready to read, which the socket's readiness doesn't show.
- <a id="net.Transport.handshake"></a>`handshake: (@Any) => apr_status_t` — Takes the handshake a step further: APR_SUCCESS when it is done, an EAGAIN status while it waits for the peer.
- <a id="net.Transport.close"></a>`close: (@Any) => None` — Ends the session, telling the peer when that needn't wait.
- <a id="net.Transport.problem"></a>`problem: (@Any) => String` — What went wrong last, in words; "" when nothing did.

### <a id="net.TlsOptions"></a>`TlsOptions`

```ec
rec TlsOptions
```

How [`secure`](#net.secure) checks the server it connects to.

**Fields**

- <a id="net.TlsOptions.caFile"></a>`caFile: String? = null` — A file of PEM certificates to trust instead of the system's: for a server with a certificate of your own.
- <a id="net.TlsOptions.verify"></a>`verify: Bool = true` — Whether to check the server's certificate and that it is for the host. Leave it on: without it, anyone between
  the two can read and change everything. Turn it off only to test against a server of your own.

### <a id="net.TlsClient"></a>`TlsClient`

```ec
type TlsClient = (Connection, String, TlsOptions) => None throws OsError
```

What makes a client connection secure: `net.tlsClient`.

### <a id="net.TlsServer"></a>`TlsServer`

```ec
rec TlsServer
```

What makes accepted connections secure, from `tls.server` in core:tls.ec.

**Fields**

- <a id="net.TlsServer.context"></a>`context: @Any` — The OpenSSL context, with the certificate and key.
- <a id="net.TlsServer.begin"></a>`begin: (@Any, Connection) => None throws OsError` — Gives an accepted connection its [`Transport`](#net.Transport), before the handshake.

### <a id="net.Listener"></a>`Listener`

```ec
rec Listener
```

A socket that waits for connections, from [`listen`](#net.listen). Close it with [`close`](#net.close).

**Fields**

- <a id="net.Listener.socket"></a>`socket: @apr_socket_t` — The APR socket, for calling apr_socket_* functions directly.
- <a id="net.Listener.address"></a>`address: Address` — Where it listens. With port 0 this tells the port the system chose.

### <a id="net.UdpSocket"></a>`UdpSocket`

```ec
rec UdpSocket
```

A UDP socket, from [`udp`](#net.udp). Close it with [`close`](#net.close).

**Fields**

- <a id="net.UdpSocket.socket"></a>`socket: @apr_socket_t` — The APR socket, for calling apr_socket_* functions directly.
- <a id="net.UdpSocket.address"></a>`address: Address` — Where it receives. With port 0 this tells the port the system chose.

### <a id="net.Datagram"></a>`Datagram`

```ec
rec Datagram
```

A message from [`receiveFrom`](#net.receiveFrom).

**Fields**

- <a id="net.Datagram.data"></a>`data: String` — What it holds.
- <a id="net.Datagram.source"></a>`source: Address` — Who sent it, for an answer.

### <a id="net.Event"></a>`Event`

```ec
rec Event
```

A socket that a [`Poller`](#net.Poller) found ready.

**Fields**

- <a id="net.Event.token"></a>`token: Long` — The token it was added with.
- <a id="net.Event.readable"></a>`readable: Bool` — Whether it has something to read (a connection to accept, for a listener), or the other end closed it.
- <a id="net.Event.writable"></a>`writable: Bool` — Whether it can be written to without waiting.
- <a id="net.Event.closed"></a>`closed: Bool` — Whether the other end hung up, or the socket failed: reading tells which.

### <a id="net.Poller"></a>`Poller`

```ec
mut ref rec Poller
```

Waits for many sockets at once (epoll on Linux): for a server with more clients than it has threads. Each socket is
added with a token, a number of your own (an index, a key of a Map) that [`wait`](#net.wait) gives back for it.

    val p = try net.poller()
    try p.add(listener, 0)
    while true do
        for e in try p.wait() do
            if e.token == 0 do
                val c = try net.accept(listener, time.micros(0), new Pool)
                try p.add(c, nextToken)                  // and keep c under nextToken
            else do
                ...                                      // net.readMore of the connection, never waiting

A connection's [`buffered`](#net.buffered) bytes don't make it ready: read them before waiting again. Remove a socket before
closing it. [`wake`](#net.wake) makes a wait in another thread return; on Linux, adding and removing can be done from other
threads too. A Poller belongs to the current pool, and is destroyed with it.

**Fields**

- <a id="net.Poller.set"></a>`set: @apr_pollset_t` — The APR pollset, for calling apr_pollset_* functions directly.
- <a id="net.Poller.capacity"></a>`capacity: Size` — The most sockets it can hold.
- <a id="net.Poller.count"></a>`count: Size` — How many sockets it holds.

## Values

### <a id="net.tlsClient"></a>`tlsClient`

```ec
var tlsClient: TlsClient? = null
```

Set by core:tls.ec when a program uses it: gives a client connection its [`Transport`](#net.Transport).

## Functions

### <a id="net.text"></a>`text`

```ec
fn text(a: Address): String
```

`a` the way it is written in a URL: "127.0.0.1:80", "[::1]:80".

### <a id="net.lookup"></a>`lookup`

```ec
fn lookup(host: String): String[] throws OsError
```

The IP addresses of a name, as DNS and the system's hosts file give them: `net.lookup("localhost")`.

**Returns** the addresses, in the order to try them; at least one

**Throws**

- `OsError` — when the name has none

### <a id="net.reverseLookup"></a>`reverseLookup`

```ec
fn reverseLookup(ip: String): String? throws OsError
```

The name of an IP address, as reverse DNS gives it: `net.reverseLookup("127.0.0.1")`.

**Returns** the name, or null when the address has none

**Throws**

- `OsError` — when `ip` isn't an IP address

### <a id="net.connect"></a>`connect`

```ec
fn connect(host: String, port: Int, timeout: time.Duration? = null): Connection throws OsError
```

Opens a TCP connection. When `host` has several addresses, they are tried in turn.

**Parameters**

- `host` — a name or an IP address
- `port` — the port
- `timeout` — how long to wait for each address; null for as long as the system does. Reads and writes aren't
  limited by it: see [`setTimeout`](#net.setTimeout)

**Throws**

- `OsError` — when the name has no address, or none of them answers

### <a id="net.setTimeout"></a>`setTimeout`

```ec
fn setTimeout(c: Connection, timeout: time.Duration?)
```

How long each read and write of `c` may wait from now on; after that it fails with the code ERR_TIMEOUT. The
connection can still be used.

**Parameters**

- `timeout` — the time, or null to wait as long as it takes, as a new connection does

### <a id="net.send"></a>`send`

```ec
fn send(c: Connection, data: String) throws OsError
```

Sends all of `data`.

**Throws**

- `OsError` — when the connection fails or a timeout passes; part of `data` may have been sent by then

### <a id="net.read"></a>`read`

```ec
fn read(c: Connection, n: Size = 65536): String throws OsError
```

Reads what has arrived, as soon as there is something: up to `n` bytes, and often fewer. TCP keeps the order of the
bytes, but not where one `send` ended: see [`readExactly`](#net.readExactly) and [`readLine`](#net.readLine).

**Parameters**

- `c` — the connection
- `n` — the most to read

**Returns** the bytes; "" when the other end has closed the connection and nothing is left

**Throws**

- `OsError` — when reading fails or a timeout passes

### <a id="net.readExactly"></a>`readExactly`

```ec
fn readExactly(c: Connection, n: Size): String throws OsError
```

Reads exactly `n` bytes, for a message whose length is known.

**Throws**

- `OsError` — when the connection ends before that, with the code ERR_IO; or reading fails, or a timeout passes

### <a id="net.readLine"></a>`readLine`

```ec
fn readLine(c: Connection, limit: Size? = null): String? throws OsError
```

Reads the next line.

**Parameters**

- `c` — the connection
- `limit` — the most bytes a line may have, for what comes from someone who can't be trusted; null for no limit

**Returns** the line, without its line break ("\n" or "\r\n"); null when the connection has ended. A last line without a
line break is given too

**Throws**

- `OsError` — when reading fails or a timeout passes, or a line is longer than `limit`, with the code ERR_RANGE

### <a id="net.readAll"></a>`readAll`

```ec
fn readAll(c: Connection): String throws OsError
```

Reads until the other end closes the connection.

**Throws**

- `OsError` — when reading fails or a timeout passes

### <a id="net.waitReadable"></a>`waitReadable`

```ec
fn waitReadable(c: Connection, timeout: time.Duration? = null): Bool throws OsError
```

Waits until [`read`](#net.read) has something to give, or the other end has closed the connection.

**Parameters**

- `c` — the connection
- `timeout` — how long to wait; null for as long as it takes

**Returns** false when the time passed first

**Throws**

- `OsError` — when waiting fails

### <a id="net.readMore"></a>`readMore`

```ec
fn readMore(c: Connection): Size? throws OsError
```

Reads what has already arrived into the connection's buffer, without waiting: for a loop that serves many
connections with a [`Poller`](#net.Poller). What it read is what [`read`](#net.read) and [`readLine`](#net.readLine) give next, and
[`buffered`](#net.buffered) shows it. The buffer holds 8 KB.

**Returns** how many bytes it added: 0 when nothing had arrived, or the buffer is full; null when the other end has closed
the connection

**Throws**

- `OsError` — when reading fails

### <a id="net.isBufferFull"></a>`isBufferFull`

```ec
fn isBufferFull(c: Connection): Bool
```

Whether the connection's buffer is full, so that [`readMore`](#net.readMore) can't add to it before something is taken.

### <a id="net.buffered"></a>`buffered`

```ec
fn buffered(c: Connection): String
```

What has been read ahead and not taken yet: what [`read`](#net.read) gives next. It is a view of the connection's buffer, which
the next read changes: clone it to keep it.

### <a id="net.finishSending"></a>`finishSending`

```ec
fn finishSending(c: Connection) throws OsError
```

Tells the other end that nothing more will be sent, which ends its reads, and goes on reading its answer.

**Throws**

- `OsError` — when the connection has already failed

### <a id="net.close"></a>`close`

```ec
fn close(c: Connection)
```

Closes the connection, ending its TLS session first if it has one. Don't use `c` after this.

### <a id="net.requireTls"></a>`requireTls`

```ec
fn requireTls() throws OsError
```

Checks that the program can make TLS connections: that it uses core:tls.ec.

**Throws**

- `OsError` — when it doesn't, with the code ERR_UNSUPPORTED and what to do

### <a id="net.secure"></a>`secure`

```ec
fn secure(c: Connection, host: String, options: TlsOptions = TlsOptions {}) throws OsError
```

Makes `c` a TLS connection, as a client of `host`: the handshake, and a check that the server's certificate is
valid and for `host` (unless `options` say otherwise). Then reads and writes of `c` are encrypted. It waits as long
as the connection's timeout ([`setTimeout`](#net.setTimeout)) allows. Needs `use "core:tls.ec"`.

**Parameters**

- `c` — a connection that has sent and read nothing yet
- `host` — the name the server's certificate must be for, also sent to the server (SNI)
- `options` — which certificates to trust

**Throws**

- `OsError` — when the program doesn't use core:tls.ec, or the handshake or the check fails

### `secure`

```ec
fn secure(c: Connection, server: TlsServer) throws OsError
```

Makes an accepted connection a TLS connection, as the server: the handshake, waiting as long as the connection's
timeout allows.

**Parameters**

- `c` — a connection from [`accept`](#net.accept) that has sent and read nothing yet
- `server` — the certificate and key, from `tls.server`

**Throws**

- `OsError` — when the handshake fails

### <a id="net.beginSecure"></a>`beginSecure`

```ec
fn beginSecure(c: Connection, server: TlsServer) throws OsError
```

Starts making an accepted connection secure, for a loop with a [`Poller`](#net.Poller): [`handshake`](#net.handshake) goes on with it as the
client's messages arrive.

**Throws**

- `OsError` — when TLS can't be set up for it

### <a id="net.handshake"></a>`handshake`

```ec
fn handshake(c: Connection): Bool throws OsError
```

Takes the handshake of `c` as far as what has arrived allows, without waiting: for a loop with a [`Poller`](#net.Poller).

**Returns** true when it is done (also for a connection without TLS), false when it waits for the peer: wait until `c`
is readable and call it again

**Throws**

- `OsError` — when it fails

### <a id="net.isSecure"></a>`isSecure`

```ec
fn isSecure(c: Connection): Bool
```

Whether reads and writes of `c` go through TLS.

### <a id="net.listen"></a>`listen`

```ec
fn listen(port: Int, host: String = "0.0.0.0", backlog: Int = 128): Listener throws OsError
```

Starts listening for TCP connections.

**Parameters**

- `port` — the port; 0 lets the system choose a free one, which `address.port` tells
- `host` — the address to listen on: "127.0.0.1" for this computer only, "::" for IPv6; all IPv4 addresses by default
- `backlog` — how many connections may wait for [`accept`](#net.accept)

**Throws**

- `OsError` — when the port is in use, or needs privileges this program doesn't have

### <a id="net.accept"></a>`accept`

```ec
fn accept(l: Listener, timeout: time.Duration? = null): Connection throws OsError
```

Waits for the next connection. It belongs to the current pool: with a `pool do` block around each connection, a
server can run forever.

**Parameters**

- `l` — the listener
- `timeout` — how long to wait; null for as long as it takes

**Throws**

- `OsError` — when accepting fails, or the time passes, with the code ERR_TIMEOUT

### `accept`

```ec
fn accept(l: Listener, timeout: time.Duration?, mem: Pool): Connection throws OsError
```

Waits for the next connection, which belongs to the pool `mem`: a pool of its own (`new Pool`) lets a loop that
serves many connections free each when it ends.

**Parameters**

- `l` — the listener
- `timeout` — how long to wait; null for as long as it takes, `time.micros(0)` not at all
- `mem` — the pool of the connection

**Throws**

- `OsError` — when accepting fails, or the time passes, with the code ERR_TIMEOUT

### `waitReadable`

```ec
fn waitReadable(l: Listener, timeout: time.Duration? = null): Bool throws OsError
```

Waits until [`accept`](#net.accept) has a connection to give.

**Parameters**

- `l` — the listener
- `timeout` — how long to wait; null for as long as it takes

**Returns** false when the time passed first

**Throws**

- `OsError` — when waiting fails

### `close`

```ec
fn close(l: Listener)
```

Stops listening. Connections it accepted stay open. Don't use `l` after this.

### <a id="net.udp"></a>`udp`

```ec
fn udp(port: Int = 0, host: String = "0.0.0.0"): UdpSocket throws OsError
```

Makes a UDP socket. UDP sends single messages: they can get lost, doubled or out of order, and nothing tells.

**Parameters**

- `port` — the port to receive on; 0 lets the system choose a free one, which `address.port` tells
- `host` — the address to receive on: "::" for IPv6; all IPv4 addresses by default. It can only send to addresses of
  the same kind

**Throws**

- `OsError` — when the port is in use

### <a id="net.sendTo"></a>`sendTo`

```ec
fn sendTo(u: UdpSocket, host: String, port: Int, data: String) throws OsError
```

Sends one message.

**Parameters**

- `u` — the socket
- `host` — where to: a name or an IP address
- `port` — its port
- `data` — the message; up to about 65000 bytes, and less than 1400 to be safe

**Throws**

- `OsError` — when the name has no address, or the message can't be sent. That it arrives isn't known

### <a id="net.receiveFrom"></a>`receiveFrom`

```ec
fn receiveFrom(u: UdpSocket, n: Size = 65536): Datagram throws OsError
```

Waits for one message.

**Parameters**

- `u` — the socket
- `n` — the most to read: the rest of a longer message is lost

**Throws**

- `OsError` — when receiving fails, or a timeout from [`setTimeout`](#net.setTimeout) passes, with the code ERR_TIMEOUT

### `setTimeout`

```ec
fn setTimeout(u: UdpSocket, timeout: time.Duration?)
```

How long [`receiveFrom`](#net.receiveFrom) may wait from now on.

**Parameters**

- `u` — the socket
- `timeout` — the time, or null to wait as long as it takes

### `waitReadable`

```ec
fn waitReadable(u: UdpSocket, timeout: time.Duration? = null): Bool throws OsError
```

Waits until [`receiveFrom`](#net.receiveFrom) has a message to give.

**Parameters**

- `u` — the socket
- `timeout` — how long to wait; null for as long as it takes

**Returns** false when the time passed first

**Throws**

- `OsError` — when waiting fails

### `close`

```ec
fn close(u: UdpSocket)
```

Closes the socket. Don't use `u` after this.

### <a id="net.poller"></a>`poller`

```ec
fn poller(capacity: Size = 1024): Poller throws OsError
```

A new Poller, in the current pool.

**Parameters**

- `capacity` — the most sockets it can hold

**Throws**

- `OsError` — when the system can't make one

### <a id="net.add"></a>`add`

```ec
fn add(p: Poller, c: Connection, token: Long, readable: Bool = true, writable: Bool = false) throws OsError
```

Watches a connection.

**Parameters**

- `p` — the poller
- `c` — the connection
- `token` — what [`wait`](#net.wait) gives back for it
- `readable` — whether to wait for something to read
- `writable` — whether to wait for room to write, for a program that writes a little at a time

**Throws**

- `OsError` — when the poller is full, or `c` is in it already

### `add`

```ec
fn add(p: Poller, l: Listener, token: Long) throws OsError
```

Watches a listener: it is readable when there is a connection to [`accept`](#net.accept).

**Throws**

- `OsError` — when the poller is full, or `l` is in it already

### `add`

```ec
fn add(p: Poller, u: UdpSocket, token: Long) throws OsError
```

Watches a UDP socket: it is readable when there is a message to [`receiveFrom`](#net.receiveFrom).

**Throws**

- `OsError` — when the poller is full, or `u` is in it already

### <a id="net.remove"></a>`remove`

```ec
fn remove(p: Poller, c: Connection)
```

Stops watching a connection. Nothing happens when it isn't watched.

### `remove`

```ec
fn remove(p: Poller, l: Listener)
```

Stops watching a listener. Nothing happens when it isn't watched.

### `remove`

```ec
fn remove(p: Poller, u: UdpSocket)
```

Stops watching a UDP socket. Nothing happens when it isn't watched.

### <a id="net.wait"></a>`wait`

```ec
fn wait(p: Poller, timeout: time.Duration? = null): Event[] throws OsError
```

Waits until some of the sockets are ready, the time passes, or [`wake`](#net.wake) is called.

**Parameters**

- `p` — the poller
- `timeout` — how long to wait; null for as long as it takes

**Returns** the sockets that are ready, in the current pool; none when the time passed or `wake` was called

**Throws**

- `OsError` — when waiting fails

### <a id="net.wake"></a>`wake`

```ec
fn wake(p: Poller)
```

Makes a [`wait`](#net.wait) of `p` return now, from another thread.

### `close`

```ec
fn close(p: Poller)
```

Destroys the poller; the sockets in it stay open. Don't use `p` after this.
