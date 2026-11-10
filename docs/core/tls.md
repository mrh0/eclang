# Module `tls`

TLS (the "S" of HTTPS) through OpenSSL: encrypted connections that check who is at the other end. Using this file
makes `net.secure` work, and `https:` URLs in core:http.ec.

    use "core:net.ec"
    use "core:tls.ec"
    val c = try net.connect("example.com", 443, time.seconds(5))
    net.setTimeout(c, time.seconds(10))
    try net.secure(c, "example.com")                    // the handshake; checks the certificate and the name
    try net.send(c, "GET / HTTP/1.1\r\nHost: example.com\r\nConnection: close\r\n\r\n")
    io.log(try net.readLine(c) ?? "")                   // HTTP/1.1 200 OK
    io.log(tls.protocol(c) ?? "")                       // TLSv1.3

    val creds = try tls.server("cert.pem", "key.pem")   // a server's certificate chain and private key, in PEM
    val client = try net.accept(listener)
    try net.secure(client, creds)

    val server = try http.server(8443, options = http.ServerOptions { tls = creds })

A client trusts the certificates of the system (/etc/ssl), checks that the server's is valid and for the name it
connected to, and speaks TLS 1.2 or 1.3. `net.TlsOptions` can name other certificates to trust (a test server's
own), or turn the checks off for a test. Reads and writes keep the connection's timeout. A server that closes the
connection without saying goodbye in TLS (as many HTTP servers do) ends the reading as if it had.

It links `-lssl -lcrypto`, which needs OpenSSL 3 and its headers: the package libssl-dev (Debian, Ubuntu),
openssl-devel (Fedora) or openssl (Homebrew; with C_INCLUDE_PATH and LIBRARY_PATH set). A TLS session belongs to the
pool of its connection, and is freed with it.

## Functions

### <a id="tls.server"></a>`server`

```ec
fn server(certFile: String, keyFile: String): net.TlsServer throws OsError
```

The certificate and key of a server, for `net.secure` and core:http.ec's `ServerOptions.tls`.

**Parameters**

- `certFile` — the certificate in PEM, followed by the certificates that lead to a trusted one (a "full chain")
- `keyFile` — its private key, in PEM, not encrypted

**Returns** the server's credentials, in the current pool, which must outlive the connections that use them

**Throws**

- `OsError` — when a file can't be read, or the key isn't the certificate's

### <a id="tls.protocol"></a>`protocol`

```ec
fn protocol(c: net.Connection): String?
```

The TLS version a secure connection speaks ("TLSv1.3"), or null for a connection without TLS.
