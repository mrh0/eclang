# Module `hash`

Checksums and hashes: CRC32, FNV-1a, MD5, SHA-1, SHA-256 and HMAC.

    use "core:hash.ec"
    io.log(hash.sha256("hello"))                         // 2cf24dba5fb0a30e26e83b2ac5b9e29e...
    io.log(hash.crc32("hello"))                          // 907060870
    io.log(hash.hmac(:sha256, "key", "message"))         // 6e9ef29b75fffc5b7abae527d58fdadb...
    io.log(try hash.file("big.iso", :sha256))            // reads it a part at a time

    val h = hash.hasher(:sha1)                           // data that arrives in parts
    hash.update(h, "hel")
    hash.update(h, "lo")
    io.log(hash.hex(h))

MD5 and SHA-1 are broken for security: use them to match a format or a checksum someone else publishes, and SHA-256
for anything new. None of this is a password hash, and the code isn't written to resist timing attacks on the machine
it runs on. MD5, SHA-1 and SHA-256 are Brad Conte's public domain C code (core:vendor/digest.c); APR has no public
hashes. What is returned is allocated in the current pool.

## Types

### <a id="hash.Algorithm"></a>`Algorithm`

```ec
declare type Algorithm as :md5 | :sha1 | :sha256
```

A hash function:

- `:md5`: 16 bytes. Broken for security
- `:sha1`: 20 bytes. Broken for security
- `:sha256`: 32 bytes

### <a id="hash.Hasher"></a>`Hasher`

```ec
rec Hasher
```

Hashes data that arrives in parts, from [`hasher`](#hash.hasher). It can give one digest.

**Fields**

- <a id="hash.Hasher.algorithm"></a>`algorithm: Algorithm` — The hash function.
- <a id="hash.Hasher.state"></a>`state: @Any` — The state of the C code.

## Functions

### <a id="hash.crc32"></a>`crc32`

```ec
fn crc32(s: String, previous: UInt = 0): UInt
```

The CRC32 of `s`, as in zip, gzip and PNG: `crc32("hello")` is 907060870.

**Parameters**

- `previous` — the CRC32 of what came before, to go on from it; 0 for the start

### <a id="hash.fnv32"></a>`fnv32`

```ec
fn fnv32(s: String): UInt
```

The 32-bit FNV-1a hash of `s`: quick and well spread, for hash tables. Not for security.

### <a id="hash.fnv64"></a>`fnv64`

```ec
fn fnv64(s: String): ULong
```

The 64-bit FNV-1a hash of `s`: quick and well spread, for hash tables. Not for security.

### <a id="hash.size"></a>`size`

```ec
fn size(algorithm: Algorithm): Size
```

How many bytes a digest of `algorithm` has.

### <a id="hash.hasher"></a>`hasher`

```ec
fn hasher(algorithm: Algorithm = :sha256): Hasher
```

A new [`Hasher`](#hash.Hasher), in the current pool.

### <a id="hash.update"></a>`update`

```ec
fn update(h: Hasher, s: String)
```

Adds `s` to what `h` has hashed.

### <a id="hash.finish"></a>`finish`

```ec
fn finish(h: Hasher): String
```

The digest of what `h` was given, as raw bytes. `h` can't be used after this.

### <a id="hash.hex"></a>`hex`

```ec
fn hex(h: Hasher): String
```

The digest of what `h` was given, in lower-case hexadecimal. `h` can't be used after this.

### <a id="hash.digest"></a>`digest`

```ec
fn digest(algorithm: Algorithm, s: String): String
```

The digest of `s` as raw bytes; see [`md5`](#hash.md5), [`sha1`](#hash.sha1) and [`sha256`](#hash.sha256) for hexadecimal.

### <a id="hash.md5"></a>`md5`

```ec
fn md5(s: String): String
```

The MD5 of `s` in hexadecimal. Broken for security; see [`Algorithm`](#hash.Algorithm).

### <a id="hash.sha1"></a>`sha1`

```ec
fn sha1(s: String): String
```

The SHA-1 of `s` in hexadecimal. Broken for security; see [`Algorithm`](#hash.Algorithm).

### <a id="hash.sha256"></a>`sha256`

```ec
fn sha256(s: String): String
```

The SHA-256 of `s` in hexadecimal.

### <a id="hash.file"></a>`file`

```ec
fn file(path: String, algorithm: Algorithm = :sha256): String throws IoError
```

The digest of the file at `path` in hexadecimal. The file is read a part at a time, so it can be any size.

**Throws**

- `IoError` — when the file can't be opened or read

### <a id="hash.hmacDigest"></a>`hmacDigest`

```ec
fn hmacDigest(algorithm: Algorithm, key: String, message: String): String
```

The HMAC (RFC 2104) of `message` as raw bytes. See [`hmac`](#hash.hmac).

### <a id="hash.hmac"></a>`hmac`

```ec
fn hmac(algorithm: Algorithm, key: String, message: String): String
```

The HMAC (RFC 2104) of `message` in hexadecimal: a signature that only those who know `key` can make. Compare
signatures with [`equal`](#hash.equal), not `==`.

**Parameters**

- `algorithm` — the hash function
- `key` — the secret
- `message` — what to sign

### <a id="hash.equal"></a>`equal`

```ec
fn equal(a: String, b: String): Bool
```

Whether two digests are the same, in a time that doesn't depend on where they differ: `==` stops at the first
difference, which tells an attacker who can measure it how much of a signature was right.
