use "core:io.ec"
use "core:string.ec"
use "core:structures.ec"
use "core:time.ec"
use "core:thread.ec"
use "core:net.ec"
use "core:log.ec"
use "core:http.ec"

/** What the handlers share. */
rec Shared as
    hits: thread.Atomic

fn showUrl(text: String) throws Error do
    val u = try http.parseUrl(text)
    val again = http.text(u)
    io.log("[${u.scheme}] [${u.userInfo}] [${u.host}] ${u.port} [${u.path}] [${u.query}] [${u.fragment}] -> ${again}")

fn check(base: http.Url, reference: String, wanted: String) throws Error do
    val got = http.text(try http.resolve(base, reference))
    io.log("${reference} -> ${got}, and it should be ${wanted}") if got != wanted

/** A header of a response, or "?". */
fn header(r: @http.Response, name: String): String = http.get(r.headers, name) ?? "?"

fn handle(req: @http.Request): @http.Response do
    val shared = req.context! !as @Shared
    thread.increment(addrof(shared.hits))
    when req.path do
        "/" do ret http.text(200, "hello ${req.remote.host}\n")
        "/echo" do
            val r = http.response(200, req.body, http.get(req.headers, "Content-Type") ?? "none")
            http.set(r.headers, "X-Method", req.method)
            ret r
        "/query" do
            val a = http.param(req.url, "a") ?? "?"
            val b = http.param(req.url, "b") ?? "?"
            ret http.text(200, "${a}|${b}")
        "/headers" do ret http.text(200, http.get(req.headers, "x-custom") ?? "none")
        "/auth" do ret http.text(200, http.get(req.headers, "Authorization") ?? "none")
        "/json" do ret http.json(201, "{}")
        "/page" do ret http.html(200, "<p>")
        "/empty" do ret http.text(204, "ignored")
        "/same" do ret http.redirect("/")
        "/twice" do ret http.redirect("/same", 301)
        "/back" do ret http.redirect("relative/../echo", 307)
        "/see" do ret http.redirect("/echo", 303)
        "/loop" do ret http.redirect("/loop")
        "/a b" do ret http.text(200, "decoded")
        "/close" do
            val r2 = http.text(200, "bye")
            http.set(r2.headers, "Connection", "close")
            ret r2
        "/broken" do
            val r3 = http.text(200, "x")
            http.set(r3.headers, "X-Bad", "a\r\nInjected: yes")
            ret r3
        "/slow" do
            time.sleep(time.millis(500))
            ret http.text(200, "late")
        else do ret http.text(404, "no ${req.path}\n")

/** A connection to the server. */
fn dial(port: Int): @net.Connection throws Error do
    val c = try net.connect("127.0.0.1", port, time.seconds(2))
    net.setTimeout(c, time.seconds(3))
    ret c

/** Sends raw bytes to the server, and gives back the first line of what it answers. */
fn raw(port: Int, request: String): String throws Error do
    val c = try dial(port)
    defer net.close(c)
    try net.send(c, request)
    ret try net.readLine(c) ?? "(closed)"

/** What a canned server does. */
rec Canned as
    listener: net.Listener
    reply: String
    silent: Bool

/** Serves one connection with a canned answer. */
fn serveCanned(job: @Canned) do
    val c = net.accept(job.listener, time.seconds(5)) ?? panic("nobody came")
    if job.silent do
        time.sleep(time.millis(400))
    else do
        while true do
            val line = (net.readLine(c) ?? "") ?? ""
            break if line.len == 0
        try net.send(c, job.reply) catch e do
            io.log(e.message)
    net.close(c)

/** Makes a request, and shows what comes back. */
fn show(method: String, url: String) throws Error do
    val r = try http.request(method, url)
    val shown = quote(r.body)
    val kind = header(r, "Content-Type")
    io.log("${r.status} [${r.reason}] ${kind} ${shown}")

/** Makes a request to a server that answers `reply`, and shows what comes back. */
fn canned(reply: String, method: String = "GET") do
    val l = net.listen(0, "127.0.0.1") ?? panic("can't listen")
    val t = thread.spawn(fn serveCanned, new Canned { l, reply, false }) ?? panic("can't start a thread")
    val url = "http://127.0.0.1:${l.address.port}/"
    try show(method, url) catch e do
        io.log(e.message)
    try thread.join(t) catch problem do
        panic(problem.message)
    net.close(l)

