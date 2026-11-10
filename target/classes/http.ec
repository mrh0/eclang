/**
 * HTTP/1.1: parsing URLs, a client, and a small server. Built on core:net.ec.
 *
 *     use "core:http.ec"
 *     val r = try http.get("http://example.com/")
 *     io.log(r.status)                                    // 200
 *     io.log(http.get(r.headers, "Content-Type") ?? "?")  // text/html; charset=UTF-8
 *     io.log(r.body)
 *
 *     val sent = try http.post("http://example.com/api", "{\"id\": 1}", "application/json")
 *     val slow = try http.get(url, http.Options { timeout = time.seconds(60), redirects = 0 })
 *
 *     val u = try http.parseUrl("http://ada@example.com:8080/a/b?x=1&y=2#top")
 *     io.log(u.host)                                      // example.com
 *     io.log(http.param(u, "y") ?? "?")                   // 2
 *     io.log(http.text(try http.resolve(u, "../c")))      // http://ada@example.com:8080/c
 *
 *     fn hello(req: http.Request): http.Response do
 *         ret http.text(404, "nothing here") if req.path != "/"
 *         val name = http.param(req.url, "name") ?? "world"
 *         ret http.text(200, "hello ${name}\n")
 *
 *     val server = try http.server(8080)
 *     try http.run(server, fn hello)                      // serves until http.stop(server)
 *
 * There is no HTTPS: that needs a TLS library, which a program can't link yet, so an `https:` URL is an error that says
 * so. For the same reason of size, there are no cookies, no compression, and the client opens a new connection for each
 * request. A redirect is followed for GET and HEAD, and for the others as browsers do. Both sides read `chunked` bodies.
 * Everything is read into memory; the limits on it are `maxBody` in {@link Options} and {@link ServerOptions}.
 *
 * The server keeps a fixed number of threads, each serving one connection at a time, so handlers run at the same time
 * and whatever they share needs a lock (core:thread.ec). Each request has its own pool, freed after its response is
 * sent. A handler can be a lambda that uses the variables around it (or reach the program's data through `req.context`). A panic in a
 * handler ends the program. The server is strict about what it reads, and limits the size of a request, but it is small:
 * in front of the open Internet, put it behind a reverse proxy.
 *
 * Failures of the network are OsErrors, and answers that aren't valid HTTP are ParseErrors; both are Errors. A status
 * such as 404 isn't a failure: it is in {@link Response}. What is returned is allocated in the current pool.
 */
module "http"
use "core:errors.ec"
use "core:string.ec"
use "core:structures.ec"
use "core:encoding.ec"
use "core:time.ec"
use "core:thread.ec"
use "core:net.ec"
use "core:log.ec"

// ---------------------------------------------------------------------------------------------------
// URLs

/**
 * The parts of a URL, as in `http://user@host:8080/path?query#fragment`. Only `host` is changed, to lower case; the rest
 * is as written, with its %XX escapes.
 */
mut rec Url as
    /** What comes before the ":", in lower case; "" in a relative URL. */
    scheme: String = ""
    /** What comes before the "@"; "" for nothing. */
    userInfo: String = ""
    /** The name or IP address, without the [ ] of IPv6, in lower case; "" when it has none. */
    host: String = ""
    /** The port; 0 when it doesn't say. */
    port: Int = 0
    /** The path; it can be "". */
    path: String = ""
    /** What comes after the "?", without it; "" for nothing. */
    query: String = ""
    /** What comes after the "#", without it; "" for nothing. */
    fragment: String = ""

/** Whether `s` can be the scheme of a URL. */
priv fn isScheme(s: String): Bool do
    ret false if s.len == 0 or not isAlpha(s[0])
    for c in s do
        ret false if not (isAlnum(c) or c == '+' or c == '-' or c == '.')
    ret true

/**
 * Reads what is between "//" and the path into `u`.
 * @throws when the port or the brackets of an IPv6 address aren't valid
 */
priv fn parseAuthority(mut u: Url, s: String, text: String) throws ParseError do
    var rest = s
    if val at = lastIndexOf(rest, '@') do
        u.userInfo = rest[0..<at]
        rest = rest[at + 1..]
    var portText = ""
    if rest.startsWith('[') do
        val close = indexOf(rest, ']') ?? throw parseError("the [ of this IPv6 address has no closing ]", text, 0)
        u.host = rest[1..<close].toLower()
        val after = rest[close + 1..]
        throw parseError("only a port can follow the ] of an IPv6 address", text, 0) if after.len > 0 and after[0] != ':'
        portText = after.drop(1)
    else do
        val at2 = lastIndexOf(rest, ':') ?? rest.len
        u.host = rest[0..<at2].toLower()
        portText = rest.drop(at2 + 1)
    if portText.len > 0 do
        for c in portText do
            throw parseError("the port of a URL is a number from 1 to 65535", text, 0) if not isDigit(c)
        val port = parseInt(portText) ?? 0
        throw parseError("the port of a URL is a number from 1 to 65535", text, 0) if port < 1 or port > 65535
        u.port = port as Int

/**
 * Splits a URL into its parts, without looking any of them up. A URL can be relative: "/a/b?x=1", "../c".
 * @throws when it has white space or control characters in it, or its port isn't valid
 */
