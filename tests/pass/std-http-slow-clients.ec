use "core:io.ec"
use "core:string.ec"
use "core:time.ec"
use "core:net.ec"
use "core:http.ec"

fn hello(req: http.Request): http.Response = http.text(200, "hello ${req.path}\n")

// Reads one response: its status line and its body.
fn response(c: net.Connection): String throws Error do
    val status = try net.readLine(c) ?? ret "closed"
    var length: Size = 0
    while true do
        val line = try net.readLine(c) ?? ret "closed"
        break if line.len == 0
        if line.startsWith("Content-Length: ") do
            length = (try parseInt(line.drop(16))) as Size
    ret "${status} ${(try net.readExactly(c, length)).trim()}"

fn main(): Int throws Error do
    // One worker: before, an idle or slow connection held it, and every other client waited.
    val options = http.ServerOptions { workers = 1, timeout = time.millis(600), idleTimeout = time.millis(400) }
    val server = try http.server(0, "127.0.0.1", options)
    val port = server.listener.address.port
    val root = "http://127.0.0.1:${port}"
    try http.start(server, fn hello)

    val idle = try net.connect("127.0.0.1", port)
    val slow = try net.connect("127.0.0.1", port)
    try net.send(slow, "GET /slow HTTP/1.1\r\nHost: x\r\n")
    val keptOpen = try net.connect("127.0.0.1", port)
    try net.send(keptOpen, "GET /first HTTP/1.1\r\nHost: x\r\n\r\n")
    io.log(try response(keptOpen))

    val started = time.instant()
    for i in 0..<5 do
        val r = try http.get("${root}/fast${i}")
        io.log("${r.status} ${r.body.trim()}")
    io.log(time.elapsed(started).micros < 300000)

    // The kept-open connection serves a second request, and two sent at once.
    try net.send(keptOpen, "GET /second HTTP/1.1\r\nHost: x\r\n\r\nGET /third HTTP/1.1\r\nHost: x\r\nConnection: close\r\n\r\n")
    io.log(try response(keptOpen))
    io.log(try response(keptOpen))
    io.log(try response(keptOpen))

    // The slow one sends the rest of its head in time; then nothing more, and is told it took too long.
    try net.send(slow, "\r\n")
    io.log(try response(slow))
    try net.send(slow, "GET /late HTTP/1.1\r\n")
    net.setTimeout(slow, time.seconds(3))
    io.log(try response(slow))
    io.log(try response(slow))

    // The idle one is closed after idleTimeout.
    net.setTimeout(idle, time.seconds(3))
    io.log(try net.readLine(idle) ?? "closed")

    http.stop(server)
    try http.wait(server)
    ret 0