/** Makes a request to a server that answers `reply`, and shows why it fails. */
fn cannedError(reply: String) do
    val l = net.listen(0, "127.0.0.1") ?? panic("can't listen")
    val t = thread.spawn(fn serveCanned, new Canned { l, reply, false }) ?? panic("can't start a thread")
    val place = net.text(l.address)
    try http.get("http://${place}/") catch e do
        io.log(e.message.replace(place, "<here>"))
    try thread.join(t) catch problem do
        panic(problem.message)
    net.close(l)

/** What each client thread is given. */
rec Client as
    port: Int
    done: @thread.Atomic

fn hammer(c: @Client) do
    for i in 0..<25 do
        pool do
            val body = "message ${i}"
            val r = http.post("http://127.0.0.1:${c.port}/echo", body, "text/plain") ?? panic("a request failed")
            assert r.body == body, "a body came back wrong"
            thread.increment(c.done)

fn fails(req: @http.Request): @http.Response throws Error do
    val n = try parseInt(http.param(req.url, "n") ?? "x")
    ret http.text(200, "${n}")

fn stops(req: @http.Request): @http.Response do
    http.stop(req.context! !as @http.Server)
    ret http.text(200, "stopping")

/** Runs a server until a request stops it. */
fn runServer(s: @http.Server) do
    try http.run(s, fn stops) catch e do
        panic(e.message)

fn urls() throws Error do
    try showUrl("http://example.com")
    try showUrl("http://ada:pw@Example.COM:8080/a/b?x=1&y=2#top")
    try showUrl("HTTP://example.com/")
    try showUrl("http://[::1]:8080/x")
    try showUrl("http://[::1]/")
    try showUrl("/a/b?x=1")
    try showUrl("../c")
    try showUrl("?q")
    try showUrl("#f")
    try showUrl("mailto:ada@example.com")
    try showUrl("http://a/?b=c/d#e?f")
    try showUrl("")
    for bad in ["http://a b/", "http://a/b c", "http://a/\n", "http://a:x/", "http://a:0/", "http://a:70000/", "http://[::1/", "http://[::1]x/"] do
        try http.parseUrl(bad) catch e do
            io.log(e.message)
    io.log(http.target(try http.parseUrl("http://a")))
    io.log(http.target(try http.parseUrl("http://a/b?c=d#e")))

    // The examples of RFC 3986, section 5.4.
    val base = try http.parseUrl("http://a/b/c/d;p?q")
    try check(base, "g:h", "g:h")
    try check(base, "g", "http://a/b/c/g")
    try check(base, "./g", "http://a/b/c/g")
    try check(base, "g/", "http://a/b/c/g/")
    try check(base, "/g", "http://a/g")
    try check(base, "//g", "http://g")
    try check(base, "?y", "http://a/b/c/d;p?y")
    try check(base, "g?y", "http://a/b/c/g?y")
    try check(base, "#s", "http://a/b/c/d;p?q#s")
    try check(base, "g#s", "http://a/b/c/g#s")
    try check(base, "g?y#s", "http://a/b/c/g?y#s")
    try check(base, ";x", "http://a/b/c/;x")
    try check(base, "g;x", "http://a/b/c/g;x")
    try check(base, "g;x?y#s", "http://a/b/c/g;x?y#s")
    try check(base, "", "http://a/b/c/d;p?q")
    try check(base, ".", "http://a/b/c/")
    try check(base, "./", "http://a/b/c/")
    try check(base, "..", "http://a/b/")
    try check(base, "../", "http://a/b/")
    try check(base, "../g", "http://a/b/g")
    try check(base, "../..", "http://a/")
    try check(base, "../../", "http://a/")
    try check(base, "../../g", "http://a/g")
    try check(base, "../../../g", "http://a/g")
    try check(base, "../../../../g", "http://a/g")
    try check(base, "/./g", "http://a/g")
    try check(base, "/../g", "http://a/g")
    try check(base, "g.", "http://a/b/c/g.")
    try check(base, ".g", "http://a/b/c/.g")
    try check(base, "g..", "http://a/b/c/g..")
    try check(base, "..g", "http://a/b/c/..g")
    try check(base, "./../g", "http://a/b/g")
    try check(base, "./g/.", "http://a/b/c/g/")
    try check(base, "g/./h", "http://a/b/c/g/h")
    try check(base, "g/../h", "http://a/b/c/h")
    try check(base, "g;x=1/./y", "http://a/b/c/g;x=1/y")
    try check(base, "g;x=1/../y", "http://a/b/c/y")
    try check(base, "g?y/./x", "http://a/b/c/g?y/./x")
    try check(base, "g?y/../x", "http://a/b/c/g?y/../x")
    try check(base, "g#s/./x", "http://a/b/c/g#s/./x")
    try check(base, "g#s/../x", "http://a/b/c/g#s/../x")
    try check(base, "http:g", "http:g")
    try check(try http.parseUrl("http://a"), "g", "http://a/g")
    io.log("resolved")

    // Queries.
    for (name, value) in try http.parseQuery("a=1&b=x+y&c=%41%26&d&=e&&f=") do
        io.log("[${name}] [${value}]")
    io.log(http.makeQuery([("a b", "c&d"), ("é", "=")]))
    val u = try http.parseUrl("/x?a=1&b=two+words&a=3")
    io.log(http.param(u, "a") ?? "?")
    io.log(http.param(u, "b") ?? "?")
    io.log(http.param(u, "c") == null)
    io.log(http.param(try http.parseUrl("/x?a=%"), "a") == null)
    try http.parseQuery("a=%zz") catch e1 do
        io.log(e1.message)