fn parseUrl(text: String): Url throws ParseError do
    for i, c in text do
        throw parseError("a URL can't have white space or control characters in it: write them as %XX", text, i) if (c >= 0 and c <= ' ') or c == 127
    var u = Url { path = "" }
    var rest = text
    if val hash = indexOf(rest, '#') do
        u.fragment = rest[hash + 1..]
        rest = rest[0..<hash]
    if val question = indexOf(rest, '?') do
        u.query = rest[question + 1..]
        rest = rest[0..<question]
    if val colon = indexOf(rest, ':') do
        if isScheme(rest[0..<colon]) do
            u.scheme = rest[0..<colon].toLower()
            rest = rest[colon + 1..]
    if rest.startsWith("//") do
        val end = indexOf(rest, "/", 2) ?? rest.len
        try parseAuthority(mut u, rest[2..<end], text)
        rest = rest[end..]
    u.path = rest
    ret u

/** What is between "//" and the path of `u`. */
priv fn authority(u: Url): String do
    val host = if u.host.contains(':') do concat("[", u.host, "]") else u.host
    val user = if u.userInfo.len > 0 do concat(u.userInfo, "@") else ""
    ret if u.port != 0 do format("${user}${host}:${u.port}") else concat(user, host)

/** `u` as text. */
fn text(u: Url): String do
    val b = stringBuilder(64)
    if u.scheme.len > 0 do
        b.append(u.scheme)
        b.append(':')
    if u.host.len > 0 do
        b.append("//")
        b.append(authority(u))
    b.append(u.path)
    if u.query.len > 0 do
        b.append('?')
        b.append(u.query)
    if u.fragment.len > 0 do
        b.append('#')
        b.append(u.fragment)
    ret b.build()

/** The path and query of `u`, as they go in a request: "/a/b?x=1", and "/" for no path. */
fn target(u: Url): String do
    val path = if u.path.len == 0 do "/" else u.path
    ret if u.query.len > 0 do concat(path, "?", u.query) else path

/** `path` without its "." and ".." segments, as RFC 3986 says. */
priv fn withoutDots(path: String): String do
    val out: List<String> = list()
    var rest = path
    while rest.len > 0 do
        if rest.startsWith("../") do
            rest = rest[3..]
        else if rest.startsWith("./") do
            rest = rest[2..]
        else if rest.startsWith("/./") do
            rest = rest[2..]
        else if rest == "/." do
            rest = "/"
        else if rest.startsWith("/../") or rest == "/.." do
            rest = if rest == "/.." do "/" else rest[3..]
            out.pop()
        else if rest == "." or rest == ".." do
            rest = ""
        else do
            val end = indexOf(rest, "/", 1) ?? rest.len
            out.push(rest[0..<end])
            rest = rest[end..]
    ret join(out.items(), "")

/**
 * The URL that `reference` means on a page at `base`, as RFC 3986 says: `resolve(u, "../c")`. A reference that is a full
 * URL is that URL.
 * @throws when `reference` isn't a valid URL
 */
fn resolve(base: Url, reference: String): Url throws ParseError do
    val r = try parseUrl(reference)
    ret Url { ...r, path = withoutDots(r.path) } if r.scheme.len > 0
    ret Url { ...r, scheme = base.scheme, path = withoutDots(r.path) } if r.host.len > 0
    if r.path.len == 0 do
        ret Url { ...base, query = if r.query.len > 0 do r.query else base.query, fragment = r.fragment }
    if r.path.startsWith('/') do
        ret Url { ...base, path = withoutDots(r.path), query = r.query, fragment = r.fragment }
    // What comes after the last "/" of the base gives way to the reference.
    val slash = lastIndexOf(base.path, '/')
    val directory = if slash != null do base.path[0..=slash] else if base.host.len > 0 do "/" else ""
    ret Url { ...base, path = withoutDots(concat(directory, r.path)), query = r.query, fragment = r.fragment }

/**
 * The `name=value` pairs of a query or form: `parseQuery("a=1&b=x+y")` gives ("a", "1") and ("b", "x y").
 * @return the pairs, in their order, without their escapes; a part without "=" has "" as its value
 * @throws when an escape isn't valid
 */
fn parseQuery(query: String): (String, String)[] throws ParseError do
    val pairs: List<(String, String)> = list()
    for part in query.split('&') where part.len > 0 do
        val at = indexOf(part, '=') ?? part.len
        val name = try encoding.urlUnescape(part[0..<at], true)
        val value = try encoding.urlUnescape(part.drop(at + 1), true)
        pairs.push((name, value))
    ret pairs.items()

/** The query or form of the pairs, with escapes: `makeQuery([("a", "x y")])` is "a=x%20y". */
fn makeQuery(pairs: (String, String)[]): String do
    val b = stringBuilder(64)
    for i, (name, value) in pairs do
        b.append('&') if i > 0
        b.append(encoding.urlEscape(name))
        b.append('=')
        b.append(encoding.urlEscape(value))
    ret b.build()

/** The first value called `name` in the query of `u`; null when it has none, or the query isn't valid. */
fn param(u: Url, name: String): String? do
    if val pairs = parseQuery(u.query) do
        for (key, value) in pairs do
            ret value if key == name
    ret null

// ---------------------------------------------------------------------------------------------------
// Headers

