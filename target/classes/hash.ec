/**
 * Checksums and hashes: CRC32, FNV-1a, MD5, SHA-1, SHA-256 and HMAC.
 *
 *     use "core:hash.ec"
 *     io.log(hash.sha256("hello"))                         // 2cf24dba5fb0a30e26e83b2ac5b9e29e...
 *     io.log(hash.crc32("hello"))                          // 907060870
 *     io.log(hash.hmac(:sha256, "key", "message"))         // 6e9ef29b75fffc5b7abae527d58fdadb...
 *     io.log(try hash.file("big.iso", :sha256))            // reads it a part at a time
 *
 *     val h = hash.hasher(:sha1)                           // data that arrives in parts
 *     hash.update(h, "hel")
 *     hash.update(h, "lo")
 *     io.log(hash.hex(h))
 *
 * MD5 and SHA-1 are broken for security: use them to match a format or a checksum someone else publishes, and SHA-256
 * for anything new. None of this is a password hash, and the code isn't written to resist timing attacks on the machine
 * it runs on. MD5, SHA-1 and SHA-256 are Brad Conte's public domain C code (core:vendor/digest.c); APR has no public
 * hashes. What is returned is allocated in the current pool.
 */
module "hash"
use "core:errors.ec"
use "core:string.ec"
use "core:math.ec"
use "core:encoding.ec"
use "core:io.ec"
use "core:vendor/digest.ec"

// ---------------------------------------------------------------------------------------------------
// Checksums

/** The CRC32 of each value of half a byte (the polynomial 0xEDB88320). */
priv val CRC_TABLE: UInt[] = [0x00000000u, 0x1DB71064u, 0x3B6E20C8u, 0x26D930ACu, 0x76DC4190u, 0x6B6B51F4u, 0x4DB26158u, 0x5005713Cu, 0xEDB88320u, 0xF00F9344u, 0xD6D6A3E8u, 0xCB61B38Cu, 0x9B64C2B0u, 0x86D3D2D4u, 0xA00AE278u, 0xBDBDF21Cu]

/**
 * The CRC32 of `s`, as in zip, gzip and PNG: `crc32("hello")` is 907060870.
 * @param previous the CRC32 of what came before, to go on from it; 0 for the start
 */
fn crc32(s: String, previous: UInt = 0): UInt do
    var crc = ~previous
    for c in s do
        val byte = (c as UChar) as UInt
        crc = (crc >> 4) ^ CRC_TABLE[((crc ^ byte) & 15) as Size]
        crc = (crc >> 4) ^ CRC_TABLE[((crc ^ (byte >> 4)) & 15) as Size]
    ret ~crc

/** The 32-bit FNV-1a hash of `s`: quick and well spread, for hash tables. Not for security. */
fn fnv32(s: String): UInt do
    var h: ULong = 2166136261
    for c in s do
        h = ((h ^ ((c as UChar) as ULong)) * 16777619) & 0xFFFFFFFFul
    ret h as UInt

/** The 64-bit FNV-1a hash of `s`: quick and well spread, for hash tables. Not for security. */
fn fnv64(s: String): ULong do
    var h: ULong = (0xCBF29CE4ul << 32) | 0x84222325ul
    for c in s do
        h = math.wrappingMul(h ^ ((c as UChar) as ULong), 1099511628211ul)
    ret h

// ---------------------------------------------------------------------------------------------------
// Digests

/**
 * A hash function:
 *
 * - `:md5`: 16 bytes. Broken for security
 * - `:sha1`: 20 bytes. Broken for security
 * - `:sha256`: 32 bytes
 */
declare type Algorithm as :md5 | :sha1 | :sha256

/** What core:vendor/digest.c calls `algorithm`. */
priv fn idOf(algorithm: Algorithm): Int do
    when algorithm do
        :md5 do ret 0
        :sha1 do ret 1
        :sha256 do ret 2

/** How many bytes a digest of `algorithm` has. */
fn size(algorithm: Algorithm): Size do
    when algorithm do
        :md5 do ret 16
        :sha1 do ret 20
        :sha256 do ret 32