fn headerLists() do
    val h = http.headers()
    http.add(h, "Accept", "a")
    http.add(h, "ACCEPT", "b")
    http.add(h, "Other", "c")
    io.log(http.get(h, "accept") ?? "?")
    io.log(http.getAll(h, "aCCept").len)
    io.log(http.has(h, "OTHER"))
    http.set(h, "accept", "d")
    io.log(http.getAll(h, "Accept").len)
    io.log(http.get(h, "Accept") ?? "?")
    http.remove(h, "accept")
    io.log(http.has(h, "Accept"))
    io.log(http.get(h, "nope") == null)
    io.log(http.statusText(404))
    io.log(http.statusText(299).len)

fn requests(root: String, port: Int) throws Error do
    val r1 = try http.get("${root}/")
    io.log(r1.status)
    io.log(r1.reason)
    io.log(r1.body)
    io.log(header(r1, "content-type"))
    io.log(header(r1, "Content-Length"))
    io.log(http.has(r1.headers, "Date"))
    io.log(r1.url == concat(root, "/"))
    val missing = try http.get("${root}/nope")
    io.log("${missing.status} ${missing.reason} ${missing.body}")
    val posted = try http.post("${root}/echo", "some data", "text/x-thing")
    io.log(posted.status)
    io.log(posted.body)
    io.log(header(posted, "Content-Type"))
    io.log(header(posted, "X-Method"))
    val binary = "a\x00b\xFF\r\n"
    io.log((try http.post("${root}/echo", binary)).body == binary)
    val form = try http.postForm("${root}/echo", [("a b", "c&d")])
    io.log(form.body)
    io.log(header(form, "Content-Type"))
    io.log((try http.put("${root}/echo", "x")).body)
    io.log(header(try http.put("${root}/echo", "x", "a/b"), "X-Method"))
    io.log(header(try http.delete("${root}/echo"), "X-Method"))
    val headed = try http.head("${root}/")
    io.log(headed.body.len)
    io.log(header(headed, "Content-Length"))
    io.log((try http.get("${root}/query?a=x%20y&b=1+2")).body)
    io.log((try http.get("${root}/a%20b")).body)
    io.log((try http.get("${root}/json")).status)
    io.log(header(try http.get("${root}/json"), "Content-Type"))
    io.log(header(try http.get("${root}/page"), "Content-Type"))
    val empty = try http.get("${root}/empty")
    io.log("${empty.status} ${empty.body.len}")
    io.log(http.has(empty.headers, "Content-Length"))

    // Headers of the client.
    val mine = http.headers()
    http.set(mine, "X-Custom", "yes")
    io.log((try http.get("${root}/headers", http.Options { headers = mine })).body)
    io.log((try http.get("http://ada:secret@127.0.0.1:${port}/auth")).body)
    io.log((try http.get("http://ada%40x:p@127.0.0.1:${port}/auth")).body)

    // What a client refuses to send.
    val injected = http.headers()
    http.set(injected, "X-Bad", "a\r\nInjected: yes")
    try http.get("${root}/", http.Options { headers = injected }) catch e2 do
        io.log(e2.message)
    val badName = http.headers()
    http.set(badName, "Bad Name", "x")
    try http.get("${root}/", http.Options { headers = badName }) catch e3 do
        io.log(e3.message)
    try http.request("BAD METHOD", "${root}/") catch e4 do
        io.log(e4.message)
    try http.get("https://example.com/") catch e5 do
        io.log(e5.message)
        io.log(e5.code == ERR_ARGUMENT)
    try http.get("ftp://example.com/") catch e6 do
        io.log(e6.message)
    try http.get("/relative") catch e7 do
        io.log(e7.message)
    try http.get("http://bad url/") catch e8 do
        io.log(e8.message)

    // Redirects.
    val followed = try http.get("${root}/twice")
    io.log(followed.status)
    io.log(followed.url == concat(root, "/"))
    val direct = try http.get("${root}/same", http.Options { redirects = 0 })
    io.log(direct.status)
    io.log(header(direct, "Location"))
    val kept = try http.post("${root}/back", "kept", "text/plain")
    io.log("${kept.status} ${kept.body}")
    io.log(header(kept, "X-Method"))
    val seen = try http.post("${root}/see", "dropped", "text/plain")
    io.log("${seen.status} [${seen.body}]")
    io.log(header(seen, "X-Method"))
    io.log(header(seen, "Content-Type"))
    io.log((try http.post("${root}/same", "dropped", "text/plain")).status)
    try http.get("${root}/loop") catch e9 do
        io.log(e9.message.replace(root, ""))
    try http.get("${root}/twice", http.Options { redirects = 1 }) catch e10 do
        io.log(e10.message.replace(root, ""))

    // A handler that makes a broken header doesn't reach the client.
    val broken = try http.get("${root}/broken")
    io.log(broken.status)
    io.log(http.has(broken.headers, "Injected"))

