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

A socket belongs to the pool that was current when it was made: it is closed when the pool is freed, if `close`
didn't close it before. What is read is allocated in the current pool. Failures are OsErrors with the reason; a
timeout has the code ERR_TIMEOUT. To serve many clients at once, give each its own thread (core:thread.ec).

Opening a connection makes the program ignore SIGPIPE, for good: writing to a peer that has gone is then an error,
and doesn't end the program. Everything is plain TCP and UDP; there is no TLS.

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
rec Connection
```

An open TCP connection, from [`connect`](#net.connect) or [`accept`](#net.accept). Close it with [`close`](#net.close).

**Fields**

- <a id="net.Connection.socket"></a>`socket: @apr_socket_t` — The APR socket, for calling apr_socket_* functions directly.
- <a id="net.Connection.remote"></a>`remote: Address` — The other end.
- <a id="net.Connection.local"></a>`local: Address` — This end.
- <a id="net.Connection.ahead"></a>`ahead: Char[]` — What was read ahead: the bytes from `start` to `end` are still to be read.
- <a id="net.Connection.start"></a>`start: Size` — Where in `ahead` the next byte is.
- <a id="net.Connection.end"></a>`end: Size` — Where in `ahead` the bytes end.

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
fn connect(host: String, port: Int, timeout: time.Duration? = null): @Connection throws OsError
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
fn setTimeout(c: @Connection, timeout: time.Duration?)
```

How long each read and write of `c` may wait from now on; after that it fails with the code ERR_TIMEOUT. The
connection can still be used.

**Parameters**

- `timeout` — the time, or null to wait as long as it takes, as a new connection does

### <a id="net.send"></a>`send`

```ec
fn send(c: @Connection, data: String) throws OsError
```

Sends all of `data`.

**Throws**

- `OsError` — when the connection fails or a timeout passes; part of `data` may have been sent by then

### <a id="net.read"></a>`read`

```ec
fn read(c: @Connection, n: Size = 65536): String throws OsError
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
fn readExactly(c: @Connection, n: Size): String throws OsError
```

Reads exactly `n` bytes, for a message whose length is known.

**Throws**

- `OsError` — when the connection ends before that, with the code ERR_IO; or reading fails, or a timeout passes

### <a id="net.readLine"></a>`readLine`

```ec
fn readLine(c: @Connection, limit: Size? = null): String? throws OsError
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
fn readAll(c: @Connection): String throws OsError
```

Reads until the other end closes the connection.

**Throws**

- `OsError` — when reading fails or a timeout passes

### <a id="net.waitReadable"></a>`waitReadable`

```ec
fn waitReadable(c: @Connection, timeout: time.Duration? = null): Bool throws OsError
```

Waits until [`read`](#net.read) has something to give, or the other end has closed the connection.

**Parameters**

- `c` — the connection
- `timeout` — how long to wait; null for as long as it takes

**Returns** false when the time passed first

**Throws**

- `OsError` — when waiting fails

### <a id="net.finishSending"></a>`finishSending`

```ec
fn finishSending(c: @Connection) throws OsError
```

Tells the other end that nothing more will be sent, which ends its reads, and goes on reading its answer.

**Throws**

- `OsError` — when the connection has already failed

### <a id="net.close"></a>`close`

```ec
fn close(c: @Connection)
```

Closes the connection. Don't use `c` after this.

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
fn accept(l: Listener, timeout: time.Duration? = null): @Connection throws OsError
```

Waits for the next connection. It belongs to the current pool: with a `pool do` block around each connection, a
server can run forever.

**Parameters**

- `l` — the listener
- `timeout` — how long to wait; null for as long as it takes

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