/** A field of the head of a request or response. */
rec Header as
    /** Its name, as it was written; names are the same in any case. */
    name: String
    /** Its value. */
    value: String

/** The headers of a request or response, in their order: {@link headers}. A name can be there several times. */
ref rec Headers as
    /** The fields. */
    fields: List<Header>

/** New empty headers, in the current pool. */
fn headers(): Headers do
    val fields: List<Header> = list()
    ret new Headers { fields }

/** Adds a header, next to others of that name. */
fn add(h: Headers, name: String, value: String) do
    h.fields.push(Header { name, value })

/** Removes all the headers called `name`. */
fn remove(h: Headers, name: String) do
    var kept: Size = 0
    for field in h.fields.items() do
        if not equalsIgnoreCase(field.name, name) do
            h.fields.buffer[kept] = field
            kept += 1
    h.fields.len = kept

/** Sets the header `name`, in place of those of that name. */
fn set(h: Headers, name: String, value: String) do
    remove(h, name)
    add(h, name, value)

/** The first header called `name`, in any case, or null when there is none. */
fn get(h: Headers, name: String): String? do
    for field in h.fields do
        ret field.value if equalsIgnoreCase(field.name, name)
    ret null

/** All the headers called `name`, in any case. */
fn getAll(h: Headers, name: String): String[] do
    val found: List<String> = list()
    for field in h.fields do
        found.push(field.value) if equalsIgnoreCase(field.name, name)
    ret found.items()

/** Whether there is a header called `name`, in any case. */
fn has(h: Headers, name: String): Bool = get(h, name) != null

/** Whether `s` is a token of HTTP: the form of a method or the name of a header. */
priv fn isToken(s: String): Bool do
    ret false if s.len == 0
    for c in s do
        ret false if c <= ' ' or c == 127 or "()<>@,;:\\\"/[]?={}".indexOf(c) != null
    ret true

/** Whether `s` can be the value of a header. A line break in one would start another header, or the body. */
priv fn isHeaderValue(s: String): Bool do
    for c in s do
        ret false if (c >= 0 and c < ' ' and c != '\t') or c == 127
    ret true

/** Whether one of the comma-separated tokens of the headers called `name` is `token`, in any case. */
priv fn hasToken(h: Headers, name: String, token: String): Bool do
    for value in getAll(h, name) do
        for part in value.split(',') do
            ret true if equalsIgnoreCase(part.trim(), token)
    ret false

// ---------------------------------------------------------------------------------------------------
// Messages

/** A request: what {@link Handler}s get. */
ref rec Request as
    /** "GET", "POST", ... */
    method: String
    /** The URL as it was in the request: usually only a path and a query. */
    url: Url
    /** The path, without its %XX escapes. */
    path: String
    /** The headers. */
    headers: Headers
    /** The body, and "" for none. */
    body: String
    /** Who sent it. */
    remote: net.Address
    /** What was given in {@link ServerOptions}, to reach the data of the program. */
    context: @Any?

/** A response, from a server to a client. */
ref rec Response as
    /** The status: 200, 404, ... */
    status: Int
    /** The text after the status: "OK", "Not Found", ... */
    reason: String
    /** The headers. */
    headers: Headers
    /** The body, and "" for none. */
    body: String
    /** Where it came from, after any redirects; "" on the server. */
    url: String = ""

/** The usual text of a status, or "" for one that has none. */
fn statusText(status: Int): String do
    when status do
        100 do ret "Continue"
        101 do ret "Switching Protocols"
        200 do ret "OK"
        201 do ret "Created"
        202 do ret "Accepted"
        204 do ret "No Content"
        206 do ret "Partial Content"
        301 do ret "Moved Permanently"
        302 do ret "Found"
        303 do ret "See Other"
        304 do ret "Not Modified"
        307 do ret "Temporary Redirect"
        308 do ret "Permanent Redirect"
        400 do ret "Bad Request"
        401 do ret "Unauthorized"
        403 do ret "Forbidden"
        404 do ret "Not Found"
        405 do ret "Method Not Allowed"
        406 do ret "Not Acceptable"
        408 do ret "Request Timeout"
        409 do ret "Conflict"
        410 do ret "Gone"
        411 do ret "Length Required"
        413 do ret "Content Too Large"
        415 do ret "Unsupported Media Type"
        422 do ret "Unprocessable Content"
        429 do ret "Too Many Requests"
        431 do ret "Request Header Fields Too Large"
        500 do ret "Internal Server Error"
        501 do ret "Not Implemented"
        502 do ret "Bad Gateway"
        503 do ret "Service Unavailable"
        504 do ret "Gateway Timeout"
        505 do ret "HTTP Version Not Supported"
        else do ret ""

/** A response with this body and `Content-Type`. */
fn response(status: Int, body: String, contentType: String): Response do
    val h = headers()
    add(h, "Content-Type", contentType)
    ret new Response { status, statusText(status), h, body }

/** A response of plain text. */
fn text(status: Int, body: String): Response = response(status, body, "text/plain; charset=utf-8")

/** A response of HTML. */
fn html(status: Int, body: String): Response = response(status, body, "text/html; charset=utf-8")

/** A response of JSON, which `body` must be: `http.json(200, json.stringify(value))`. */
fn json(status: Int, body: String): Response = response(status, body, "application/json")