fn connections(port: Int) throws Error do
    // What is valid.
    io.log(try raw(port, "GET / HTTP/1.1\r\nHost: x\r\nConnection: close\r\n\r\n"))
    io.log(try raw(port, "\r\nGET / HTTP/1.1\r\nHost: x\r\nConnection: close\r\n\r\n"))
    io.log(try raw(port, "GET http://x/ HTTP/1.1\r\nHost: x\r\nConnection: close\r\n\r\n"))
    io.log(try raw(port, "GET / HTTP/1.0\r\n\r\n"))
    io.log(try raw(port, "OPTIONS * HTTP/1.1\r\nHost: x\r\nConnection: close\r\n\r\n"))
    io.log(try raw(port, "GET / HTTP/1.1\nHost: x\nConnection: close\n\n"))

    // Several requests on one connection.
    val keep = try dial(port)
    for i in 0..<3 do
        try net.send(keep, "POST /echo HTTP/1.1\r\nHost: x\r\nContent-Length: 2\r\n\r\nh${i}")
        io.log(try net.readLine(keep) ?? "?")
        var length: Size = 0
        while true do
            val line = try net.readLine(keep) ?? ""
            break if line.len == 0
            if line.toLower().startsWith("content-length:") do
                length = (try parseInt(line.after(":").trim())) as Size
        io.log(try net.readExactly(keep, length))
    // After a response that says it closes, nothing more is served.
    try net.send(keep, "GET /close HTTP/1.1\r\nHost: x\r\n\r\nGET / HTTP/1.1\r\nHost: x\r\n\r\n")
    val last = try net.readAll(keep)
    io.log(last.contains("bye"))
    io.log(last.contains("Connection: close"))
    io.log(last.contains("hello"))
    net.close(keep)

    // Requests sent at once, without waiting.
    val piped = try dial(port)
    try net.send(piped, "GET /query?a=1 HTTP/1.1\r\nHost: x\r\n\r\nGET /query?a=2 HTTP/1.1\r\nHost: x\r\nConnection: close\r\n\r\n")
    val both = try net.readAll(piped)
    io.log(both.contains("1|?"))
    io.log(both.contains("2|?"))
    net.close(piped)

    // An idle connection is closed after a while.
    val idle = try dial(port)
    io.log((try net.readAll(idle)).len)
    net.close(idle)

    // HTTP/1.0 without keep-alive is closed after the response.
    val old = try dial(port)
    try net.send(old, "GET / HTTP/1.0\r\n\r\n")
    io.log((try net.readAll(old)).contains("Connection: close"))
    net.close(old)

    // Bodies in chunks, and being told to go on.
    val c1 = try dial(port)
    try net.send(c1, "POST /echo HTTP/1.1\r\nHost: x\r\nTransfer-Encoding: Chunked\r\nConnection: close\r\n\r\n5\r\nhello\r\n6;ext=1\r\n world\r\n0\r\nTrailer: x\r\n\r\n")
    io.log((try net.readAll(c1)).endsWith("hello world"))
    net.close(c1)
    val c2 = try dial(port)
    try net.send(c2, "POST /echo HTTP/1.1\r\nHost: x\r\nContent-Length: 2\r\nExpect: 100-continue\r\nConnection: close\r\n\r\n")
    io.log(try net.readLine(c2) ?? "?")
    io.log((try net.readLine(c2) ?? "?").len)
    try net.send(c2, "ok")
    io.log((try net.readAll(c2)).endsWith("ok"))
    net.close(c2)

