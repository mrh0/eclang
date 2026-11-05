/**
 * Unicode text in UTF-8: going through code points, encoding and decoding them, validating, and repairing broken text.
 *
 *     use "core:unicode.ec"
 *     for cp in unicode.codePoints("héllo") do            // 104, 233, 108, 108, 111
 *         io.log(cp)
 *     for at, cp in unicode.codePoints("héllo") do        // at is the byte where cp starts: 0, 1, 3, 4, 5
 *         io.log(at)
 *
 *     io.log(unicode.length("héllo"))                     // 5, where "héllo".len is 6 bytes
 *     io.log(unicode.slice("héllo", 1, 3))                // él
 *     io.log(unicode.encode(0x1F600))                     // 😀
 *     val (cp, n) = unicode.decode("é!", 0)               // 233 and 2, its length in bytes
 *     val shown = unicode.sanitize(fromTheNetwork)        // broken bytes become U+FFFD
 *     val at = unicode.firstInvalid(text) ?? 0            // where the text stops being UTF-8
 *
 * A String is bytes, and nothing checks that they are UTF-8. Reading never fails here: each byte that isn't part of a
 * valid sequence counts as one U+FFFD, the replacement character, so a broken text can still be gone through. What
 * UTF-8 doesn't allow is broken too: overlong forms, surrogates, and anything above U+10FFFF.
 *
 * A code point isn't always what a reader calls a character: "é" can be written as "e" and a combining mark, and a flag
 * is two code points. There are no tables of the Unicode character database here, so there is no case mapping,
 * normalisation or categories; {@link width} is an estimate from ranges. What is made is allocated in the current pool.
 */
module "unicode"
use "core:errors.ec"
use "core:string.ec"

/** U+FFFD, which stands for what can't be read. */
const REPLACEMENT: UInt = 0xFFFD
/** U+10FFFF, the highest code point. */
const MAX: UInt = 0x10FFFF

// ---------------------------------------------------------------------------------------------------
// Code points

/** Whether `cp` is a Unicode scalar value, which is what UTF-8 can hold: anything up to U+10FFFF but a surrogate. */
fn isScalar(cp: UInt): Bool = cp <= MAX and not isSurrogate(cp)

/** Whether `cp` is half of a UTF-16 surrogate pair, from U+D800 to U+DFFF. */
fn isSurrogate(cp: UInt): Bool = cp >= 0xD800 and cp <= 0xDFFF

/** Whether `cp` is the first half of a UTF-16 surrogate pair. */
fn isHighSurrogate(cp: UInt): Bool = cp >= 0xD800 and cp <= 0xDBFF

/** Whether `cp` is the second half of a UTF-16 surrogate pair. */
fn isLowSurrogate(cp: UInt): Bool = cp >= 0xDC00 and cp <= 0xDFFF

/** The code point of a UTF-16 surrogate pair, which must be one: `joinSurrogates(0xD83D, 0xDE00)` is 0x1F600. */
fn joinSurrogates(high: UInt, low: UInt): UInt = 0x10000 + ((high - 0xD800) << 10) + (low - 0xDC00)

/** The UTF-16 surrogate pair of `cp`, which must be above U+FFFF: `splitSurrogates(0x1F600)` is (0xD83D, 0xDE00). */
fn splitSurrogates(cp: UInt): (UInt, UInt) = (0xD800 + ((cp - 0x10000) >> 10), 0xDC00 + ((cp - 0x10000) & 0x3FF))

/** Whether `cp` is white space, as Unicode's White_Space property says. */
fn isSpace(cp: UInt): Bool do
    ret (cp >= 9 and cp <= 13) or cp == 32 or cp == 0x85 or cp == 0xA0 or cp == 0x1680 or (cp >= 0x2000 and cp <= 0x200A) or cp == 0x2028 or cp == 0x2029 or cp == 0x202F or cp == 0x205F or cp == 0x3000

/**
 * How many columns a code point takes on a terminal: 2 for the wide ones (Chinese, Japanese, Korean, most emoji), 0 for
 * combining marks and control characters, 1 for the rest. Like C's wcwidth, without depending on the locale.
 */
fn width(cp: UInt): Int do
    ret 0 if cp < 0x20 or (cp >= 0x7F and cp < 0xA0)
    ret 1 if cp < 0x300
    ret 0 if cp < 0x370 or (cp >= 0x200B and cp <= 0x200F) or (cp >= 0xFE00 and cp <= 0xFE0F) or (cp >= 0x20D0 and cp <= 0x20FF)
    ret 2 if cp >= 0x1100 and cp <= 0x115F
    ret 2 if cp >= 0x2E80 and cp <= 0xA4CF and cp != 0x303F
    ret 2 if (cp >= 0xAC00 and cp <= 0xD7A3) or (cp >= 0xF900 and cp <= 0xFAFF) or (cp >= 0xFE30 and cp <= 0xFE6F)
    ret 2 if (cp >= 0xFF00 and cp <= 0xFF60) or (cp >= 0xFFE0 and cp <= 0xFFE6)
    ret 2 if (cp >= 0x1F300 and cp <= 0x1F64F) or (cp >= 0x1F900 and cp <= 0x1F9FF) or (cp >= 0x20000 and cp <= 0x3FFFD)
    ret 1