/** Hashes data that arrives in parts, from {@link hasher}. It can give one digest. */
rec Hasher as
    /** The hash function. */
    algorithm: Algorithm
    /** The state of the C code. */
    state: @Any

/** A new {@link Hasher}, in the current pool. */
fn hasher(algorithm: Algorithm = :sha256): Hasher do
    val state = new Char[digestSize()]
    val data = state.data !as @Any
    digestInit(data, idOf(algorithm))
    ret Hasher { algorithm, data }

/** Adds `s` to what `h` has hashed. */
fn update(h: Hasher, s: String) do
    digestUpdate(h.state, idOf(h.algorithm), s.data !as CString, s.len)

/** The digest of what `h` was given, as raw bytes. `h` can't be used after this. */
fn finish(h: Hasher): String do
    val out = new Char[size(h.algorithm)]
    digestFinal(h.state, idOf(h.algorithm), out.data !as CString)
    ret out !as String

/** The digest of what `h` was given, in lower-case hexadecimal. `h` can't be used after this. */
fn hex(h: Hasher): String = encoding.hex(finish(h))

/** The digest of `s` as raw bytes; see {@link md5}, {@link sha1} and {@link sha256} for hexadecimal. */
fn digest(algorithm: Algorithm, s: String): String do
    val h = hasher(algorithm)
    update(h, s)
    ret finish(h)

/** The MD5 of `s` in hexadecimal. Broken for security; see {@link Algorithm}. */
fn md5(s: String): String = encoding.hex(digest(:md5, s))

/** The SHA-1 of `s` in hexadecimal. Broken for security; see {@link Algorithm}. */
fn sha1(s: String): String = encoding.hex(digest(:sha1, s))

/** The SHA-256 of `s` in hexadecimal. */
fn sha256(s: String): String = encoding.hex(digest(:sha256, s))

/**
 * The digest of the file at `path` in hexadecimal. The file is read a part at a time, so it can be any size.
 * @throws when the file can't be opened or read
 */
fn file(path: String, algorithm: Algorithm = :sha256): String throws IoError do
    val f = try io.open(path, :read)
    defer io.close(f)
    val h = hasher(algorithm)
    val buffer = new Char[65536]
    while true do
        var got = buffer.len
        val status = apr_file_read(f.handle, buffer.data, mut got)
        break if APR_STATUS_IS_EOF(status) != 0
        throw ioError(status, path) if status != APR_SUCCESS
        update(h, buffer[0..<got] !as String)
    ret hex(h)

// ---------------------------------------------------------------------------------------------------
// HMAC

/** The block size of all three functions, in bytes: what HMAC pads its key to. */
priv const BLOCK: Size = 64

/** The HMAC (RFC 2104) of `message` as raw bytes. See {@link hmac}. */
fn hmacDigest(algorithm: Algorithm, key: String, message: String): String do
    val k = if key.len > BLOCK do digest(algorithm, key) else key
    val inner = new Char[BLOCK]
    val outer = new Char[BLOCK]
    for i in 0..<BLOCK do
        val byte = if i < k.len do (k[i] as UChar) else (0 as UChar)
        inner[i] = (byte ^ (0x36 as UChar)) as Char
        outer[i] = (byte ^ (0x5C as UChar)) as Char
    val h = hasher(algorithm)
    update(h, inner !as String)
    update(h, message)
    val first = finish(h)
    val o = hasher(algorithm)
    update(o, outer !as String)
    update(o, first)
    ret finish(o)

/**
 * The HMAC (RFC 2104) of `message` in hexadecimal: a signature that only those who know `key` can make. Compare
 * signatures with {@link equal}, not `==`.
 * @param algorithm the hash function
 * @param key the secret
 * @param message what to sign
 */
fn hmac(algorithm: Algorithm, key: String, message: String): String = encoding.hex(hmacDigest(algorithm, key, message))

/**
 * Whether two digests are the same, in a time that doesn't depend on where they differ: `==` stops at the first
 * difference, which tells an attacker who can measure it how much of a signature was right.
 */
fn equal(a: String, b: String): Bool do
    ret false if a.len != b.len
    var difference: UChar = 0
    for i in 0..<a.len do
        difference = difference | ((a[i] as UChar) ^ (b[i] as UChar))
    ret difference == 0