fn refusals(port: Int, root: String) throws Error do
    val close = "Host: x\r\nConnection: close\r\n"
    io.log(try raw(port, "GET /\r\n\r\n"))
    io.log(try raw(port, "GET / HTTP/1.1 extra\r\n\r\n"))
    io.log(try raw(port, "GE T / HTTP/1.1\r\n\r\n"))
    io.log(try raw(port, "G(T / HTTP/1.1\r\n\r\n"))
    io.log(try raw(port, "GET x HTTP/1.1\r\n\r\n"))
    io.log(try raw(port, "GET / HTTP/2.0\r\n\r\n"))
    io.log(try raw(port, "GET /%zz HTTP/1.1\r\n${close}\r\n"))
    io.log(try raw(port, "GET / HTTP/1.1\r\n\r\n"))
    io.log(try raw(port, "GET / HTTP/1.1\r\nHost: a\r\nHost: b\r\n\r\n"))
    io.log(try raw(port, "GET / HTTP/1.1\r\n${close}no colon\r\n\r\n"))
    io.log(try raw(port, "GET / HTTP/1.1\r\n${close}Bad Name: x\r\n\r\n"))
    io.log(try raw(port, "GET / HTTP/1.1\r\n${close}Bad : x\r\n\r\n"))
    io.log(try raw(port, "GET / HTTP/1.1\r\n${close}: x\r\n\r\n"))
    io.log(try raw(port, "GET / HTTP/1.1\r\n${close}X: a\r\n b\r\n\r\n"))
    io.log(try raw(port, "GET / HTTP/1.1\r\n${close}X: a\rb\r\n\r\n"))
    io.log(try raw(port, concat("GET / HTTP/1.1\r\n", close, "X: a\x00b\r\n\r\n")))

    // What could smuggle a second request past a proxy.
    io.log(try raw(port, "POST /echo HTTP/1.1\r\n${close}Content-Length: 1\r\nTransfer-Encoding: chunked\r\n\r\n0\r\n\r\n"))
    io.log(try raw(port, "POST /echo HTTP/1.1\r\n${close}Content-Length: 1\r\nContent-Length: 2\r\n\r\nab"))
    io.log(try raw(port, "POST /echo HTTP/1.1\r\n${close}Content-Length: 1, 2\r\n\r\nab"))
    io.log(try raw(port, "POST /echo HTTP/1.1\r\n${close}Content-Length: -1\r\n\r\n"))
    io.log(try raw(port, "POST /echo HTTP/1.1\r\n${close}Content-Length: +1\r\n\r\na"))
    io.log(try raw(port, "POST /echo HTTP/1.1\r\n${close}Content-Length: 99999999999999999999\r\n\r\n"))
    io.log(try raw(port, "POST /echo HTTP/1.1\r\n${close}Transfer-Encoding: gzip\r\n\r\n"))
    io.log(try raw(port, "POST /echo HTTP/1.1\r\n${close}Transfer-Encoding: chunked\r\nTransfer-Encoding: chunked\r\n\r\n"))
    io.log(try raw(port, "POST /echo HTTP/1.1\r\n${close}Transfer-Encoding: chunked\r\n\r\nzz\r\n"))
    io.log(try raw(port, "POST /echo HTTP/1.1\r\n${close}Transfer-Encoding: chunked\r\n\r\n2\r\nabc\r\n0\r\n\r\n"))

    // What is too large.
    io.log(try raw(port, "POST /echo HTTP/1.1\r\n${close}Content-Length: 1001\r\n\r\n"))
    io.log(try raw(port, "POST /echo HTTP/1.1\r\n${close}Transfer-Encoding: chunked\r\n\r\n3e9\r\n"))
    val longLine = repeat("a", 20000)
    io.log(try raw(port, "GET /${longLine} HTTP/1.1\r\n\r\n"))
    io.log(try raw(port, "GET / HTTP/1.1\r\n${close}X: ${longLine}\r\n\r\n"))
    val many = stringBuilder()
    for i in 0..<300 do
        many.append("X-${i}: y\r\n")
    io.log(try raw(port, concat("GET / HTTP/1.1\r\n${close}", many.build(), "\r\n")))
    val tall = stringBuilder()
    val filler = repeat("a", 1000)
    for i in 0..<80 do
        tall.append("X: ${filler}\r\n")
    io.log(try raw(port, concat("GET / HTTP/1.1\r\n${close}", tall.build(), "\r\n")))
    val big = try http.post("${root}/echo", repeat("x", 1000))
    io.log("${big.status} ${big.body.len}")
    io.log((try http.post("${root}/echo", repeat("x", 1001))).status)

