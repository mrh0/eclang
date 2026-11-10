# Module `uuid`

UUIDs (RFC 9562): random ones (version 4), ones that sort by when they were made (version 7), and their text.

    use "core:uuid.ec"
    val id = try uuid.v4()                              // 0b5c9a3e-6f1d-4c8a-9d2e-7a41b3c5e6f8
    val key = try uuid.v7()                             // starts with the time: later ones sort after it
    io.log("${id}")                                     // as text, lower case with hyphens
    val same = try uuid.parse(uuid.text(id).toUpper())  // either case, with or without hyphens
    io.log(same == id)                                  // true: `==`, `hash` and Sets work on a Uuid
    io.log(uuid.version(key))                           // 7
    if val made = uuid.timeOf(key) do                   // when it was made, to the millisecond
        io.log(time.unixMillis(made))

Both kinds take their random bits from the system's secure random source (`math.secureRandom`), so they
can't be guessed. A v4 is the one for a session id or a link to share; a v7 tells when it was made. Two v7 UUIDs made
in the same millisecond are in no particular order. A Uuid is two numbers, not memory in a pool.

## Types

### <a id="uuid.Uuid"></a>`Uuid`

```ec
rec Uuid
```

A UUID: 128 bits, as two halves read with the first byte in the highest bits.

**Fields**

- <a id="uuid.Uuid.high"></a>`high: ULong` — The first 8 bytes (the time of a v7, and the version).
- <a id="uuid.Uuid.low"></a>`low: ULong` — The last 8 bytes (the variant, and random bits).

## Values

### <a id="uuid.zero"></a>`zero`

```ec
val zero = Uuid { 0, 0 }
```

The nil UUID, all zero: 00000000-0000-0000-0000-000000000000.

## Functions

### <a id="uuid.v4"></a>`v4`

```ec
fn v4(): Uuid throws OsError
```

A new random UUID (version 4): 122 random bits.

**Throws**

- `OsError` — when the system's random source can't be read

### <a id="uuid.v7"></a>`v7`

```ec
fn v7(): Uuid throws OsError
```

A new UUID that starts with the time (version 7): the milliseconds since 1970 in its first 48 bits, then 74 random
bits. Sorted by their text or with [`compare`](#uuid.compare), they are in the order they were made (to the millisecond), which
keeps a database index on them in order.

**Throws**

- `OsError` — when the system's random source can't be read

### `v7`

```ec
fn v7(at: time.Time): Uuid throws OsError
```

A UUID of version 7 for the moment `at`, with fresh random bits. A moment before 1970 counts as 1970.

**Throws**

- `OsError` — when the system's random source can't be read

### <a id="uuid.version"></a>`version`

```ec
fn version(u: Uuid): Int
```

The version of `u`: 4 for [`v4`](#uuid.v4), 7 for [`v7`](#uuid.v7), 0 for [`zero`](#uuid.zero).

### <a id="uuid.isZero"></a>`isZero`

```ec
fn isZero(u: Uuid): Bool
```

Whether `u` is the nil UUID, all zero.

### <a id="uuid.timeOf"></a>`timeOf`

```ec
fn timeOf(u: Uuid): time.Time?
```

When a version 7 UUID was made, to the millisecond.

**Returns** the moment, null for a UUID of another version

### <a id="uuid.compare"></a>`compare`

```ec
fn compare(a: Uuid, b: Uuid): Int
```

-1, 0 or 1 as `a` sorts before, the same as or after `b`: the order of their bytes, and of their text.

### <a id="uuid.bytes"></a>`bytes`

```ec
fn bytes(u: Uuid): UChar[]
```

The 16 bytes of `u`, the first byte first, in the current pool.

### <a id="uuid.fromBytes"></a>`fromBytes`

```ec
fn fromBytes(b: UChar[]): Uuid throws ArgumentError
```

The Uuid of 16 bytes, as [`bytes`](#uuid.bytes) gives them.

**Throws**

- `ArgumentError` — when there aren't exactly 16

### <a id="uuid.text"></a>`text`

```ec
fn text(u: Uuid): String
```

`u` as text: 36 lower-case characters in groups of 8, 4, 4, 4 and 12 hexadecimal digits, joined by hyphens.

### <a id="uuid.str"></a>`str`

```ec
fn str(u: Uuid): String
```

`${u}` writes [`text`](#uuid.text).

### <a id="uuid.parse"></a>`parse`

```ec
fn parse(s: String): Uuid throws ParseError
```

The Uuid that `s` writes: 32 hexadecimal digits in either case, with hyphens between the groups as [`text`](#uuid.text)
writes them or with none at all. Braces around it ("{...}") and a "urn:uuid:" in front are allowed too.

**Throws**

- `ParseError` — when `s` isn't a UUID; the error says where

### <a id="uuid.isValid"></a>`isValid`

```ec
fn isValid(s: String): Bool
```

Whether `s` is a UUID that [`parse`](#uuid.parse) would accept.