/** A response that sends the client to `location`, with the status 302, or the one given. */
fn redirect(location: String, status: Int = 302): Response do
    val r = text(status, "")
    set(r.headers, "Location", location)
    ret r

// ---------------------------------------------------------------------------------------------------
// Reading and writing messages

/** The longest line of a head. */
priv const MAX_LINE: Size = 16384
/** How much of a head there can be. */
priv const MAX_HEAD: Size = 65536
/** How many headers there can be. */
priv const MAX_HEADERS: Size = 200

/** Reading a message, which can go wrong in a way the server answers with a status. */
priv mut rec Reading as
    /** The connection. */
    c: net.Connection
    /** The largest body to read. */
    maxBody: Size
    /** The status to answer with when reading failed; 0 when the other side isn't to blame. */
    refusal: Int = 0
    /** How much of the head has been read. */
    headSize: Size = 0

/** A ParseError for something the other side sent, noting the status the server answers it with. */
priv fn refuse(r: @Reading, status: Int, message: String): ParseError do
    r.refusal = status
    ret parseError(message, "", 0)

/**
 * Reads a line of the head.
 * @return the line, or null at the end of the connection
 * @throws when it is too long, or reading fails
 */
priv fn headLine(r: @Reading): String? throws Error do
    val line = try net.readLine(r.c, MAX_LINE)
    if val text = line do
        r.headSize += text.len + 2
        throw refuse(r, 431, "the head of this message is longer than ${MAX_HEAD} bytes") if r.headSize > MAX_HEAD
    ret line

/**
 * Reads the headers up to the empty line after them.
 * @throws when they aren't valid, or there are too many
 */
priv fn readHeaders(r: @Reading): Headers throws Error do
    val h = headers()
    while true do
        val line = try headLine(r) ?? throw refuse(r, 400, "the connection ended inside the headers")
        break if line.len == 0
        throw refuse(r, 400, "a header can't continue on the next line") if line[0] == ' ' or line[0] == '\t'
        val colon = indexOf(line, ':') ?? throw refuse(r, 400, "this header has no colon")
        val name = line[0..<colon]
        throw refuse(r, 400, "this isn't the name of a header: ${quote(name)}") if not isToken(name)
        val value = trimValue(line[colon + 1..])
        throw refuse(r, 400, "the header ${name} has a control character in it") if not isHeaderValue(value)
        throw refuse(r, 431, "there are more than ${MAX_HEADERS} headers") if h.fields.len >= MAX_HEADERS
        add(h, name, value)
    ret h

/** `s` without the spaces and tabs around it. */
priv fn trimValue(s: String): String do
    var start: Size = 0
    var end = s.len
    while start < end and (s[start] == ' ' or s[start] == '\t') do
        start += 1
    while end > start and (s[end - 1] == ' ' or s[end - 1] == '\t') do
        end -= 1
    ret s[start..<end]

/**
 * The value of `Content-Length`, or null when there is none.
 * @throws when it isn't a number, or there are several that differ, which is how requests are smuggled past a proxy
 */
priv fn contentLength(r: @Reading, h: Headers): Size? throws ParseError do
    var length: Size? = null
    for value in getAll(h, "Content-Length") do
        throw refuse(r, 400, "Content-Length isn't a number") if value.len == 0 or value.len > 18
        for c in value do
            throw refuse(r, 400, "Content-Length isn't a number") if not isDigit(c)
        val n = (parseInt(value) ?? 0) as Size
        if val other = length do
            throw refuse(r, 400, "this message has two different values of Content-Length") if other != n
        length = n
    ret length

/**
 * Reads the size at the start of a chunk.
 * @throws when it isn't valid
 */
priv fn chunkSize(r: @Reading): Size throws Error do
    val line = try headLine(r) ?? throw refuse(r, 400, "the connection ended inside a chunked body")
    r.headSize = 0
    val digits = before(line, ";").trim()
    throw refuse(r, 400, "this isn't the size of a chunk: ${quote(line)}") if digits.len == 0 or digits.len > 15
    var size: Size = 0
    for c in digits do
        throw refuse(r, 400, "this isn't the size of a chunk: ${quote(line)}") if not isHexDigit(c)
        size = size * 16 + ((digitValue(c) ?? 0) as Size)
    ret size

/**
 * Reads a chunked body, and the headers that can follow it.
 * @throws when it isn't valid, or is larger than allowed
 */
priv fn readChunked(r: @Reading): String throws Error do
    val body = stringBuilder(1024)
    while true do
        val size = try chunkSize(r)
        break if size == 0
        throw refuse(r, 413, "this body is larger than ${r.maxBody} bytes") if body.len + size > r.maxBody
        body.append(try net.readExactly(r.c, size))
        val end = try headLine(r) ?? throw refuse(r, 400, "the connection ended inside a chunked body")
        throw refuse(r, 400, "a chunk doesn't end where its size says") if end.len != 0
    try readHeaders(r)
    ret body.build()

/**
 * Reads a body of `length` bytes.
 * @throws when it is larger than allowed, or the connection ends before that
 */
priv fn readSized(r: @Reading, length: Size): String throws Error do
    throw refuse(r, 413, "this body is larger than ${r.maxBody} bytes") if length > r.maxBody
    ret try net.readExactly(r.c, length)