/** How many columns `s` takes on a terminal, which is often not `s.len`; see {@link width}. */
fn width(s: String): Size do
    var total: Size = 0
    for cp in codePoints(s) do
        total += width(cp) as Size
    ret total

// ---------------------------------------------------------------------------------------------------
// Decoding

/**
 * The code point that starts at byte `i` of `s`, which must be less than `s.len`.
 * @return the code point and its length in bytes; {@link REPLACEMENT} and 1 for a byte that doesn't start a valid sequence
 */
fn decode(s: String, i: Size): (UInt, Size) do
    val c = (s[i] as UChar) as UInt
    ret (c, 1) if c < 0x80
    var n: Size = 0
    var cp: UInt = 0
    // The second byte is what tells overlong forms, surrogates and what is above U+10FFFF from the rest.
    var low: UInt = 0x80
    var high: UInt = 0xBF
    if c >= 0xC2 and c <= 0xDF do
        n = 2
        cp = c & 0x1F
    else if c >= 0xE0 and c <= 0xEF do
        n = 3
        cp = c & 0x0F
        low = if c == 0xE0 do 0xA0 else 0x80
        high = if c == 0xED do 0x9F else 0xBF
    else if c >= 0xF0 and c <= 0xF4 do
        n = 4
        cp = c & 0x07
        low = if c == 0xF0 do 0x90 else 0x80
        high = if c == 0xF4 do 0x8F else 0xBF
    ret (REPLACEMENT, 1) if n == 0 or i + n > s.len
    for k in 1..<n do
        val next = (s[i + k] as UChar) as UInt
        ret (REPLACEMENT, 1) if next < low or next > high
        cp = (cp << 6) | (next & 0x3F)
        low = 0x80
        high = 0xBF
    ret (cp, n)

/**
 * The code point that ends just before byte `i` of `s`, for going backwards. `i` must not be 0.
 * @return the code point and its length in bytes; {@link REPLACEMENT} and 1 for a byte that doesn't end a valid sequence
 */
fn decodeBefore(s: String, i: Size): (UInt, Size) do
    var n: Size = 1
    while n <= 4 and n <= i do
        val c = (s[i - n] as UChar) as UInt
        if c & 0xC0 != 0x80 do
            val (cp, length) = decode(s, i - n)
            ret (cp, length) if length == n
            ret (REPLACEMENT, 1)
        n += 1
    ret (REPLACEMENT, 1)

/** Goes through the code points of a String: {@link codePoints}. */
rec CodePoints as
    /** What is gone through. */
    text: String
    /** The byte where the next code point starts. */
    at: Size

/** What `for at, cp in unicode.codePoints(s)` goes through. */
rec CodePointsAt as
    /** The code points. */
    points: CodePoints

/**
 * The code points of `s`, for `for`. `for cp in unicode.codePoints(s)` gives each one, and
 * `for at, cp in unicode.codePoints(s)` also the byte where it starts. Bytes that aren't valid UTF-8 give {@link REPLACEMENT}.
 */
fn codePoints(s: String): CodePoints = CodePoints { s, 0 }

/** The next code point, or null at the end. */
fn next(c: @CodePoints): UInt? do
    ret null if c.at >= c.text.len
    val (cp, n) = decode(c.text, c.at)
    c.at += n
    ret cp

/** Makes `for at, cp in` give the byte where each code point starts. */
fn pairs(c: CodePoints): CodePointsAt = CodePointsAt { c }

/** The byte where the next code point starts, and the code point; null at the end. */
fn next(c: @CodePointsAt): (Size, UInt)? do
    ret null if c.points.at >= c.points.text.len
    val at = c.points.at
    val (cp, n) = decode(c.points.text, at)
    c.points.at += n
    ret (at, cp)

/** All the code points of `s`, as an array. */
fn toArray(s: String): UInt[] do
    val out = new UInt[length(s)]
    var at: Size = 0
    var i: Size = 0
    while at < s.len do
        val (cp, n) = decode(s, at)
        out[i] = cp
        i += 1
        at += n
    ret out

// ---------------------------------------------------------------------------------------------------
// Encoding

/** How many bytes UTF-8 needs for `cp`. What can't be encoded takes the 3 bytes of {@link REPLACEMENT}. */
fn encodedLength(cp: UInt): Size do
    ret 1 if cp < 0x80
    ret 2 if cp < 0x800
    ret 3 if cp < 0x10000 or not isScalar(cp)
    ret 4

