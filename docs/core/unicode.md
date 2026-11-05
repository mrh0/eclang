# Module `unicode`

Unicode text in UTF-8: going through code points, encoding and decoding them, validating, and repairing broken text.

    use "core:unicode.ec"
    for cp in unicode.codePoints("héllo") do            // 104, 233, 108, 108, 111
        io.log(cp)
    for at, cp in unicode.codePoints("héllo") do        // at is the byte where cp starts: 0, 1, 3, 4, 5
        io.log(at)

    io.log(unicode.length("héllo"))                     // 5, where "héllo".len is 6 bytes
    io.log(unicode.slice("héllo", 1, 3))                // él
    io.log(unicode.encode(0x1F600))                     // 😀
    val (cp, n) = unicode.decode("é!", 0)               // 233 and 2, its length in bytes
    val shown = unicode.sanitize(fromTheNetwork)        // broken bytes become U+FFFD
    val at = unicode.firstInvalid(text) ?? 0            // where the text stops being UTF-8

A String is bytes, and nothing checks that they are UTF-8. Reading never fails here: each byte that isn't part of a
valid sequence counts as one U+FFFD, the replacement character, so a broken text can still be gone through. What
UTF-8 doesn't allow is broken too: overlong forms, surrogates, and anything above U+10FFFF.

