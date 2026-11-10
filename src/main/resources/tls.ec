/**
 * TLS (the "S" of HTTPS) through OpenSSL: encrypted connections that check who is at the other end. Using this file
 * makes {@link net.secure} work, and `https:` URLs in core:http.ec.
 *
 *     use "core:net.ec"
 *     use "core:tls.ec"
 *     val c = try net.connect("example.com", 443, time.seconds(5))
 *     net.setTimeout(c, time.seconds(10))
 *     try net.secure(c, "example.com")                    // the handshake; checks the certificate and the name
 *     try net.send(c, "GET / HTTP/1.1\r\nHost: example.com\r\nConnection: close\r\n\r\n")
 *     io.log(try net.readLine(c) ?? "")                   // HTTP/1.1 200 OK
 *     io.log(tls.protocol(c) ?? "")                       // TLSv1.3
 *
 *     val creds = try tls.server("cert.pem", "key.pem")   // a server's certificate chain and private key, in PEM
 *     val client = try net.accept(listener)
 *     try net.secure(client, creds)
 *
 *     val server = try http.server(8443, options = http.ServerOptions { tls = creds })
 *
 * A client trusts the certificates of the system (/etc/ssl), checks that the server's is valid and for the name it
 * connected to, and speaks TLS 1.2 or 1.3. {@link net.TlsOptions} can name other certificates to trust (a test server's
 * own), or turn the checks off for a test. Reads and writes keep the connection's timeout. A server that closes the
 * connection without saying goodbye in TLS (as many HTTP servers do) ends the reading as if it had.
 *
 * It links `-lssl -lcrypto`, which needs OpenSSL 3 and its headers: the package libssl-dev (Debian, Ubuntu),
 * openssl-devel (Fedora) or openssl (Homebrew; with C_INCLUDE_PATH and LIBRARY_PATH set). A TLS session belongs to the
 * pool of its connection, and is freed with it.
 */
module "tls"
use "core:errors.ec"
use "core:string.ec"
use "core:net.ec"
use "core:apr/apr.ec"
use "core:apr/errno.ec"
use "core:apr/pools.ec"
use "core:apr/network_io.ec"
use "core:apr/thread_mutex.ec"
use "core:openssl/ssl.ec"

// ---------------------------------------------------------------------------------------------------
// Sessions

/** The TLS state of one connection, which its BIO and its {@link net.Transport} are given. */
priv mut ref rec Session as
    /** The OpenSSL session. */
    ssl: @SSL
    /** The socket it reads and writes, through APR, so that the connection's timeout holds. */
    socket: @apr_socket_t
    /** The pool of the connection, for what the session keeps. */
    mem: Pool
    /** What APR said last time the BIO read or wrote. */
    status: apr_status_t = 0
    /** What went wrong last, in words. */
    problem: String = ""
    /** Whether the session has been ended. */
    closed: Bool = false

/** The BIO type that reads and writes an APR socket, made when the program starts. */
priv var socketMethod: @BIO_METHOD? = null

/** The client context with the system's certificates, made on first use. */
priv var defaultClient: @SSL_CTX? = null

/** Guards {@code defaultClient}. */
priv var clientLock: @apr_thread_mutex_t? = null

/** The OpenSSL errors waiting in this thread, in words, which takes them away. */
priv fn openSslErrors(): String do
    val b = stringBuilder(128)
    val text = new Char[256]
    var code = ERR_get_error()
    while code != 0 do
        b.append("; ") if b.len > 0
        ERR_error_string_n(code, text.data !as CString, text.len)
        b.append(fromC(text.data !as CString))
        code = ERR_get_error()
    ret b.build()

/** An OsError for something OpenSSL failed to do. */
priv fn tlsError(what: String): OsError do
    val detail = openSslErrors()
    ret OsError { ERR_FAILED, if detail.len > 0 do "${what}: ${detail}" else what, APR_EGENERAL }

