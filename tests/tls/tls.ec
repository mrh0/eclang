use "core:io.ec"
use "core:string.ec"
use "core:time.ec"
use "core:thread.ec"
use "core:net.ec"
use "core:tls.ec"

rec Setup as
    listener: net.Listener
    creds: net.TlsServer
    rounds: Int

// Answers each connection's first line in upper case, over TLS.
fn serve(s: @Setup) do
    for i in 0..<s.rounds do
        pool do
            val c = net.accept(s.listener) ?? ret
            net.setTimeout(c, time.seconds(5))
            try net.secure(c, s.creds) catch e do
                net.close(c)
                continue
            val line = (net.readLine(c) ?? null) ?? ""
            val answer = line.toUpper()
            net.send(c, "${answer}\n") ?? continue
            net.close(c)

fn talk(host: String, port: Int, options: net.TlsOptions) throws Error do
    val c = try net.connect("127.0.0.1", port, time.seconds(5))
    defer net.close(c)
    net.setTimeout(c, time.seconds(5))
    try net.secure(c, host, options) catch e do
        io.log("refused: ${e.message}")
        ret
    io.log(net.isSecure(c))
    io.log(tls.protocol(c) ?? "?")
    try net.send(c, "hello over tls\n")
    io.log(try net.readLine(c) ?? "nothing")
    io.log(try net.readLine(c) ?? "ended")

fn main(): Int throws Error do
    val l = try net.listen(0, "127.0.0.1")
    val creds = try tls.server("tests/tls/cert.pem", "tests/tls/key.pem")
    val setup = new Setup { l, creds, 5 }
    val t = try unsafe thread.spawn(fn serve, setup)
    val trusted = net.TlsOptions { caFile = "tests/tls/cert.pem" }
    try talk("localhost", l.address.port, trusted)
    try talk("127.0.0.1", l.address.port, trusted)
    try talk("localhost", l.address.port, net.TlsOptions {})
    try talk("example.com", l.address.port, trusted)
    try talk("example.com", l.address.port, net.TlsOptions { verify = false })
    try thread.join(t)

    try tls.server("tests/tls/cert.pem", "tests/tls/missing.pem") catch e do
        io.log(e.message.contains("missing.pem"))
    val plain = net.TlsOptions {}
    io.log(plain.verify)
    ret 0
