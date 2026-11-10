use "core:io.ec"
use "core:time.ec"
use "core:net.ec"

fn main(): Int throws Error do
    val server = try net.listen(0, "127.0.0.1")
    val port = server.address.port
    val p = try net.poller(8)
    try p.add(server, 0)
    io.log(p.count)
    io.log((try p.wait(time.millis(10))).len)

    val a = try net.connect("127.0.0.1", port)
    val b = try net.connect("127.0.0.1", port)
    var accepted = 0
    var tokens: Long = 1
    val held = new (net.Connection?)[4]
    while accepted < 2 do
        for e in try p.wait(time.seconds(2)) do
            if e.token == 0 and e.readable do
                val c = try net.accept(server, time.micros(0), new Pool)
                held[tokens as Size] = c
                try p.add(c, tokens)
                tokens += 1
                accepted += 1
    io.log(p.count)

    try net.send(b, "hello from b\nand more")
    var lines = 0
    while lines < 1 do
        for e in try p.wait(time.seconds(2)) do
            io.log("token ${e.token} readable ${e.readable}")
            val c = held[e.token as Size]!
            io.log(try net.readMore(c) ?? 0)
            io.log(net.buffered(c))
            io.log(try net.readLine(c) ?? "none")
            io.log(net.buffered(c))
            lines += 1

    net.close(b)
    var closed = false
    while not closed do
        for e in try p.wait(time.seconds(2)) do
            val c = held[e.token as Size]!
            if try net.readMore(c) == null do
                io.log("token ${e.token} closed")
                p.remove(c)
                net.close(c)
                closed = true
    io.log(p.count)
    io.log(try net.readMore(held[1]!) ?? 99)

    try net.secure(a, "localhost") catch e do
        io.log(e.message)
    net.close(a)
    p.close()
    net.close(server)
    ret 0