/** The BIO's read: apr_socket_recv, with the connection's timeout. */
priv fn bioRead(b: @BIO, buffer: CString, size: Size, readbytes: @Size): Int do
    val s = BIO_get_data(b) !as Session
    BIO_clear_flags(b, BIO_FLAGS_READ | BIO_FLAGS_WRITE | BIO_FLAGS_SHOULD_RETRY)
    var length = size
    val status = apr_socket_recv(s.socket, buffer, mut length)
    s.status = status
    @readbytes = length
    ret 1 if length > 0
    if APR_STATUS_IS_EAGAIN(status) != 0 do
        BIO_set_flags(b, BIO_FLAGS_READ | BIO_FLAGS_SHOULD_RETRY)
    ret 0

/** The BIO's write: apr_socket_send, with the connection's timeout. */
priv fn bioWrite(b: @BIO, buffer: CString, size: Size, written: @Size): Int do
    val s = BIO_get_data(b) !as Session
    BIO_clear_flags(b, BIO_FLAGS_READ | BIO_FLAGS_WRITE | BIO_FLAGS_SHOULD_RETRY)
    var length = size
    val status = apr_socket_send(s.socket, buffer, mut length)
    s.status = status
    @written = length
    ret 1 if length > 0
    if APR_STATUS_IS_EAGAIN(status) != 0 do
        BIO_set_flags(b, BIO_FLAGS_WRITE | BIO_FLAGS_SHOULD_RETRY)
    ret 0

/** The BIO's other requests: only a flush is answered, as there is nothing to flush. */
priv fn bioCtrl(b: @BIO, cmd: Int, num: Long, ptr: @Any?): Long = if cmd == BIO_CTRL_FLUSH do 1 else 0

/** The APR status of an OpenSSL call that returned `result`, noting what went wrong in the session. */
priv fn outcome(s: Session, result: Int): apr_status_t do
    val error = SSL_get_error(s.ssl, result)
    ret APR_EOF if error == SSL_ERROR_ZERO_RETURN
    ret APR_EAGAIN if error == SSL_ERROR_WANT_READ or error == SSL_ERROR_WANT_WRITE
    if error == SSL_ERROR_SYSCALL do
        // The socket failed (a timeout, a reset), or ended in the middle of a message.
        ret s.status if s.status != APR_SUCCESS and APR_STATUS_IS_EOF(s.status) == 0
        s.problem = "the connection ended in the middle of the TLS handshake or a message"
        ret APR_EOF
    val verified = SSL_get_verify_result(s.ssl)
    if verified != X509_V_OK do
        s.problem = format(s.mem, "the certificate isn't valid: ${fromC(X509_verify_cert_error_string(verified))}")
        openSslErrors()
    else do
        val detail = openSslErrors()
        s.problem = clone(if detail.len > 0 do detail else "TLS failed", s.mem)
    ret APR_EGENERAL

/** {@link net.Transport}'s recv. */
priv fn sessionRecv(state: @Any, buffer: CString, length: @Size): apr_status_t do
    val s = state !as Session
    ERR_clear_error()
    var got: Size = 0
    val ok = SSL_read_ex(s.ssl, buffer !as @Any, @length, mut got)
    @length = got
    ret APR_SUCCESS if ok == 1
    ret outcome(s, ok)

/** {@link net.Transport}'s send. */
priv fn sessionSend(state: @Any, buffer: CString, length: @Size): apr_status_t do
    val s = state !as Session
    ERR_clear_error()
    var sent: Size = 0
    val ok = SSL_write_ex(s.ssl, buffer !as @Any, @length, mut sent)
    @length = sent
    ret APR_SUCCESS if ok == 1
    ret outcome(s, ok)

/** {@link net.Transport}'s pending. */
priv fn sessionPending(state: @Any): Size do
    val n = SSL_pending((state !as Session).ssl)
    ret if n > 0 do n as Size else 0

