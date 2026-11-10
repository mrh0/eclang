# Module `http`

HTTP/1.1: parsing URLs, a client, and a small server. Built on core:net.ec.

    use "core:http.ec"
    val r = try http.get("http://example.com/")
    io.log(r.status)                                    // 200
    io.log(http.get(r.headers, "Content-Type") ?? "?")  // text/html; charset=UTF-8
    io.log(r.body)

    val sent = try http.post("http://example.com/api", "{\"id\": 1}", "application/json")
    val slow = try http.get(url, http.Options { timeout = time.seconds(60), redirects = 0 })

    val u = try http.parseUrl("http://ada@example.com:8080/a/b?x=1&y=2#top")
    io.log(u.host)                                      // example.com
    io.log(http.param(u, "y") ?? "?")                   // 2
    io.log(http.text(try http.resolve(u, "../c")))      // http://ada@example.com:8080/c

    fn hello(req: http.Request): http.Response do
        ret http.text(404, "nothing here") if req.path != "/"
        val name = http.param(req.url, "name") ?? "world"
        ret http.text(200, "hello ${name}\n")

    val server = try http.server(8080)
    try http.run(server, fn hello)                      // serves until http.stop(server)

There is no HTTPS: that needs a TLS library, which a program can't link yet, so an `https:` URL is an error that says
so. For the same reason of size, there are no cookies, no compression, and the client opens a new connection for each
request. A redirect is followed for GET and HEAD, and for the others as browsers do. Both sides read `chunked` bodies.
Everything is read into memory; the limits on it are `maxBody` in [`Options`](#http.Options) and [`ServerOptions`](#http.ServerOptions).

The server keeps a fixed number of threads, each serving one connection at a time, so handlers run at the same time
and whatever they share needs a lock (core:thread.ec). Each request has its own pool, freed after its response is
sent. A handler can be a lambda that uses the variables around it (or reach the program's data through `req.context`). A panic in a
handler ends the program. The server is strict about what it reads, and limits the size of a request, but it is small:
in front of the open Internet, put it behind a reverse proxy.

Failures of the network are OsErrors, and answers that aren't valid HTTP are ParseErrors; both are Errors. A status
such as 404 isn't a failure: it is in [`Response`](#http.Response). What is returned is allocated in the current pool.

## Types

### <a id="http.Url"></a>`Url`

```ec
mut rec Url
```

The parts of a URL, as in `http://user@host:8080/path?query#fragment`. Only `host` is changed, to lower case; the rest
is as written, with its %XX escapes.

**Fields**

- <a id="http.Url.scheme"></a>`scheme: String = ""` — What comes before the ":", in lower case; "" in a relative URL.
- <a id="http.Url.userInfo"></a>`userInfo: String = ""` — What comes before the "@"; "" for nothing.
- <a id="http.Url.host"></a>`host: String = ""` — The name or IP address, without the [ ] of IPv6, in lower case; "" when it has none.
- <a id="http.Url.port"></a>`port: Int = 0` — The port; 0 when it doesn't say.
- <a id="http.Url.path"></a>`path: String = ""` — The path; it can be "".
- <a id="http.Url.query"></a>`query: String = ""` — What comes after the "?", without it; "" for nothing.
- <a id="http.Url.fragment"></a>`fragment: String = ""` — What comes after the "#", without it; "" for nothing.

### <a id="http.Header"></a>`Header`

```ec
rec Header
```

A field of the head of a request or response.

**Fields**

- <a id="http.Header.name"></a>`name: String` — Its name, as it was written; names are the same in any case.
- <a id="http.Header.value"></a>`value: String` — Its value.

### <a id="http.Headers"></a>`Headers`

```ec
ref rec Headers
```

The headers of a request or response, in their order: [`headers`](#http.headers). A name can be there several times.

**Fields**

- <a id="http.Headers.fields"></a>`fields: List<Header>` — The fields.

### <a id="http.Request"></a>`Request`

```ec
ref rec Request
```

A request: what [`Handler`](#http.Handler)s get.

**Fields**

- <a id="http.Request.method"></a>`method: String` — "GET", "POST", ...
- <a id="http.Request.url"></a>`url: Url` — The URL as it was in the request: usually only a path and a query.
- <a id="http.Request.path"></a>`path: String` — The path, without its %XX escapes.
- <a id="http.Request.headers"></a>`headers: Headers` — The headers.
- <a id="http.Request.body"></a>`body: String` — The body, and "" for none.
- <a id="http.Request.remote"></a>`remote: net.Address` — Who sent it.
- <a id="http.Request.context"></a>`context: @Any?` — What was given in [`ServerOptions`](#http.ServerOptions), to reach the data of the program.

### <a id="http.Response"></a>`Response`

```ec
ref rec Response
```

A response, from a server to a client.

**Fields**

- <a id="http.Response.status"></a>`status: Int` — The status: 200, 404, ...
- <a id="http.Response.reason"></a>`reason: String` — The text after the status: "OK", "Not Found", ...
- <a id="http.Response.headers"></a>`headers: Headers` — The headers.
- <a id="http.Response.body"></a>`body: String` — The body, and "" for none.
- <a id="http.Response.url"></a>`url: String = ""` — Where it came from, after any redirects; "" on the server.

### <a id="http.Options"></a>`Options`

```ec
rec Options
```

How a client makes a request: `http.Options { timeout = time.seconds(5) }`.

**Fields**

- <a id="http.Options.timeout"></a>`timeout: time.Duration? = time.seconds(30)` — How long connecting, and each read and write, may take; null for as long as it takes.
- <a id="http.Options.redirects"></a>`redirects: Int = 5` — How many redirects to follow; 0 gives the redirect itself as the response.
- <a id="http.Options.headers"></a>`headers: Headers? = null` — Headers to send, next to those that are always sent. They take precedence over those.
- <a id="http.Options.maxBody"></a>`maxBody: Size = 1073741824` — The largest body to read.

### <a id="http.ServerOptions"></a>`ServerOptions`

```ec
mut rec ServerOptions
```

How a server works.

**Fields**

- <a id="http.ServerOptions.workers"></a>`workers: Int = 16` — How many connections are served at the same time; more of them wait.
- <a id="http.ServerOptions.timeout"></a>`timeout: time.Duration = time.seconds(30)` — How long a read or write of a request may take, once it has started.
- <a id="http.ServerOptions.idleTimeout"></a>`idleTimeout: time.Duration = time.seconds(5)` — How long a connection may stay open waiting for the next request.
- <a id="http.ServerOptions.maxBody"></a>`maxBody: Size = 16777216` — The largest body of a request; a larger one gets the status 413.
- <a id="http.ServerOptions.context"></a>`context: @Any? = null` — What handlers get as `req.context`.

### <a id="http.Handler"></a>`Handler`

```ec
type Handler = (Request) => Response
```

What serves requests: a function from a request to a response.

### <a id="http.FailingHandler"></a>`FailingHandler`

```ec
type FailingHandler = (Request) => Response throws Error
```

What serves requests and can fail, which the client sees as the status 500.

### <a id="http.Server"></a>`Server`

```ec
mut ref rec Server
```

A server: [`server`](#http.server).

**Fields**

- <a id="http.Server.listener"></a>`listener: net.Listener` — Where it listens. `listener.address.port` is the port, also when the system chose it.
- <a id="http.Server.options"></a>`options: ServerOptions` — How it works.
- <a id="http.Server.running"></a>`running: @thread.Atomic` — Whether workers should go on: 1 or 0.
- <a id="http.Server.accepting"></a>`accepting: thread.Mutex` — Held by the worker that is waiting for a connection.
- <a id="http.Server.workers"></a>`workers: List<thread.Thread>` — The workers, once it has started.
- <a id="http.Server.handler"></a>`handler: Handler = fn unserved` — What serves requests, when `canFail` is false.
- <a id="http.Server.failing"></a>`failing: FailingHandler = fn unservedFailing` — What serves requests, when `canFail` is true.
- <a id="http.Server.canFail"></a>`canFail: Bool = false` — Which of the two is in use.

## Functions

### <a id="http.parseUrl"></a>`parseUrl`

```ec
fn parseUrl(text: String): Url throws ParseError
```

Splits a URL into its parts, without looking any of them up. A URL can be relative: "/a/b?x=1", "../c".

**Throws**

- `ParseError` — when it has white space or control characters in it, or its port isn't valid

### <a id="http.text"></a>`text`

```ec
fn text(u: Url): String
```

`u` as text.

### <a id="http.target"></a>`target`

```ec
fn target(u: Url): String
```

The path and query of `u`, as they go in a request: "/a/b?x=1", and "/" for no path.

### <a id="http.resolve"></a>`resolve`

```ec
fn resolve(base: Url, reference: String): Url throws ParseError
```

The URL that `reference` means on a page at `base`, as RFC 3986 says: `resolve(u, "../c")`. A reference that is a full
URL is that URL.

**Throws**

- `ParseError` — when `reference` isn't a valid URL

### <a id="http.parseQuery"></a>`parseQuery`

```ec
fn parseQuery(query: String): (String, String)[] throws ParseError
```

The `name=value` pairs of a query or form: `parseQuery("a=1&b=x+y")` gives ("a", "1") and ("b", "x y").

**Returns** the pairs, in their order, without their escapes; a part without "=" has "" as its value

**Throws**

- `ParseError` — when an escape isn't valid

### <a id="http.makeQuery"></a>`makeQuery`

```ec
fn makeQuery(pairs: (String, String)[]): String
```

The query or form of the pairs, with escapes: `makeQuery([("a", "x y")])` is "a=x%20y".

### <a id="http.param"></a>`param`

```ec
fn param(u: Url, name: String): String?
```

The first value called `name` in the query of `u`; null when it has none, or the query isn't valid.

### <a id="http.headers"></a>`headers`

```ec
fn headers(): Headers
```

New empty headers, in the current pool.

### <a id="http.add"></a>`add`

```ec
fn add(h: Headers, name: String, value: String)
```

Adds a header, next to others of that name.

### <a id="http.remove"></a>`remove`

```ec
fn remove(h: Headers, name: String)
```

Removes all the headers called `name`.

### <a id="http.set"></a>`set`

```ec
fn set(h: Headers, name: String, value: String)
```

Sets the header `name`, in place of those of that name.

### <a id="http.get"></a>`get`

```ec
fn get(h: Headers, name: String): String?
```

The first header called `name`, in any case, or null when there is none.

### <a id="http.getAll"></a>`getAll`

```ec
fn getAll(h: Headers, name: String): String[]
```

All the headers called `name`, in any case.

### <a id="http.has"></a>`has`

```ec
fn has(h: Headers, name: String): Bool
```

Whether there is a header called `name`, in any case.

### <a id="http.statusText"></a>`statusText`

```ec
fn statusText(status: Int): String
```

The usual text of a status, or "" for one that has none.

### <a id="http.response"></a>`response`

```ec
fn response(status: Int, body: String, contentType: String): Response
```

A response with this body and `Content-Type`.

### `text`

```ec
fn text(status: Int, body: String): Response
```

A response of plain text.

### <a id="http.html"></a>`html`

```ec
fn html(status: Int, body: String): Response
```

A response of HTML.

### <a id="http.json"></a>`json`

```ec
fn json(status: Int, body: String): Response
```

A response of JSON, which `body` must be: `http.json(200, json.stringify(value))`.

### <a id="http.redirect"></a>`redirect`

```ec
fn redirect(location: String, status: Int = 302): Response
```

A response that sends the client to `location`, with the status 302, or the one given.

### <a id="http.defaults"></a>`defaults`

```ec
fn defaults(): Options
```

The [`Options`](#http.Options) that are used when none are given.

### <a id="http.request"></a>`request`

```ec
fn request(method: String, url: String, body: String = "", options: Options = defaults()): Response throws Error
```

Makes a request, following redirects.

**Parameters**

- `method` — "GET", "POST", ...
- `url` — where it goes
- `body` — what it sends; "" for nothing
- `options` — how to do it

**Returns** the response, whatever its status

**Throws**

- `Error` — when the URL isn't http, the server can't be reached, a timeout passes, the answer isn't valid or is larger
  than `maxBody`, or there are too many redirects

### `get`

```ec
fn get(url: String, options: Options = defaults()): Response throws Error
```

A GET request.

**Parameters**

- `url` — where it goes
- `options` — how to do it

**Returns** the response, whatever its status

**Throws**

- `Error` — when it can't be made; see [`request`](#http.request)

### <a id="http.head"></a>`head`

```ec
fn head(url: String, options: Options = defaults()): Response throws Error
```

A HEAD request, which gets the headers of a GET without its body.

**Parameters**

- `url` — where it goes
- `options` — how to do it

**Returns** the response, whatever its status

**Throws**

- `Error` — when it can't be made; see [`request`](#http.request)

### <a id="http.post"></a>`post`

```ec
fn post(url: String, body: String, contentType: String = "application/octet-stream", options: Options = defaults()): Response throws Error
```

A POST request.

**Parameters**

- `url` — where it goes
- `body` — what it sends
- `contentType` — what kind of data that is
- `options` — how to do it

**Returns** the response, whatever its status

**Throws**

- `Error` — when it can't be made; see [`request`](#http.request)

### <a id="http.postForm"></a>`postForm`

```ec
fn postForm(url: String, fields: (String, String)[], options: Options = defaults()): Response throws Error
```

A POST request of a form, as a browser sends one.

**Parameters**

- `url` — where it goes
- `fields` — the names and values
- `options` — how to do it

**Returns** the response, whatever its status

**Throws**

- `Error` — when it can't be made; see [`request`](#http.request)

### <a id="http.put"></a>`put`

```ec
fn put(url: String, body: String, contentType: String = "application/octet-stream", options: Options = defaults()): Response throws Error
```

A PUT request.

**Parameters**

- `url` — where it goes
- `body` — what it sends
- `contentType` — what kind of data that is
- `options` — how to do it

**Returns** the response, whatever its status

**Throws**

- `Error` — when it can't be made; see [`request`](#http.request)

### <a id="http.delete"></a>`delete`

```ec
fn delete(url: String, options: Options = defaults()): Response throws Error
```

A DELETE request.

**Parameters**

- `url` — where it goes
- `options` — how to do it

**Returns** the response, whatever its status

**Throws**

- `Error` — when it can't be made; see [`request`](#http.request)

### <a id="http.serverDefaults"></a>`serverDefaults`

```ec
fn serverDefaults(): ServerOptions
```

The [`ServerOptions`](#http.ServerOptions) that are used when none are given.

### <a id="http.server"></a>`server`

```ec
fn server(port: Int, host: String = "0.0.0.0", options: ServerOptions = serverDefaults()): Server throws OsError
```

A server listening on a port, which isn't serving yet: see [`start`](#http.start) and [`run`](#http.run).

**Parameters**

- `port` — the port; 0 lets the system choose a free one, which `listener.address.port` tells
- `host` — the address to listen on: "127.0.0.1" for this computer only; all IPv4 addresses by default
- `options` — how it works

**Returns** the server, in the current pool, which must outlive it

**Throws**

- `OsError` — when the port is in use, or needs privileges this program doesn't have

### <a id="http.start"></a>`start`

```ec
fn start(s: Server, escaping handler: Handler) throws OsError
```

Starts serving requests on other threads, and goes on. [`stop`](#http.stop) and [`wait`](#http.wait) end it.

**Parameters**

- `s` — the server
- `handler` — what serves requests, which the workers keep (`escaping`): a lambda that uses the caller's `val`s is fine, its `var`s aren't

**Throws**

- `OsError` — when the system can't start a thread

### `start`

```ec
fn start(s: Server, escaping handler: FailingHandler) throws OsError
```

Starts serving requests on other threads with a handler that can fail: a failure is logged with core:log.ec, and the
client gets the status 500.

**Parameters**

- `s` — the server
- `handler` — what serves requests

**Throws**

- `OsError` — when the system can't start a thread

### <a id="http.stop"></a>`stop`

```ec
fn stop(s: Server)
```

Tells a server to stop, which a handler can do too. Requests that are being served are finished. [`wait`](#http.wait) waits for that.

**Parameters**

- `s` — the server

### <a id="http.wait"></a>`wait`

```ec
fn wait(s: Server) throws OsError
```

Waits until a server has stopped, and closes its port.

**Throws**

- `OsError` — when a worker can't be joined

### <a id="http.run"></a>`run`

```ec
fn run(s: Server, handler: Handler) throws OsError
```

Serves requests until [`stop`](#http.stop) is called.

**Parameters**

- `s` — the server
- `handler` — what serves requests

**Throws**

- `OsError` — when the system can't start a thread

### `run`

```ec
fn run(s: Server, handler: FailingHandler) throws OsError
```

Serves requests until [`stop`](#http.stop) is called, with a handler that can fail: see [`start`](#http.start).

**Parameters**

- `s` — the server
- `handler` — what serves requests

**Throws**

- `OsError` — when the system can't start a thread