/**
 * The value of `Transfer-Encoding`, checked: whether it says `chunked`.
 * @throws when it says something else, or there is a Content-Length next to it, which is how requests are smuggled past a proxy
 */
priv fn isChunked(r: @Reading, h: Headers): Bool throws ParseError do
    ret false if not has(h, "Transfer-Encoding")
    throw refuse(r, 400, "this message has both Transfer-Encoding and Content-Length") if has(h, "Content-Length")
    val values = getAll(h, "Transfer-Encoding")
    throw refuse(r, 501, "the only Transfer-Encoding here is chunked") if values.len != 1 or not equalsIgnoreCase(values[0].trim(), "chunked")
    ret true

/**
 * Writes the head of a message into `b`.
 * @throws when a header would break the message
 */
priv fn addHeaders(b: StringBuilder, h: Headers) throws ArgumentError do
    for field in h.fields do
        throw argumentError("headers", "this isn't the name of a header: ${quote(field.name)}") if not isToken(field.name)
        throw argumentError("headers", "the header ${field.name} has a line break or control character in its value") if not isHeaderValue(field.value)
        b.append(field.name)
        b.append(": ")
        b.append(field.value)
        b.append("\r\n")
    b.append("\r\n")

// ---------------------------------------------------------------------------------------------------
// The client

/** How a client makes a request: `http.Options { timeout = time.seconds(5) }`. */
rec Options as
    /** How long connecting, and each read and write, may take; null for as long as it takes. */
    timeout: time.Duration? = time.seconds(30)
    /** How many redirects to follow; 0 gives the redirect itself as the response. */
    redirects: Int = 5
    /** Headers to send, next to those that are always sent. They take precedence over those. */
    headers: Headers? = null
    /** The largest body to read. */
    maxBody: Size = 1073741824

/** The {@link Options} that are used when none are given. */
fn defaults(): Options = Options { redirects = 5 }

/** The headers of a request: those of the options, and what is missing from them. */
priv fn requestHeaders(u: Url, method: String, body: String, options: Options): Headers do
    val h = headers()
    if val given = options.headers do
        h.fields.pushAll(given.fields.items())
    if not has(h, "Host") do
        add(h, "Host", authority(Url { host = u.host, port = u.port }))
    if not has(h, "User-Agent") do
        add(h, "User-Agent", "eclang")
    if not has(h, "Accept") do
        add(h, "Accept", "*/*")
    set(h, "Connection", "close")
    if body.len > 0 or method == "POST" or method == "PUT" or method == "PATCH" do
        set(h, "Content-Length", "${body.len}")
    if u.userInfo.len > 0 and not has(h, "Authorization") do
        val login = encoding.urlUnescape(u.userInfo) ?? u.userInfo
        add(h, "Authorization", concat("Basic ", encoding.base64(login)))
    ret h

/**
 * Reads the body of a response.
 * @throws when it isn't valid, or is larger than allowed
 */
priv fn responseBody(r: @Reading, method: String, status: Int, h: Headers): String throws Error do
    ret "" if method == "HEAD" or status == 204 or status == 304
    val chunked = try isChunked(r, h)
    ret try readChunked(r) if chunked
    if val length = try contentLength(r, h) do
        ret try readSized(r, length)
    // Without either, the body goes on until the connection closes.
    val body = stringBuilder(4096)
    while true do
        val part = try net.read(r.c)
        break if part.len == 0
        throw refuse(r, 413, "this body is larger than ${r.maxBody} bytes") if body.len + part.len > r.maxBody
        body.append(part)
    ret body.build()

/**
 * Reads a response, past any 100 Continue.
 * @throws when it isn't valid
 */
priv fn readResponse(r: @Reading, method: String, url: String): Response throws Error do
    while true do
        r.headSize = 0
        val line = try headLine(r) ?? throw parseError("the server closed the connection without an answer", "", 0)
        throw parseError("this isn't an HTTP response: ${quote(line.take(60))}", line, 0) if not line.startsWith("HTTP/1.") or line.len < 12 or line[8] != ' ' or (line.len > 12 and line[12] != ' ')
        val status = parseInt(line[9..<12]) ?? 0
        throw parseError("this isn't an HTTP status: ${quote(line[9..<12])}", line, 9) if status < 100 or status > 999 or not isDigit(line[9])
        val h = try readHeaders(r)
        if status >= 200 or status == 101 do
            val body = try responseBody(r, method, status as Int, h)
            ret new Response { status as Int, line.drop(13), h, body, url }

/**
 * Makes one request, without following redirects.
 * @throws when it can't be made, or the answer isn't valid
 */
priv fn exchange(method: String, u: Url, body: String, options: Options): Response throws Error do
    throw argumentError("url", "https isn't supported: it needs a TLS library, which a program can't link yet") if u.scheme == "https"
    throw argumentError("url", "this URL should start with http://, and it is: ${text(u)}") if u.scheme != "http" or u.host.len == 0
    throw argumentError("method", "this isn't a method: ${quote(method)}") if not isToken(method)
    throw argumentError("url", "a URL can't have white space or control characters in it") if not isHeaderValue(target(u)) or target(u).contains(' ')
    val head = stringBuilder(256)
    head.append("${method} ${target(u)} HTTP/1.1\r\n")
    try addHeaders(head, requestHeaders(u, method, body, options))
    val c = try net.connect(u.host, if u.port != 0 do u.port else 80, options.timeout)
    defer net.close(c)
    net.setTimeout(c, options.timeout)
    try net.send(c, head.build())
    try net.send(c, body) if body.len > 0
    val r = new Reading { c, options.maxBody }
    ret try readResponse(r, method, text(u))