/** {@link net.Transport}'s handshake. */
priv fn sessionHandshake(state: @Any): apr_status_t do
    val s = state !as Session
    ERR_clear_error()
    val ok = SSL_do_handshake(s.ssl)
    ret APR_SUCCESS if ok == 1
    ret outcome(s, ok)

/** {@link net.Transport}'s close: says goodbye, without waiting for the peer's. */
priv fn sessionClose(state: @Any) do
    val s = state !as Session
    ret if s.closed
    s.closed = true
    ERR_clear_error()
    SSL_shutdown(s.ssl)
    ERR_clear_error()

/** {@link net.Transport}'s problem. */
priv fn sessionProblem(state: @Any): String = (state !as Session).problem

/** Frees a session when the pool of its connection is freed. */
priv fn freeSession(data: @Any): apr_status_t do
    SSL_free((data !as Session).ssl)
    ret APR_SUCCESS

/** Frees a context when its pool is freed. */
priv fn freeContext(data: @Any): apr_status_t do
    SSL_CTX_free(data !as @SSL_CTX)
    ret APR_SUCCESS

/**
 * Gives `c` a session of `ssl`, which reads and writes its socket.
 * @throws when OpenSSL can't make what it needs
 */
priv fn attach(c: net.Connection, ssl: @SSL) throws OsError do
    val method = socketMethod!
    val bio = BIO_new(method) ?? throw tlsError("make a TLS session")
    val s = new Session { ssl, c.socket, c.mem } in c.mem
    BIO_set_data(bio, s !as @Any)
    BIO_set_init(bio, 1)
    SSL_set_bio(ssl, bio, bio)
    apr_pool_cleanup_register(c.mem, s !as @Any, fn freeSession, fn apr_pool_cleanup_null)
    c.transport = net.Transport { s !as @Any, fn sessionRecv, fn sessionSend, fn sessionPending, fn sessionHandshake, fn sessionClose, fn sessionProblem }

/** A context for clients or servers that speaks TLS 1.2 and 1.3. */
priv fn context(client: Bool): @SSL_CTX throws OsError do
    val ctx = SSL_CTX_new(if client do TLS_client_method() else TLS_server_method()) ?? throw tlsError("make a TLS context")
    SSL_CTX_ctrl(ctx, SSL_CTRL_SET_MIN_PROTO_VERSION, TLS1_2_VERSION as Long, null)
    SSL_CTX_set_options(ctx, SSL_OP_IGNORE_UNEXPECTED_EOF)
    ret ctx

/**
 * A client context for `options`: one with the system's certificates is made once and shared.
 * @return the context, and whether it is the shared one (the others are the caller's to free)
 */
priv fn clientContext(options: net.TlsOptions): (@SSL_CTX, Bool) throws OsError do
    if options.verify and options.caFile == null do
        apr_thread_mutex_lock(clientLock!)
        defer apr_thread_mutex_unlock(clientLock!)
        if val shared = defaultClient do
            ret (shared, true)
        val ctx = try context(true)
        if SSL_CTX_set_default_verify_paths(ctx) != 1 do
            val error = tlsError("load the system's certificates")
            SSL_CTX_free(ctx)
            throw error
        SSL_CTX_set_verify(ctx, SSL_VERIFY_PEER, null)
        defaultClient = ctx
        ret (ctx, true)
    val ctx = try context(true)
    if val file = options.caFile do
        if SSL_CTX_load_verify_locations(ctx, cstr(file), null) != 1 do
            val error = tlsError("load the certificates in '${file}'")
            SSL_CTX_free(ctx)
            throw error
    SSL_CTX_set_verify(ctx, if options.verify do SSL_VERIFY_PEER else SSL_VERIFY_NONE, null)
    ret (ctx, false)

/** Whether `host` is an IP address rather than a name. */
priv fn isIp(host: String): Bool do
    ret true if host.contains(':')
    for ch in host do
        ret false if not isDigit(ch) and ch != '.'
    ret host.len > 0