/** Adds `cp` to `b` as UTF-8. What isn't a scalar value ({@link isScalar}) adds {@link REPLACEMENT}. */
fn appendCodePoint(b: @StringBuilder, cp: UInt) do
    val c = if isScalar(cp) do cp else REPLACEMENT
    if c < 0x80 do
        b.append(c as Char)
    else if c < 0x800 do
        b.append((0xC0 | (c >> 6)) as Char)
        b.append((0x80 | (c & 0x3F)) as Char)
    else if c < 0x10000 do
        b.append((0xE0 | (c >> 12)) as Char)
        b.append((0x80 | ((c >> 6) & 0x3F)) as Char)
        b.append((0x80 | (c & 0x3F)) as Char)
    else do
        b.append((0xF0 | (c >> 18)) as Char)
        b.append((0x80 | ((c >> 12) & 0x3F)) as Char)
        b.append((0x80 | ((c >> 6) & 0x3F)) as Char)
        b.append((0x80 | (c & 0x3F)) as Char)

/** `cp` as UTF-8: `encode(0xE9)` is "é". What isn't a scalar value ({@link isScalar}) gives {@link REPLACEMENT}. */
fn encode(cp: UInt): String do
    val b = stringBuilder(4)
    appendCodePoint(b, cp)
    ret b.build()

/** The code points as UTF-8. */
fn encode(cps: UInt[]): String do
    val b = stringBuilder(cps.len + 1)
    for cp in cps do
        appendCodePoint(b, cp)
    ret b.build()

// ---------------------------------------------------------------------------------------------------
// Validating

/** Whether `s` is valid UTF-8: no broken sequences, overlong forms or surrogates. */
fn isValid(s: String): Bool = firstInvalid(s) == null

/** Whether `s` has only ASCII characters, which are the same in UTF-8. */
fn isAscii(s: String): Bool do
    for c in s where c < 0 do
        ret false
    ret true

/** The byte where `s` stops being valid UTF-8, or null when all of it is. */
fn firstInvalid(s: String): Size? do
    var at: Size = 0
    while at < s.len do
        val (cp, n) = decode(s, at)
        // A U+FFFD that is really there is 3 bytes long.
        ret at if cp == REPLACEMENT and n == 1
        at += n
    ret null

/**
 * Checks that `s` is valid UTF-8, for text from outside the program.
 * @return `s`
 * @throws when it isn't, with the position of the first broken byte
 */
fn validate(s: String): String throws ParseError do
    if val at = firstInvalid(s) do
        throw parseError("this text isn't valid UTF-8: the byte at ${at} doesn't fit", s, at)
    ret s

/** `s` with each byte that isn't valid UTF-8 replaced by {@link REPLACEMENT}. It is `s` itself when all of it is valid. */
fn sanitize(s: String): String do
    val start = firstInvalid(s) ?? ret s
    val b = stringBuilder(s.len + 8)
    b.append(s[0..<start])
    var at = start
    while at < s.len do
        val (cp, n) = decode(s, at)
        appendCodePoint(b, cp)
        at += n
    ret b.build()

// ---------------------------------------------------------------------------------------------------
// Counting and cutting

/** The number of code points in `s`, where `s.len` counts bytes: `length("héllo")` is 5. */
fn length(s: String): Size do
    var count: Size = 0
    var at: Size = 0
    while at < s.len do
        val (_, n) = decode(s, at)
        at += n
        count += 1
    ret count

/**
 * The byte where code point `n` of `s` starts.
 * @return the byte; `s.len` for `n` just past the last one; null when `s` has fewer code points
 */
fn byteOffset(s: String, n: Size): Size? do
    var at: Size = 0
    var left = n
    while left > 0 do
        ret null if at >= s.len
        val (_, used) = decode(s, at)
        at += used
        left -= 1
    ret at

/**
 * The code points of `s` from `from` up to, but not including, `until`: `slice("héllo", 1, 3)` is "él". It shares the memory
 * of `s`. Beyond the last code point, it stops at the end.
 */
fn slice(s: String, from: Size, until: Size): String do
    val start = byteOffset(s, from) ?? s.len
    val end = byteOffset(s, until) ?? s.len
    ret if start >= end do "" else s[start..<end]

/** At most `maxBytes` bytes of `s`, without cutting a code point in half. It shares the memory of `s`. */
fn truncate(s: String, maxBytes: Size): String do
    ret s if s.len <= maxBytes
    var end = maxBytes
    // A sequence is at most 4 bytes, so a continuation byte further back than that isn't part of one.
    while end > 0 and maxBytes - end < 3 and (s[end] as UChar) & 0xC0 == 0x80 do
        end -= 1
    ret s[0..<end]
