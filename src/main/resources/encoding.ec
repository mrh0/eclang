/**
 * Bytes as text and back: hexadecimal, Base64 (RFC 4648) and URL escapes.
 *
 *     use "core:encoding.ec"
 *     val text = encoding.base64("hello")                 // "aGVsbG8="
 *     val back = try encoding.fromBase64(text)            // "hello", or a ParseError
 *     val hex = encoding.hex("\xffA")                     // "ff41"
 *     val query = encoding.urlEscape("a b&c")             // "a%20b%26c"
 *
 * A String is bytes, so these work on any data. What they make is allocated in the current pool.
 */
module "encoding"
use "core:errors.ec"
use "core:string.ec"

/** The letters of Base64, by their value. */
priv val BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
/** The digits of hexadecimal, by their value. */
priv val HEX = "0123456789abcdef"

/** The byte at `i`, as a number from 0 to 255. */
priv fn byteAt(s: String, i: Size): Size = (s[i] as UChar) as Size

/** The bytes of `s` in hexadecimal, two lower-case digits each: `hex("\xffA")` is "ff41". */
fn hex(s: String): String do
    val out = new Char[s.len * 2]
    for i in 0..<s.len do
        out[i * 2] = HEX[byteAt(s, i) >> 4]
        out[i * 2 + 1] = HEX[byteAt(s, i) & 15]
    ret out !as String

/**
 * The bytes that {@link hex} wrote; the digits may be in either case.
 * @throws when `s` has an odd length, or a character that isn't a hexadecimal digit
 */
fn fromHex(s: String): String throws ParseError do
    throw parseError("hexadecimal text has two digits for each byte, but this has an odd length", s, s.len) if s.len % 2 != 0
    val out = new Char[s.len / 2]
    for i in 0..<out.len do
        val high = try hexValue(s, i * 2)
        val low = try hexValue(s, i * 2 + 1)
        out[i] = (high * 16 + low) as UChar as Char
    ret out !as String

/**
 * The value of the hexadecimal digit at `at`.
 * @throws when it isn't one
 */
priv fn hexValue(s: String, at: Size): Int throws ParseError do
    throw parseError("'${s[at]}' is not a hexadecimal digit", s, at) if not isHexDigit(s[at])
    ret digitValue(s[at]) ?? 0

/** `s` in Base64, with `=` padding: `base64("hello")` is "aGVsbG8=". */
fn base64(s: String): String do
    val out = new Char[(s.len + 2) / 3 * 4]
    var i: Size = 0
    var at: Size = 0
    while i < s.len do
        val left = s.len - i
        val b0 = byteAt(s, i)
        val b1 = if left > 1 do byteAt(s, i + 1) else 0
        val b2 = if left > 2 do byteAt(s, i + 2) else 0
        out[at] = BASE64[b0 >> 2]
        out[at + 1] = BASE64[((b0 & 3) << 4) | (b1 >> 4)]
        out[at + 2] = if left > 1 do BASE64[((b1 & 15) << 2) | (b2 >> 6)] else '='
        out[at + 3] = if left > 2 do BASE64[b2 & 63] else '='
        i += 3
        at += 4
    ret out !as String

/** The value of a Base64 letter, or null when `c` isn't one. */
priv fn base64Value(c: Char): Size? do
    ret (c - 'A') as Size if isUpper(c)
    ret (c - 'a') as Size + 26 if isLower(c)
    ret (c - '0') as Size + 52 if isDigit(c)
    ret 62 if c == '+'
    ret 63 if c == '/'
    ret null

/**
 * The bytes that {@link base64} wrote. The `=` padding is needed.
 * @throws when `s` isn't Base64
 */
fn fromBase64(s: String): String throws ParseError do
    throw parseError("Base64 text comes in groups of 4 characters, but this has ${s.len}", s, s.len) if s.len % 4 != 0
    ret "" if s.len == 0
    val padding: Size = if s[s.len - 2] == '=' do 2 else if s[s.len - 1] == '=' do 1 else 0
    val out = new Char[s.len / 4 * 3 - padding]
    val data = s.len - padding
    var bits: Size = 0
    var held: Size = 0
    var at: Size = 0
    for i in 0..<data do
        val value = base64Value(s[i])
        throw parseError("'${s[i]}' is not a Base64 character", s, i) if value == null
        bits = ((bits << 6) | value) & 0xFFFF
        held += 6
        if held >= 8 do
            held -= 8
            out[at] = ((bits >> held) & 255) as UChar as Char
            at += 1
    ret out !as String

/** Whether `c` stays as it is in a URL: a letter, a digit, or one of `-_.~`. */
priv fn isUrlSafe(c: Char): Bool = isAlnum(c) or c == '-' or c == '_' or c == '.' or c == '~'

/** `s` with everything but letters, digits and `-_.~` as %XX, for a part of a URL: `urlEscape("a b")` is "a%20b". */
fn urlEscape(s: String): String do
    val b = stringBuilder(s.len)
    for i, c in s do
        if isUrlSafe(c) do
            b.append(c)
        else do
            b.append('%')
            b.append(toUpper(HEX[byteAt(s, i) >> 4]))
            b.append(toUpper(HEX[byteAt(s, i) & 15]))
    ret b.build()

/**
 * `s` with each %XX replaced by its byte, and each `+` by a space when `plusIsSpace`, as in form data.
 * @param plusIsSpace whether `+` stands for a space
 * @throws when a `%` isn't followed by two hexadecimal digits
 */
fn urlUnescape(s: String, plusIsSpace: Bool = false): String throws ParseError do
    val b = stringBuilder(s.len)
    var i: Size = 0
    while i < s.len do
        if s[i] == '%' do
            throw parseError("a '%' needs two hexadecimal digits after it", s, i) if i + 2 >= s.len
            val high = try hexValue(s, i + 1)
            val low = try hexValue(s, i + 2)
            b.append((high * 16 + low) as UChar as Char)
            i += 3
        else do
            b.append(if plusIsSpace and s[i] == '+' do ' ' else s[i])
            i += 1
    ret b.build()