/** {@code net.tlsClient}: a client session for `c`, to `host`. */
priv fn startClient(c: net.Connection, host: String, options: net.TlsOptions) throws OsError do
    val (ctx, shared) = try clientContext(options)
    val made = SSL_new(ctx)
    // A session holds its own reference to the context.
    SSL_CTX_free(ctx) if not shared
    val ssl = made ?? throw tlsError("make a TLS session")
    if isIp(host) do
        X509_VERIFY_PARAM_set1_ip_asc(SSL_get0_param(ssl), cstr(host)) if options.verify
    else do
        // The name goes to the server (SNI), which may serve several, and the certificate must be for it.
        SSL_ctrl(ssl, SSL_CTRL_SET_TLSEXT_HOSTNAME, TLSEXT_NAMETYPE_host_name as Long, cstr(host) !as @Any)
        SSL_set1_host(ssl, cstr(host)) if options.verify
    try attach(c, ssl) catch e do
        SSL_free(ssl)
        throw e
    SSL_set_connect_state(ssl)

/** {@code net.TlsServer.begin}: a server session for an accepted connection. */
priv fn startServer(context: @Any, c: net.Connection) throws OsError do
    val ssl = SSL_new(context !as @SSL_CTX) ?? throw tlsError("make a TLS session")
    try attach(c, ssl) catch e do
        SSL_free(ssl)
        throw e
    SSL_set_accept_state(ssl)

/** Sets up OpenSSL for net, when the program starts. */
priv fn install(): Bool do
    val method = BIO_meth_new(BIO_get_new_index() | BIO_TYPE_SOURCE_SINK, "eclang APR socket"c) ?? ret false
    BIO_meth_set_write_ex(method, fn bioWrite)
    BIO_meth_set_read_ex(method, fn bioRead)
    BIO_meth_set_ctrl(method, fn bioCtrl)
    socketMethod = method
    var lock: @apr_thread_mutex_t? = null
    ret false if apr_thread_mutex_create(mut lock, APR_THREAD_MUTEX_DEFAULT as UInt, pool) != APR_SUCCESS
    clientLock = lock
    // The global startClient sets (the shared context) is OpenSSL's memory, not a pool's, and what it keeps of the
    // connection is in the connection's own pool.
    net.tlsClient = unsafe fn startClient
    ret true

/** Whether {@link install} worked; net.secure says so when it didn't. */
priv val installed = install()

// ---------------------------------------------------------------------------------------------------
// Servers and connections

/**
 * The certificate and key of a server, for {@link net.secure} and core:http.ec's `ServerOptions.tls`.
 * @param certFile the certificate in PEM, followed by the certificates that lead to a trusted one (a "full chain")
 * @param keyFile its private key, in PEM, not encrypted
 * @return the server's credentials, in the current pool, which must outlive the connections that use them
 * @throws when a file can't be read, or the key isn't the certificate's
 */
fn server(certFile: String, keyFile: String): net.TlsServer throws OsError do
    val ctx = try context(false)
    apr_pool_cleanup_register(pool, ctx !as @Any, fn freeContext, fn apr_pool_cleanup_null)
    throw tlsError("load the certificate chain in '${certFile}'") if SSL_CTX_use_certificate_chain_file(ctx, cstr(certFile)) != 1
    throw tlsError("load the private key in '${keyFile}'") if SSL_CTX_use_PrivateKey_file(ctx, cstr(keyFile), SSL_FILETYPE_PEM) != 1
    throw tlsError("the key in '${keyFile}' isn't the one of the certificate in '${certFile}'") if SSL_CTX_check_private_key(ctx) != 1
    ret net.TlsServer { ctx !as @Any, fn startServer }

/** The TLS version a secure connection speaks ("TLSv1.3"), or null for a connection without TLS. */
fn protocol(c: net.Connection): String? do
    if val t = c.transport do
        ret fromC(SSL_get_version((t.state !as Session).ssl))
    ret null