fn oddServers() throws Error do
    // Bodies of every kind.
    canned("HTTP/1.1 200 OK\r\nContent-Length: 5\r\n\r\nhello")
    canned("HTTP/1.1 200 OK\r\nContent-Length: 5\r\n\r\nhello and more")
    canned("HTTP/1.1 200 OK\r\nContent-Type: a/b\r\n\r\nuntil it closes")
    canned("HTTP/1.1 200 OK\r\nTransfer-Encoding: chunked\r\n\r\n5\r\nhello\r\n1\r\n \r\nA;x=y\r\n0123456789\r\n0\r\nX: y\r\n\r\n")
    canned("HTTP/1.0 200 OK\r\n\r\nold")
    canned("HTTP/1.1 200 \r\nContent-Length: 0\r\n\r\n")
    canned("HTTP/1.1 200\r\nContent-Length: 0\r\n\r\n")
    canned("HTTP/1.1 299 Odd Status\r\nContent-Length: 0\r\n\r\n")
    canned("HTTP/1.1 100 Continue\r\n\r\nHTTP/1.1 102 Processing\r\n\r\nHTTP/1.1 200 OK\r\nContent-Length: 2\r\n\r\nok")
    canned("HTTP/1.1 204 No Content\r\nContent-Length: 5\r\n\r\n")
    canned("HTTP/1.1 304 Not Modified\r\n\r\n")
    canned("HTTP/1.1 200 OK\r\nContent-Length: 5\r\n\r\n", "HEAD")
    canned("HTTP/1.1 200 OK\nContent-Length: 2\n\nok")

    // Answers that aren't valid.
    cannedError("")
    cannedError("not http\r\n\r\n")
    cannedError("HTTP/1.1 abc OK\r\n\r\n")
    cannedError("HTTP/1.1 20 OK\r\n\r\n")
    cannedError("HTTP/2 200 OK\r\n\r\n")
    cannedError("HTTP/1.1 200 OK\r\nno colon\r\n\r\n")
    cannedError("HTTP/1.1 200 OK\r\n")
    cannedError("HTTP/1.1 200 OK\r\nContent-Length: 10\r\n\r\nshort")
    cannedError("HTTP/1.1 200 OK\r\nContent-Length: x\r\n\r\n")
    cannedError("HTTP/1.1 200 OK\r\nContent-Length: 1\r\nContent-Length: 2\r\n\r\n")
    cannedError("HTTP/1.1 200 OK\r\nContent-Length: 1\r\nTransfer-Encoding: chunked\r\n\r\n")
    cannedError("HTTP/1.1 200 OK\r\nTransfer-Encoding: gzip\r\n\r\n")
    cannedError("HTTP/1.1 200 OK\r\nTransfer-Encoding: chunked\r\n\r\n5\r\nhe")
    cannedError("HTTP/1.1 200 OK\r\nTransfer-Encoding: chunked\r\n\r\n5\r\nhello")
    cannedError("HTTP/1.1 200 OK\r\nTransfer-Encoding: chunked\r\n\r\nzz\r\n")
    cannedError("HTTP/1.1 200 OK\r\nTransfer-Encoding: chunked\r\n\r\n2\r\nabc\r\n")
    cannedError("HTTP/1.1 302 Found\r\nLocation: http://bad url/\r\n\r\n")

    // A limit on the body.
    val l = try net.listen(0, "127.0.0.1")
    val t = try thread.spawn(fn serveCanned, new Canned { l, "HTTP/1.1 200 OK\r\n\r\nmore than ten bytes", false })
    try http.get("http://127.0.0.1:${l.address.port}/", http.Options { maxBody = 10 }) catch e do
        io.log(e.message)
    try thread.join(t)
    val t2 = try thread.spawn(fn serveCanned, new Canned { l, "HTTP/1.1 200 OK\r\nContent-Length: 11\r\n\r\nmore than 10", false })
    try http.get("http://127.0.0.1:${l.address.port}/", http.Options { maxBody = 10 }) catch e2 do
        io.log(e2.message)
    try thread.join(t2)

    // A server that doesn't answer.
    val t3 = try thread.spawn(fn serveCanned, new Canned { l, "", true })
    try http.get("http://127.0.0.1:${l.address.port}/", http.Options { timeout = time.millis(50) }) catch e3 do
        io.log(e3.code == ERR_TIMEOUT)
    try thread.join(t3)
    net.close(l)