/** Whether two URLs are of the same server, so that credentials can go from one to the other. */
priv fn sameOrigin(a: Url, b: Url): Bool = a.scheme == b.scheme and a.host == b.host and a.port == b.port

/**
 * Makes a request, following redirects.
 * @param method "GET", "POST", ...
 * @param url where it goes
 * @param body what it sends; "" for nothing
 * @param options how to do it
 * @return the response, whatever its status
 * @throws when the URL isn't http, the server can't be reached, a timeout passes, the answer isn't valid or is larger
 *     than `maxBody`, or there are too many redirects
 */
fn request(method: String, url: String, body: String = "", options: Options = defaults()): Response throws Error do
    var u = try parseUrl(url)
    var verb = method
    var content = body
    var settings = options
    var left = options.redirects
    while true do
        val r = try exchange(verb, u, content, settings)
        val location = get(r.headers, "Location")
        val redirected = r.status == 301 or r.status == 302 or r.status == 303 or r.status == 307 or r.status == 308
        ret r if not redirected or location == null or options.redirects == 0
        throw Error { ERR_FAILED, "too many redirects: the last one is to ${location}" } if left == 0
        left -= 1
        val next = try resolve(u, location)
        if r.status == 303 or ((r.status == 301 or r.status == 302) and verb == "POST") do
            verb = if verb == "HEAD" do verb else "GET"
            content = ""
            if val given = settings.headers do
                val kept = headers()
                kept.fields.pushAll(given.fields.items())
                remove(kept, "Content-Type")
                settings = Options { ...settings, headers = kept }
        if not sameOrigin(u, next) do
            if val given2 = settings.headers do
                val kept2 = headers()
                kept2.fields.pushAll(given2.fields.items())
                remove(kept2, "Authorization")
                remove(kept2, "Cookie")
                remove(kept2, "Host")
                settings = Options { ...settings, headers = kept2 }
        u = next

/**
 * A GET request.
 * @param url where it goes
 * @param options how to do it
 * @return the response, whatever its status
 * @throws when it can't be made; see {@link request}
 */
fn get(url: String, options: Options = defaults()): Response throws Error = try request("GET", url, "", options)

/**
 * A HEAD request, which gets the headers of a GET without its body.
 * @param url where it goes
 * @param options how to do it
 * @return the response, whatever its status
 * @throws when it can't be made; see {@link request}
 */
fn head(url: String, options: Options = defaults()): Response throws Error = try request("HEAD", url, "", options)

/** A copy of `options` with the header `Content-Type`. */
priv fn withType(options: Options, contentType: String): Options do
    val h = headers()
    if val given = options.headers do
        h.fields.pushAll(given.fields.items())
    if not has(h, "Content-Type") do
        add(h, "Content-Type", contentType)
    ret Options { ...options, headers = h }

/**
 * A POST request.
 * @param url where it goes
 * @param body what it sends
 * @param contentType what kind of data that is
 * @param options how to do it
 * @return the response, whatever its status
 * @throws when it can't be made; see {@link request}
 */
fn post(url: String, body: String, contentType: String = "application/octet-stream", options: Options = defaults()): Response throws Error = try request("POST", url, body, withType(options, contentType))

/**
 * A POST request of a form, as a browser sends one.
 * @param url where it goes
 * @param fields the names and values
 * @param options how to do it
 * @return the response, whatever its status
 * @throws when it can't be made; see {@link request}
 */
fn postForm(url: String, fields: (String, String)[], options: Options = defaults()): Response throws Error = try post(url, makeQuery(fields), "application/x-www-form-urlencoded", options)

/**
 * A PUT request.
 * @param url where it goes
 * @param body what it sends
 * @param contentType what kind of data that is
 * @param options how to do it
 * @return the response, whatever its status
 * @throws when it can't be made; see {@link request}
 */
fn put(url: String, body: String, contentType: String = "application/octet-stream", options: Options = defaults()): Response throws Error = try request("PUT", url, body, withType(options, contentType))

/**
 * A DELETE request.
 * @param url where it goes
 * @param options how to do it
 * @return the response, whatever its status
 * @throws when it can't be made; see {@link request}
 */
fn delete(url: String, options: Options = defaults()): Response throws Error = try request("DELETE", url, "", options)

// ---------------------------------------------------------------------------------------------------
// The server

/** How often a waiting worker looks whether it should stop. */
priv const POLL_MILLIS: Long = 50

/** How a server works. */
mut rec ServerOptions as
    /** How many connections are served at the same time; more of them wait. */
    workers: Int = 16
    /** How long a read or write of a request may take, once it has started. */
    timeout: time.Duration = time.seconds(30)
    /** How long a connection may stay open waiting for the next request. */
    idleTimeout: time.Duration = time.seconds(5)
    /** The largest body of a request; a larger one gets the status 413. */
    maxBody: Size = 16777216
    /** What handlers get as `req.context`. */
    context: @Any? = null