A code point isn't always what a reader calls a character: "é" can be written as "e" and a combining mark, and a flag
is two code points. There are no tables of the Unicode character database here, so there is no case mapping,
normalisation or categories; [`width`](#unicode.width) is an estimate from ranges. What is made is allocated in the current pool.

## Types

### <a id="unicode.CodePoints"></a>`CodePoints`

```ec
rec CodePoints
```

Goes through the code points of a String: [`codePoints`](#unicode.codePoints).

**Fields**

- <a id="unicode.CodePoints.text"></a>`text: String` — What is gone through.
- <a id="unicode.CodePoints.at"></a>`at: Size` — The byte where the next code point starts.

### <a id="unicode.CodePointsAt"></a>`CodePointsAt`

```ec
rec CodePointsAt
```

What `for at, cp in unicode.codePoints(s)` goes through.

**Fields**

- <a id="unicode.CodePointsAt.points"></a>`points: CodePoints` — The code points.

## Values

### <a id="unicode.REPLACEMENT"></a>`REPLACEMENT`

```ec
const REPLACEMENT: UInt = 0xFFFD
```

U+FFFD, which stands for what can't be read.

### <a id="unicode.MAX"></a>`MAX`

```ec
const MAX: UInt = 0x10FFFF
```

U+10FFFF, the highest code point.

## Functions

### <a id="unicode.isScalar"></a>`isScalar`

```ec
fn isScalar(cp: UInt): Bool
```

Whether `cp` is a Unicode scalar value, which is what UTF-8 can hold: anything up to U+10FFFF but a surrogate.

### <a id="unicode.isSurrogate"></a>`isSurrogate`

```ec
fn isSurrogate(cp: UInt): Bool
```

Whether `cp` is half of a UTF-16 surrogate pair, from U+D800 to U+DFFF.

### <a id="unicode.isHighSurrogate"></a>`isHighSurrogate`

```ec
fn isHighSurrogate(cp: UInt): Bool
```

Whether `cp` is the first half of a UTF-16 surrogate pair.

### <a id="unicode.isLowSurrogate"></a>`isLowSurrogate`

```ec
fn isLowSurrogate(cp: UInt): Bool
```

Whether `cp` is the second half of a UTF-16 surrogate pair.

### <a id="unicode.joinSurrogates"></a>`joinSurrogates`

```ec
fn joinSurrogates(high: UInt, low: UInt): UInt
```

The code point of a UTF-16 surrogate pair, which must be one: `joinSurrogates(0xD83D, 0xDE00)` is 0x1F600.

### <a id="unicode.splitSurrogates"></a>`splitSurrogates`

```ec
fn splitSurrogates(cp: UInt): (UInt, UInt)
```

The UTF-16 surrogate pair of `cp`, which must be above U+FFFF: `splitSurrogates(0x1F600)` is (0xD83D, 0xDE00).

### <a id="unicode.isSpace"></a>`isSpace`

```ec
fn isSpace(cp: UInt): Bool
```

Whether `cp` is white space, as Unicode's White_Space property says.

### <a id="unicode.width"></a>`width`

```ec
fn width(cp: UInt): Int
```

How many columns a code point takes on a terminal: 2 for the wide ones (Chinese, Japanese, Korean, most emoji), 0 for
combining marks and control characters, 1 for the rest. Like C's wcwidth, without depending on the locale.

### `width`

```ec
fn width(s: String): Size
```

How many columns `s` takes on a terminal, which is often not `s.len`; see [`width`](#unicode.width).

### <a id="unicode.decode"></a>`decode`

```ec
fn decode(s: String, i: Size): (UInt, Size)
```

The code point that starts at byte `i` of `s`, which must be less than `s.len`.

**Returns** the code point and its length in bytes; [`REPLACEMENT`](#unicode.REPLACEMENT) and 1 for a byte that doesn't start a valid sequence

### <a id="unicode.decodeBefore"></a>`decodeBefore`

```ec
fn decodeBefore(s: String, i: Size): (UInt, Size)
```

The code point that ends just before byte `i` of `s`, for going backwards. `i` must not be 0.

**Returns** the code point and its length in bytes; [`REPLACEMENT`](#unicode.REPLACEMENT) and 1 for a byte that doesn't end a valid sequence

### <a id="unicode.codePoints"></a>`codePoints`

```ec
fn codePoints(s: String): CodePoints
```

The code points of `s`, for `for`. `for cp in unicode.codePoints(s)` gives each one, and
`for at, cp in unicode.codePoints(s)` also the byte where it starts. Bytes that aren't valid UTF-8 give [`REPLACEMENT`](#unicode.REPLACEMENT).

### <a id="unicode.next"></a>`next`

```ec
fn next(c: @CodePoints): UInt?
```

The next code point, or null at the end.

### <a id="unicode.pairs"></a>`pairs`

```ec
fn pairs(c: CodePoints): CodePointsAt
```

Makes `for at, cp in` give the byte where each code point starts.

### `next`

```ec
fn next(c: @CodePointsAt): (Size, UInt)?
```

The byte where the next code point starts, and the code point; null at the end.

### <a id="unicode.toArray"></a>`toArray`

```ec
fn toArray(s: String): UInt[]
```

All the code points of `s`, as an array.

### <a id="unicode.encodedLength"></a>`encodedLength`

```ec
fn encodedLength(cp: UInt): Size
```

How many bytes UTF-8 needs for `cp`. What can't be encoded takes the 3 bytes of [`REPLACEMENT`](#unicode.REPLACEMENT).

### <a id="unicode.appendCodePoint"></a>`appendCodePoint`

```ec
fn appendCodePoint(b: @StringBuilder, cp: UInt)
```

Adds `cp` to `b` as UTF-8. What isn't a scalar value ([`isScalar`](#unicode.isScalar)) adds [`REPLACEMENT`](#unicode.REPLACEMENT).

### <a id="unicode.encode"></a>`encode`

```ec
fn encode(cp: UInt): String
```

`cp` as UTF-8: `encode(0xE9)` is "é". What isn't a scalar value ([`isScalar`](#unicode.isScalar)) gives [`REPLACEMENT`](#unicode.REPLACEMENT).

### `encode`

```ec
fn encode(cps: UInt[]): String
```

The code points as UTF-8.

### <a id="unicode.isValid"></a>`isValid`

```ec
fn isValid(s: String): Bool
```

Whether `s` is valid UTF-8: no broken sequences, overlong forms or surrogates.

### <a id="unicode.isAscii"></a>`isAscii`

```ec
fn isAscii(s: String): Bool
```

Whether `s` has only ASCII characters, which are the same in UTF-8.

### <a id="unicode.firstInvalid"></a>`firstInvalid`

```ec
fn firstInvalid(s: String): Size?
```

The byte where `s` stops being valid UTF-8, or null when all of it is.

### <a id="unicode.validate"></a>`validate`

```ec
fn validate(s: String): String throws ParseError
```

Checks that `s` is valid UTF-8, for text from outside the program.

**Returns** `s`

**Throws**

- `ParseError` — when it isn't, with the position of the first broken byte

### <a id="unicode.sanitize"></a>`sanitize`

```ec
fn sanitize(s: String): String
```

`s` with each byte that isn't valid UTF-8 replaced by [`REPLACEMENT`](#unicode.REPLACEMENT). It is `s` itself when all of it is valid.

### <a id="unicode.length"></a>`length`

```ec
fn length(s: String): Size
```

The number of code points in `s`, where `s.len` counts bytes: `length("héllo")` is 5.

### <a id="unicode.byteOffset"></a>`byteOffset`

```ec
fn byteOffset(s: String, n: Size): Size?
```

The byte where code point `n` of `s` starts.

**Returns** the byte; `s.len` for `n` just past the last one; null when `s` has fewer code points

### <a id="unicode.slice"></a>`slice`

```ec
fn slice(s: String, from: Size, until: Size): String
```

The code points of `s` from `from` up to, but not including, `until`: `slice("héllo", 1, 3)` is "él". It shares the memory
of `s`. Beyond the last code point, it stops at the end.

### <a id="unicode.truncate"></a>`truncate`

```ec
fn truncate(s: String, maxBytes: Size): String
```

At most `maxBytes` bytes of `s`, without cutting a code point in half. It shares the memory of `s`.
