use "core:io.ec"
use "core:net.ec"
use "core:time.ec"
use "core:string.ec"
use "core:array.ec"

fn main(): Int throws Error do
    val server = try net.listen(0, "127.0.0.1")
    val port = server.address.port
    io.log(port > 0)
    try net.listen(port, "127.0.0.1") catch taken do
        io.log(taken.message.contains("in use"))
    val client = try net.connect("127.0.0.1", port, time.seconds(2))
    val peer = try net.accept(server, time.seconds(2))
    io.log(client.remote.port == port)
    io.log(peer.remote.port == client.local.port)
    try net.send(client, "one\r\ntwo\nthree")
    io.log(try net.readLine(peer) ?? "?")
    io.log(try net.readLine(peer) ?? "?")
    io.log(try net.readExactly(peer, 2))
    io.log(try net.waitReadable(peer, time.millis(1)))
    try net.finishSending(client)
    io.log(try net.readLine(peer) ?? "?")
    io.log((try net.readLine(peer)) == null)
    try net.send(peer, "reply")
    net.close(peer)
    io.log(try net.readAll(client))
    // Writing to a peer that has gone is an error, and doesn't end the program with SIGPIPE.
    var failed = false
    for i in 0..<5 do
        time.sleep(time.millis(5))
        try net.send(client, "x") catch gone do
            failed = true
    io.log(failed)
    net.close(client)

    // A limit on a line, which doesn't count its line break.
    val writer = try net.connect("127.0.0.1", port, time.seconds(2))
    val limited = try net.accept(server, time.seconds(2))
    try net.send(writer, "12345\r\n123456\n1234567\n")
    io.log(try net.readLine(limited, 5) ?? "?")
    io.log(try net.readLine(limited, 6) ?? "?")
    try net.readLine(limited, 6) catch long do
        io.log(long.code == ERR_RANGE)
    net.close(writer)
    net.close(limited)

    net.setTimeout(try net.connect("127.0.0.1", port), time.millis(1))
    val quiet = try net.accept(server)
    net.setTimeout(quiet, time.millis(20))
    try net.read(quiet) catch problem do
        io.log(problem.code == ERR_TIMEOUT)
    try net.accept(server, time.millis(20)) catch problem2 do
        io.log(problem2.code == ERR_TIMEOUT)
    net.close(server)
    try net.connect("127.0.0.1", port, time.seconds(1)) catch problem3 do
        io.log(problem3.message.contains("refused"))
    try net.connect("127.0.0.1", 70000) catch problem4 do
        io.log(problem4.message)

    val a = try net.udp(0, "127.0.0.1")
    val b = try net.udp(0, "127.0.0.1")
    try net.sendTo(a, "127.0.0.1", b.address.port, "ping")
    io.log(try net.waitReadable(b, time.seconds(1)))
    val d = try net.receiveFrom(b)
    io.log(d.data)
    io.log(d.source.port == a.address.port)
    io.log(d.source.host)
    net.setTimeout(b, time.millis(20))
    try net.receiveFrom(b) catch problem5 do
        io.log(problem5.code == ERR_TIMEOUT)

    io.log((try net.lookup("localhost")).contains("127.0.0.1"))
    io.log(net.text(net.Address { "::1", 80 }))
    io.log((try net.lookup("127.0.0.1"))[0])
    ret 0