/** The {@link ServerOptions} that are used when none are given. */
fn serverDefaults(): ServerOptions = ServerOptions { workers = 16 }

/** What serves requests: a function from a request to a response. */
type Handler = (Request) => Response

/** What serves requests and can fail, which the client sees as the status 500. */
type FailingHandler = (Request) => Response throws Error

/** The handler of a server that hasn't started. */
priv fn unserved(req: Request): Response = text(503, "Service Unavailable\n")

/** The failing handler of a server that hasn't started. */
priv fn unservedFailing(req: Request): Response throws Error = text(503, "Service Unavailable\n")

/** A server: {@link server}. */
mut ref rec Server as
    /** Where it listens. {@code listener.address.port} is the port, also when the system chose it. */
    listener: net.Listener
    /** How it works. */
    options: ServerOptions
    /** Whether workers should go on: 1 or 0. */
    running: @thread.Atomic
    /** Held by the worker that is waiting for a connection. */
    accepting: thread.Mutex
    /** The workers, once it has started. */
    workers: List<thread.Thread>
    /** What serves requests, when {@code canFail} is false. */
    handler: Handler = fn unserved
    /** What serves requests, when {@code canFail} is true. */
    failing: FailingHandler = fn unservedFailing
    /** Which of the two is in use. */
    canFail: Bool = false

/**
 * A server listening on a port, which isn't serving yet: see {@link start} and {@link run}.
 * @param port the port; 0 lets the system choose a free one, which `listener.address.port` tells
 * @param host the address to listen on: "127.0.0.1" for this computer only; all IPv4 addresses by default
 * @param options how it works
 * @return the server, in the current pool, which must outlive it
 * @throws when the port is in use, or needs privileges this program doesn't have
 */
fn server(port: Int, host: String = "0.0.0.0", options: ServerOptions = serverDefaults()): Server throws OsError do
    val l = try net.listen(port, host)
    val running = new thread.Atomic { 0 }
    val workers: List<thread.Thread> = list()
    ret new Server { l, options, running, try thread.mutex(), workers }

/** Whether the workers should go on. */
priv fn isRunning(s: Server): Bool = thread.load(mut @s.running) != 0

/** Runs the handler, in a way that can't fail: a failure becomes the status 500. */
priv fn answer(s: Server, req: Request): Response do
    ret s.handler(req) if not s.canFail
    val res = try s.failing(req) catch e do
        log.error("http: ${req.method} ${req.path}: ${describe(e)}")
        ret text(500, "Internal Server Error\n")
    ret res

/**
 * Reads the request line and what follows it.
 * @return the request, or null when the connection ended first
 * @throws when it isn't valid, or reading fails
 */
priv fn readRequest(s: Server, r: @Reading): Request? throws Error do
    var first = try headLine(r) ?? ret null
    // One empty line before a request is allowed: some clients send one after the body of the last.
    if first.len == 0 do
        first = try headLine(r) ?? ret null
    val parts = first.split(' ')
    throw refuse(r, 400, "this isn't a request line: ${quote(first.take(60))}") if parts.len != 3
    throw refuse(r, 400, "this isn't a method") if not isToken(parts[0])
    throw refuse(r, 505, "this server speaks HTTP/1.1") if parts[2] != "HTTP/1.1" and parts[2] != "HTTP/1.0"
    throw refuse(r, 400, "the target of a request starts with /") if not parts[1].startsWith('/') and not parts[1].startsWith("http://") and parts[1] != "*"
    r.refusal = 400
    val url = try parseUrl(parts[1])
    val path = try encoding.urlUnescape(url.path)
    r.refusal = 0
    val h = try readHeaders(r)
    val hosts = getAll(h, "Host")
    throw refuse(r, 400, "an HTTP/1.1 request has one Host header") if parts[2] == "HTTP/1.1" and hosts.len != 1
    var body = ""
    val chunked = try isChunked(r, h)
    val length = try contentLength(r, h)
    if chunked or length != null do
        throw refuse(r, 413, "this body is larger than ${r.maxBody} bytes") if (length ?? 0) > r.maxBody
        if hasToken(h, "Expect", "100-continue") do
            try net.send(r.c, "HTTP/1.1 100 Continue\r\n\r\n")
    if chunked do
        body = try readChunked(r)
    else do
        body = try readSized(r, length ?? 0)
    // Keep-alive is only for those who can say so.
    if parts[2] == "HTTP/1.0" and not hasToken(h, "Connection", "keep-alive") do
        set(h, "Connection", "close")
    ret new Request { parts[0], url, path, h, body, r.c.remote, s.options.context }

/**
 * Sends a response.
 * @param c the connection
 * @param method the method of the request, if there was one
 * @param res what to send
 * @param keepOpen whether the connection stays open after it
 * @throws when sending fails, or a header would break the message
 */
priv fn sendResponse(c: net.Connection, method: String, res: Response, keepOpen: Bool) throws Error do
    val b = stringBuilder(256 + res.body.len)
    val noBody = res.status == 204 or res.status == 304 or (res.status >= 100 and res.status < 200)
    b.append("HTTP/1.1 ${res.status} ${res.reason}\r\n")
    set(res.headers, "Date", time.rfc822(time.now()))
    if not noBody do
        set(res.headers, "Content-Length", "${res.body.len}")
    if not keepOpen do
        set(res.headers, "Connection", "close")
    try addHeaders(b, res.headers)
    b.append(res.body) if method != "HEAD" and not noBody
    try net.send(c, b.build())

