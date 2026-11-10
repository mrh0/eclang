use "core:io.ec"
use "core:string.ec"
use "core:time.ec"
use "core:net.ec"
use "core:tls.ec"
use "core:http.ec"

fn hello(req: http.Request): http.Response = http.text(200, "secure hello ${req.path} ${req.body}\n")

fn main(): Int throws Error do
    val creds = try tls.server("tests/tls/cert.pem", "tests/tls/key.pem")
    val server = try http.server(0, "127.0.0.1", http.ServerOptions { workers = 2, tls = creds })
    val root = "https://localhost:${server.listener.address.port}"
    try http.start(server, fn hello)

    val trusted = http.Options { tls = net.TlsOptions { caFile = "tests/tls/cert.pem" } }
    for i in 0..<3 do
        val r = try http.get("${root}/n${i}", trusted)
        io.log("${r.status} ${r.body.trim()}")
    val posted = try http.post("${root}/form", "x=1", "text/plain", trusted)
    io.log(posted.body.trim())

    try http.get("${root}/", http.Options {}) catch e do
        io.log(e.message)

    // Plain HTTP to the HTTPS port is dropped, and doesn't stop the server.
    try http.get("http://localhost:${server.listener.address.port}/", http.Options { timeout = time.seconds(2) }) catch e2 do
        io.log("plain http refused")
    val again = try http.get("${root}/again", trusted)
    io.log(again.body.trim())

    http.stop(server)
    try http.wait(server)
    ret 0