fn main(): Int throws Error do
    try urls()
    headerLists()

    // A server, and a client talking to it.
    log.setLevel(:off)
    val shared = new Shared { thread.Atomic { 0 } }
    val options = http.ServerOptions { workers = 4, context = shared, maxBody = 1000, idleTimeout = time.millis(300) }
    val s = try http.server(0, "127.0.0.1", options)
    try http.start(s, fn handle)
    val port = s.listener.address.port
    val root = "http://127.0.0.1:${port}"
    try requests(root, port)
    try connections(port)
    try refusals(port, root)
    try oddServers()

    // Many clients at once.
    val done = new thread.Atomic { 0 }
    val clients: @List<thread.Thread> = list()
    for i in 0..<8 do
        clients.push(try thread.spawn(fn hammer, new Client { port, done }))
    for t in clients do
        try thread.join(t)
    io.log(thread.load(done))
    io.log(thread.load(addrof(shared.hits)) > 200)

    // Stopping waits for the request being served.
    val slow = try dial(port)
    try net.send(slow, "GET /slow HTTP/1.1\r\nHost: x\r\n\r\n")
    time.sleep(time.millis(200))
    http.stop(s)
    try http.wait(s)
    val finished = try net.readAll(slow)
    io.log(finished.endsWith("late"))
    io.log(finished.contains("Connection: close"))
    net.close(slow)
    try http.get("${root}/") catch e do
        io.log(e.message.contains("refused"))

    // A handler that can fail.
    val path = io.joinPath(try io.tempDir(), "eclang-std-http.log")
    log.setLevel(:error)
    log.setTimestamps(:none)
    try log.toFile(path)
    val s2 = try http.server(0, "127.0.0.1")
    try http.start(s2, fn fails)
    val root2 = "http://127.0.0.1:${s2.listener.address.port}"
    io.log((try http.get("${root2}/?n=5")).body)
    val failed = try http.get("${root2}/x?n=z")
    io.log(failed.status)
    io.log(failed.body)
    http.stop(s2)
    try http.wait(s2)
    log.toStderr()
    io.log(try io.readFile(path))
    try io.remove(path)
    log.setLevel(:off)

    // A handler that stops the server, which runs on a thread of its own.
    val s3 = try http.server(0, "127.0.0.1")
    s3.options.context = s3
    val runner = try thread.spawn(fn runServer, s3)
    val root3 = "http://127.0.0.1:${s3.listener.address.port}"
    time.sleep(time.millis(50))
    io.log((try http.get("${root3}/")).body)
    try thread.join(runner)
    io.log("stopped")

    // A port that is taken.
    val taken = try net.listen(0, "127.0.0.1")
    try http.server(taken.address.port, "127.0.0.1") catch e2 do
        io.log(e2.message.contains("in use"))
    net.close(taken)
    ret 0