/** Whether the next request has come; false when it didn't in time, or the server is stopping. */
priv fn awaitRequest(s: Server, c: net.Connection): Bool do
    val started = time.instant()
    while isRunning(s) do
        val came = net.waitReadable(c, time.millis(POLL_MILLIS)) ?? ret false
        ret true if came
        ret false if time.elapsed(started).micros > s.options.idleTimeout.micros
    ret false

/** Serves one request, in the current pool, and says whether the connection stays open. */
priv fn serveOne(s: Server, c: net.Connection): Bool do
    val r = new Reading { c, s.options.maxBody }
    val read = try readRequest(s, r) catch e do
        val status = if e.code == ERR_RANGE do 431 else r.refusal
        if status != 0 do
            val message = concat(e.message, "\n")
            try sendResponse(c, "", text(status, message), false) catch e2 do
                log.debug("http: can't send a refusal: ${e2.message}")
        ret false
    val req = read ?? ret false
    val res = answer(s, req)
    val keepOpen = not hasToken(req.headers, "Connection", "close") and not hasToken(res.headers, "Connection", "close") and isRunning(s)
    try sendResponse(c, req.method, res, keepOpen) catch e3 do
        // Most likely the client has gone, or the handler made a header that can't be sent.
        log.warn("http: ${req.method} ${req.path}: ${e3.message}")
        if e3.code == ERR_ARGUMENT do
            try sendResponse(c, req.method, text(500, "Internal Server Error\n"), false) catch e4 do
                log.debug("http: can't send a refusal: ${e4.message}")
        ret false
    ret keepOpen

/** Serves a connection until it closes, with a pool for each request. */
priv fn serveConnection(s: Server, c: net.Connection) do
    net.setTimeout(c, s.options.timeout)
    var open = true
    while open and awaitRequest(s, c) do
        pool do
            open = serveOne(s, c)

/** Waits for a connection for a moment, taking its turn with the other workers; null when none came. */
priv fn acceptNext(s: Server): net.Connection? do
    thread.lock(s.accepting)
    defer thread.unlock(s.accepting)
    ret null if not isRunning(s)
    val c = try net.accept(s.listener, time.millis(POLL_MILLIS)) catch e do
        if e.code != ERR_TIMEOUT do
            // Out of file descriptors, say. There is nothing to do but to try again soon.
            log.warn("http: can't accept a connection: ${e.message}")
            time.sleep(time.millis(POLL_MILLIS))
        ret null
    ret c

/** Waits for a connection for a moment, and serves it. */
priv fn serveNext(s: Server) do
    if val c = acceptNext(s) do
        serveConnection(s, c)
        net.close(c)

/** What each worker runs. Only one at a time waits for a connection, so none of them is stuck when the server stops. */
priv fn work(s: Server) do
    while isRunning(s) do
        pool do
            serveNext(s)

/**
 * Starts the workers.
 * @throws when the system can't start a thread
 */
priv fn startWorkers(s: Server) throws OsError do
    thread.store(mut @s.running, 1)
    // The workers share the Server, which isn't `flat`: they only read it, except for its atomics and what its own locks guard,
    // and it lives until stop has joined them all.
    for i in 0..<s.options.workers do
        s.workers.push(try unsafe thread.spawn(fn work, s))

/**
 * Starts serving requests on other threads, and goes on. {@link stop} and {@link wait} end it.
 * @param s the server
 * @param handler what serves requests, which the workers keep (`escaping`): a lambda that uses the caller's `val`s is fine, its `var`s aren't
 * @throws when the system can't start a thread
 */
fn start(s: Server, escaping handler: Handler) throws OsError do
    s.handler = handler
    try startWorkers(s)

/**
 * Starts serving requests on other threads with a handler that can fail: a failure is logged with core:log.ec, and the
 * client gets the status 500.
 * @param s the server
 * @param handler what serves requests
 * @throws when the system can't start a thread
 */
fn start(s: Server, escaping handler: FailingHandler) throws OsError do
    s.failing = handler
    s.canFail = true
    try startWorkers(s)

/**
 * Tells a server to stop, which a handler can do too. Requests that are being served are finished. {@link wait} waits for that.
 * @param s the server
 */
fn stop(s: Server) do
    thread.store(mut @s.running, 0)

/**
 * Waits until a server has stopped, and closes its port.
 * @throws when a worker can't be joined
 */
fn wait(s: Server) throws OsError do
    for worker in s.workers do
        try thread.join(worker)
    s.workers.clear()
    net.close(s.listener)

/**
 * Serves requests until {@link stop} is called.
 * @param s the server
 * @param handler what serves requests
 * @throws when the system can't start a thread
 */
fn run(s: Server, handler: Handler) throws OsError do
    try start(s, handler)
    try wait(s)

/**
 * Serves requests until {@link stop} is called, with a handler that can fail: see {@link start}.
 * @param s the server
 * @param handler what serves requests
 * @throws when the system can't start a thread
 */
fn run(s: Server, handler: FailingHandler) throws OsError do
    try start(s, handler)
    try wait(s)
