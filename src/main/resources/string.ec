/**
 * Strings: searching, changing case, splitting and joining, padding, parsing numbers, and a StringBuilder.
 *
 *     use "core:string.ec"
 *     val name = " Ada Lovelace ".trim().toUpper()        // "ADA LOVELACE"
 *     val parts = "a, b, c".split(", ")                   // ["a", "b", "c"]
 *     val csv = join(parts, ";")                          // "a;b;c"
 *     val n = try parseDouble("2.5e3")                    // 2500.0, or a ParseError
 *
 * The functions are top-level, so they can be called on a String: `s.replace("a", "b")`. Characters are
 * bytes; the character classes (isDigit, toUpper, ...) are ASCII only, whatever the C locale is.
 * Functions returning a part of a String (trim, before, removePrefix, ...) return a slice, which shares the
 * String's memory. Functions building a new String allocate it in the current pool.
 */
use "core:errors.ec"
use "core:c/stdlib.ec"
use "core:c/string.ec"
use "core:apr/strings.ec"
use "core:math.ec"

// ---------------------------------------------------------------------------------------------------
// The basics

/** Whether `s` starts with `prefix`: `"photo.jpg".startsWith("photo")`. Every String starts with "". */
fn startsWith(s: String, prefix: String): Bool = prefix.len <= s.len and s[0..<prefix.len] == prefix

/** Whether `s` ends with `suffix`: `"photo.jpg".endsWith(".jpg")`. Every String ends with "". */
fn endsWith(s: String, suffix: String): Bool = suffix.len <= s.len and s[s.len - suffix.len..<s.len] == suffix

/**
 * Finds `part` in `s`: `"banana".indexOf("na")` is 2.
 * @return the index of the first `part`, or null when `s` doesn't contain it
 */
fn indexOf(s: String, part: String): Size? do
    if part.len > s.len do
        ret null
    for i in 0..=s.len - part.len do
        ret i if s[i..<i + part.len] == part
    ret null

/** Whether `part` is somewhere in `s`. */
fn contains(s: String, part: String): Bool = indexOf(s, part) != null

/** Whether `c` is a space, a tab or a line break ("\n" or "\r"). */
fn isSpace(c: Char): Bool = c == ' ' or c == '\t' or c == '\n' or c == '\r'

/**
 * `s` without spaces, tabs and line breaks at its start and end: `" a b ".trim()` is "a b". See {@link trimStart} and {@link trimEnd}.
 */
fn trim(s: String): String do
    var start: Size = 0
    var end = s.len
    while start < end and isSpace(s[start]) do
        start += 1
    while end > start and isSpace(s[end - 1]) do
        end -= 1
    ret s[start..<end]

/**
 * Splits `s` at each `separator`: `"a,b,,c".split(',')` is ["a", "b", "", "c"]. The array is in the current pool, and the parts share the memory of `s`.
 */
fn split(s: String, separator: Char): String[] do
    var count: Size = 1
    for c in s where c == separator do
        count += 1
    val parts = new String[count]
    var part: Size = 0
    var start: Size = 0
    for i, c in s do
        if c == separator do
            parts[part] = s[start..<i]
            part += 1
            start = i + 1
    parts[part] = s[start..<s.len]
    ret parts

/**
 * Parses a whole number: an optional sign, then decimal digits, with spaces around them allowed. See {@link parseLong} for other bases.
 * @return the number
 * @throws when `s` isn't a number ({@link ERR_PARSE}) or the number doesn't fit in a Long ({@link ERR_RANGE})
 */
fn parseInt(s: String): Long throws Error do
    val t = trim(s)
    throw Error { ERR_PARSE, "not a number: it is empty" } if t.len == 0
    val negative = t[0] == '-'
    var i: Size = if negative or t[0] == '+' do 1 else 0
    throw Error { ERR_PARSE, "not a number: no digits" } if i == t.len
    // Work with negative numbers, which reach one further than positive ones (the smallest Long).
    var value: Long = 0
    val limit: Long = -9223372036854775807 - 1
    while i < t.len do
        val c = t[i]
        throw Error { ERR_PARSE, "not a number: it has other characters than digits" } if c < '0' or c > '9'
        val digit = (c - '0') as Long
        throw Error { ERR_RANGE, "the number is too big for a Long" } if value < (limit + digit) / 10
        value = value * 10 - digit
        i += 1
    ret value if negative
    throw Error { ERR_RANGE, "the number is too big for a Long" } if value == limit
    ret -value

// ---------------------------------------------------------------------------------------------------
// Characters (ASCII)

/** Whether `c` is a decimal digit, 0 to 9. */
fn isDigit(c: Char): Bool = c >= '0' and c <= '9'

/** Whether `c` is a lower-case letter, a to z. */
fn isLower(c: Char): Bool = c >= 'a' and c <= 'z'

/** Whether `c` is an upper-case letter, A to Z. */
fn isUpper(c: Char): Bool = c >= 'A' and c <= 'Z'

/** Whether `c` is a letter, a to z or A to Z. */
fn isAlpha(c: Char): Bool = isLower(c) or isUpper(c)

/** Whether `c` is a letter or a decimal digit. */
fn isAlnum(c: Char): Bool = isAlpha(c) or isDigit(c)

/** Whether `c` is a hexadecimal digit: 0 to 9, a to f or A to F. */
fn isHexDigit(c: Char): Bool = isDigit(c) or (c >= 'a' and c <= 'f') or (c >= 'A' and c <= 'F')

/**
 * Whether `c` is a printable character other than a letter, a digit or space: one of
 * ``!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~``.
 */
fn isPunct(c: Char): Bool = c > ' ' and c < 127 and not isAlnum(c)

/** `c` in upper case when it is a letter (a to z); other characters stay as they are. */
fn toUpper(c: Char): Char = if isLower(c) do c - 32 else c

/** `c` in lower case when it is a letter (A to Z); other characters stay as they are. */
fn toLower(c: Char): Char = if isUpper(c) do c + 32 else c

/**
 * The value of a digit in bases up to 36: 0 to 9 for 0-9, then 10 to 35 for a-z or A-Z.
 * @return the value, or null when `c` isn't a digit or letter
 */
fn digitValue(c: Char): Int? do
    ret (c - '0') as Int if isDigit(c)
    ret (c - 'a' + 10) as Int if isLower(c)
    ret (c - 'A' + 10) as Int if isUpper(c)
    ret null

// ---------------------------------------------------------------------------------------------------
// Making Strings

/** A copy of the C string `cs`, up to its terminating 0, in the current pool. */
fn fromC(cs: CString): String = format("${cs}")

/** A String of the one character `c`. */
fn charString(c: Char): String do
    val buffer = new Char[1]
    buffer[0] = c
    ret buffer !as String

/** `s`, `n` times over: `"ab".repeat(3)` is "ababab". */
fn repeat(s: String, n: Size): String do
    val buffer = new Char[s.len * n]
    for i in 0..<n do
        memcpy(buffer[i * s.len..<buffer.len].data, s.data, s.len)
    ret buffer !as String

/** The parts, with `separator` between each two: `join(["a", "b", "c"], ", ")` is "a, b, c". */
fn join(parts: String[], separator: String): String do
    ret "" if parts.len == 0
    var total = separator.len * (parts.len - 1)
    for p in parts do
        total += p.len
    val b = stringBuilder(total)
    for i, p in parts do
        append(b, separator) if i > 0
        append(b, p)
    ret build(b)

// ---------------------------------------------------------------------------------------------------
// Inspecting

/** Whether `s` has no characters. See {@link isBlank}. */
fn isEmpty(s: String): Bool = s.len == 0

/** Whether `s` is empty or only has spaces, tabs and line breaks. */
fn isBlank(s: String): Bool do
    for c in s where not isSpace(c) do
        ret false
    ret true

/** Whether `s` starts with the character `c`. */
fn startsWith(s: String, c: Char): Bool = s.len > 0 and s[0] == c

/** Whether `s` ends with the character `c`. */
fn endsWith(s: String, c: Char): Bool = s.len > 0 and s[s.len - 1] == c

/** The index of the first `c` in `s`, or null when there is none. */
fn indexOf(s: String, c: Char): Size? do
    for i, x in s where x == c do
        ret i
    ret null

/**
 * Finds `part` in `s`, starting from `from`.
 * @param from where to start looking
 * @return the index of the first `part` at `from` or after it, or null
 */
fn indexOf(s: String, part: String, from: Size): Size? do
    ret null if from > s.len
    if val i = indexOf(s[from..<s.len], part) do
        ret from + i
    ret null

/** The index of the last `part` in `s`, or null when there is none. */
fn lastIndexOf(s: String, part: String): Size? do
    ret null if part.len > s.len
    var i = s.len - part.len + 1
    while i > 0 do
        i -= 1
        ret i if s[i..<i + part.len] == part
    ret null

/** The index of the last `c` in `s`, or null when there is none. */
fn lastIndexOf(s: String, c: Char): Size? do
    var i = s.len
    while i > 0 do
        i -= 1
        ret i if s[i] == c
    ret null

/** Whether the character `c` is somewhere in `s`. */
fn contains(s: String, c: Char): Bool = indexOf(s, c) != null

/**
 * How many times `part` is in `s`, not counting overlaps (`"aaaa".count("aa")` is 2). An empty `part` counts 0.
 */
fn count(s: String, part: String): Size do
    ret 0 if part.len == 0
    var n: Size = 0
    var at: Size = 0
    while true do
        val i = indexOf(s, part, at)
        break if i == null
        n += 1
        at = i + part.len
    ret n

/** How many times the character `c` is in `s`. */
fn count(s: String, c: Char): Size do
    var n: Size = 0
    for x in s where x == c do
        n += 1
    ret n

/**
 * Compares byte by byte, so "Z" comes before "a". See {@link naturalCompare} and {@link equalsIgnoreCase}.
 * @return -1, 0 or 1 as `a` comes before, is equal to or comes after `b`
 */
fn compare(a: String, b: String): Int do
    val n = if a.len < b.len do a.len else b.len
    val diff = if n == 0 do 0 else memcmp(a.data, b.data, n)
    ret -1 if diff < 0 or (diff == 0 and a.len < b.len)
    ret 1 if diff > 0 or (diff == 0 and a.len > b.len)
    ret 0

/** Whether `a` and `b` are equal when upper and lower case (ASCII) don't matter: "Yes" and "YES" are. */
fn equalsIgnoreCase(a: String, b: String): Bool do
    ret false if a.len != b.len
    for i, c in a where toLower(c) != toLower(b[i]) do
        ret false
    ret true

/**
 * Compares like a person sorting file names: "file9" before "file10" (APR's apr_strnatcmp).
 * @return less than 0, 0 or more than 0 as `a` comes before, is equal to or comes after `b`
 */
fn naturalCompare(a: String, b: String): Int = apr_strnatcmp(cstr(a), cstr(b))

/**
 * A 64-bit FNV-1a hash of `s`, for hash tables: equal Strings have equal hashes. Not for passwords or security.
 */
fn hash(s: String): ULong do
    var h: ULong = (0xcbf29ce4ul << 32) | 0x84222325ul
    for c in s do
        h = math.wrappingMul(h ^ (c as UChar as ULong), 1099511628211)
    ret h

// ---------------------------------------------------------------------------------------------------
// Parts of a String (slices: no copy is made)

/** `s` without spaces, tabs and line breaks at its start. */
fn trimStart(s: String): String do
    var start: Size = 0
    while start < s.len and isSpace(s[start]) do
        start += 1
    ret s[start..<s.len]

/** `s` without spaces, tabs and line breaks at its end. */
fn trimEnd(s: String): String do
    var end = s.len
    while end > 0 and isSpace(s[end - 1]) do
        end -= 1
    ret s[0..<end]

/**
 * `s` without the characters of `chars` at its start and end: `trim("--x--", "-")` is "x".
 * @param chars the characters to remove, in any order
 */
fn trim(s: String, chars: String): String do
    var start: Size = 0
    var end = s.len
    while start < end and contains(chars, s[start]) do
        start += 1
    while end > start and contains(chars, s[end - 1]) do
        end -= 1
    ret s[start..<end]

/** `s` without `prefix` at its start, or `s` when it doesn't start with it. */
fn removePrefix(s: String, prefix: String): String = if startsWith(s, prefix) do s[prefix.len..<s.len] else s

/** `s` without `suffix` at its end, or `s` when it doesn't end with it. */
fn removeSuffix(s: String, suffix: String): String = if endsWith(s, suffix) do s[0..<s.len - suffix.len] else s

/**
 * The part of `s` before the first `separator`: `before("key=value", "=")` is "key".
 * @return the part, or all of `s` when there is no `separator`
 */
fn before(s: String, separator: String): String do
    if val i = indexOf(s, separator) do
        ret s[0..<i]
    ret s

/**
 * The part of `s` after the first `separator`: `after("key=value", "=")` is "value".
 * @return the part, or "" when there is no `separator`
 */
fn after(s: String, separator: String): String do
    if val i = indexOf(s, separator) do
        ret s[i + separator.len..<s.len]
    ret ""

/**
 * The part of `s` before the last `separator`: `beforeLast("a/b/c", "/")` is "a/b".
 * @return the part, or all of `s` when there is no `separator`
 */
fn beforeLast(s: String, separator: String): String do
    if val i = lastIndexOf(s, separator) do
        ret s[0..<i]
    ret s

/**
 * The part of `s` after the last `separator`: `afterLast("a/b/c.txt", "/")` is "c.txt".
 * @return the part, or "" when there is no `separator`
 */
fn afterLast(s: String, separator: String): String do
    if val i = lastIndexOf(s, separator) do
        ret s[i + separator.len..<s.len]
    ret ""

/** The first `n` bytes of `s` (all of `s` when it is shorter). */
fn take(s: String, n: Size): String = if n >= s.len do s else s[0..<n]

/** `s` without its first `n` bytes ("" when it is shorter). */
fn drop(s: String, n: Size): String = if n >= s.len do "" else s[n..<s.len]

// ---------------------------------------------------------------------------------------------------
// Splitting (the parts are slices, in an array in the current pool)

/**
 * Splits `s` at each `separator`: `split("a, b", ", ")` is ["a", "b"]. An empty `separator` splits nothing. The parts share the memory of `s`.
 */
fn split(s: String, separator: String): String[] do
    if separator.len == 0 do
        val whole = new String[1]
        whole[0] = s
        ret whole
    val parts = new String[count(s, separator) + 1]
    var part: Size = 0
    var start: Size = 0
    while true do
        val i = indexOf(s, separator, start)
        break if i == null
        parts[part] = s[start..<i]
        part += 1
        start = i + separator.len
    parts[part] = s[start..<s.len]
    ret parts

/** The lines of `s`, without their "\n" or "\r\n". A line break at the very end doesn't start another line. */
fn lines(s: String): String[] do
    val body = removeSuffix(s, "\n")
    ret new String[0] if s.len == 0
    val parts = split(body, '\n')
    for i, line in parts do
        parts[i] = removeSuffix(line, "\r")
    ret parts

/** The words of `s`: the parts between runs of spaces, tabs and line breaks. `words("  a  b ")` is ["a", "b"]. */
fn words(s: String): String[] do
    var n: Size = 0
    var inWord = false
    for c in s do
        if isSpace(c) do
            inWord = false
        else if not inWord do
            inWord = true
            n += 1
    val parts = new String[n]
    var part: Size = 0
    var start: Size = 0
    inWord = false
    for i, c in s do
        if isSpace(c) do
            if inWord do
                parts[part] = s[start..<i]
                part += 1
            inWord = false
        else if not inWord do
            inWord = true
            start = i
    if inWord do
        parts[part] = s[start..<s.len]
    ret parts

// ---------------------------------------------------------------------------------------------------
// Changed copies (in the current pool)

/** A copy of `s` with its letters in upper case (ASCII). */
fn toUpper(s: String): String do
    val buffer = new Char[s.len]
    for i, c in s do
        buffer[i] = toUpper(c)
    ret buffer !as String

/** A copy of `s` with its letters in lower case (ASCII). */
fn toLower(s: String): String do
    val buffer = new Char[s.len]
    for i, c in s do
        buffer[i] = toLower(c)
    ret buffer !as String

/** `s` with its first character in upper case: `"hello".capitalize()` is "Hello". */
fn capitalize(s: String): String do
    ret s if s.len == 0 or not isLower(s[0])
    val buffer = new Char[s.len]
    memcpy(buffer.data, s.data, s.len)
    buffer[0] = toUpper(s[0])
    ret buffer !as String

/** `s` with its bytes in the opposite order (so a UTF-8 character of several bytes is broken). */
fn reverse(s: String): String do
    val buffer = new Char[s.len]
    for i, c in s do
        buffer[s.len - 1 - i] = c
    ret buffer !as String

/**
 * `s` with every `target` replaced by `replacement`: `"a-b-c".replace("-", ", ")` is "a, b, c". An empty `target` replaces nothing.
 */
fn replace(s: String, target: String, replacement: String): String do
    ret s if target.len == 0
    val n = count(s, target)
    ret s if n == 0
    val b = stringBuilder(s.len - n * target.len + n * replacement.len)
    var start: Size = 0
    while true do
        val i = indexOf(s, target, start)
        break if i == null
        append(b, s[start..<i])
        append(b, replacement)
        start = i + target.len
    append(b, s[start..<s.len])
    ret build(b)

/** `s` with every `target` character replaced by `replacement`. */
fn replace(s: String, target: Char, replacement: Char): String do
    val buffer = new Char[s.len]
    for i, c in s do
        buffer[i] = if c == target do replacement else c
    ret buffer !as String

/**
 * `s`, made at least `width` bytes long by adding `fill` at its start: `padStart("7", 3, '0')` is "007".
 * @param width the length to reach; a longer `s` is returned as it is
 */
fn padStart(s: String, width: Size, fill: Char = ' '): String do
    ret s if s.len >= width
    val buffer = new Char[width]
    val pad = width - s.len
    for i in 0..<pad do
        buffer[i] = fill
    memcpy(buffer[pad..<width].data, s.data, s.len)
    ret buffer !as String

/**
 * `s`, made at least `width` bytes long by adding `fill` at its end: `padEnd("ab", 4, '.')` is "ab..".
 * @param width the length to reach; a longer `s` is returned as it is
 */
fn padEnd(s: String, width: Size, fill: Char = ' '): String do
    ret s if s.len >= width
    val buffer = new Char[width]
    memcpy(buffer.data, s.data, s.len)
    for i in s.len..<width do
        buffer[i] = fill
    ret buffer !as String

/**
 * `s` in the middle of `width` bytes of `fill`, with one more on the right when it can't be even.
 * @param width the length to reach; a longer `s` is returned as it is
 */
fn center(s: String, width: Size, fill: Char = ' '): String do
    ret s if s.len >= width
    ret padEnd(padStart(s, s.len + (width - s.len) / 2, fill), width, fill)

/**
 * `s` as an eclang (and C) string literal, with quotes, and escapes for quotes, backslashes and control characters: `quote("a\"b")` is `"a\"b"`.
 */
fn quote(s: String): String do
    val b = stringBuilder(s.len + 2)
    append(b, '"')
    for c in s do
        when c do
            '"' do append(b, "\\\"")
            '\\' do append(b, "\\\\")
            '\n' do append(b, "\\n")
            '\r' do append(b, "\\r")
            '\t' do append(b, "\\t")
            else do
                if c >= ' ' and c != 127 or c < 0 do
                    append(b, c)
                else do
                    append(b, "\\x")
                    append(b, hexDigit((c as UChar >> 4) as Int))
                    append(b, hexDigit((c as UChar & 15) as Int))
    append(b, '"')
    ret build(b)

/** The hexadecimal digit for 0 to 15, in lower case. */
priv fn hexDigit(value: Int): Char = "0123456789abcdef"[value as Size]

// ---------------------------------------------------------------------------------------------------
// Numbers to text and back

/**
 * `n` written in base `base`, with a '-' when it is negative: `toBase(255, 16)` is "ff".
 * @param base 2 to 36; the digits after 9 are a to z
 */
fn toBase(n: Long, base: Int): String do
    assert base >= 2 and base <= 36, "toBase needs a base from 2 to 36"
    ret "0" if n == 0
    val buffer = new Char[65]
    var at = buffer.len
    // Negative numbers are worked on as negative, so the smallest Long doesn't overflow.
    var rest = if n > 0 do -n else n
    while rest != 0 do
        at -= 1
        buffer[at] = "0123456789abcdefghijklmnopqrstuvwxyz"[(-(rest % base)) as Size]
        rest /= base
    if n < 0 do
        at -= 1
        buffer[at] = '-'
    ret buffer[at..<buffer.len] !as String

/** `n` in hexadecimal, in lower case and without "0x": `hex(255)` is "ff". */
fn hex(n: ULong): String do
    ret "0" if n == 0
    val buffer = new Char[16]
    var at = buffer.len
    var rest = n
    while rest != 0 do
        at -= 1
        buffer[at] = hexDigit((rest & 15) as Int)
        rest = rest >> 4
    ret buffer[at..<buffer.len] !as String

/**
 * Parses a whole number in any base, with an optional sign; for base 16, also an optional "0x". Spaces around it are allowed. `parseLong("-ff", 16)` is -255.
 * @param base 2 to 36
 * @throws when `s` isn't a number in `base`, or the number doesn't fit in a Long
 */
fn parseLong(s: String, base: Int): Long throws ParseError do
    throw parseError("the base must be from 2 to 36", s, 0) if base < 2 or base > 36
    val t = trim(s)
    val offset = indexOf(s, t) ?? 0
    throw parseError("not a number: it is empty", s, offset) if t.len == 0
    val negative = t[0] == '-'
    var i: Size = if negative or t[0] == '+' do 1 else 0
    if base == 16 and t.len >= i + 2 and t[i] == '0' and (t[i + 1] == 'x' or t[i + 1] == 'X') do
        i += 2
    throw parseError("not a number: it has no digits", s, offset + i) if i == t.len
    // Work with negative numbers, which reach one further than positive ones (the smallest Long).
    var value: Long = 0
    val limit: Long = -9223372036854775807 - 1
    while i < t.len do
        val digit = digitValue(t[i]) ?? 99
        throw parseError("'${t[i]}' is not a digit in base ${base}", s, offset + i) if digit >= base
        throw parseError("the number is too big for a Long", s, offset) if value < (limit + digit) / base
        value = value * base - digit
        i += 1
    if not negative do
        throw parseError("the number is too big for a Long", s, offset) if value == limit
        ret -value
    ret value

/**
 * Parses a decimal number: "3.25", "-1e9", "inf", "nan". Spaces around it are allowed.
 * @throws when `s` isn't a number
 */
fn parseDouble(s: String): Double throws ParseError do
    val t = trim(s)
    throw parseError("not a number: it is empty", s, 0) if t.len == 0
    val text = cstr(t)
    var end = text
    val value = strtod(text, addrof(end))
    val used = (end !as ULong) - (text !as ULong)
    throw parseError("not a number", s, 0) if used == 0
    throw parseError("the number has other characters after it", s, (indexOf(s, t) ?? 0) + used) if used != t.len
    ret value

/**
 * Parses a truth value, in any case: "true", "yes", "on" and "1" are true; "false", "no", "off" and "0" are false.
 * @throws when `s` is none of them
 */
fn parseBool(s: String): Bool throws ParseError do
    val t = toLower(trim(s))
    ret true if t == "true" or t == "yes" or t == "on" or t == "1"
    ret false if t == "false" or t == "no" or t == "off" or t == "0"
    throw parseError("expected true or false", s, 0)

// ---------------------------------------------------------------------------------------------------
// UTF-8

/**
 * The number of Unicode characters (code points) in `s`, which must be valid UTF-8 (see {@link isUtf8}); `s.len` counts bytes.
 */
fn charCount(s: String): Size do
    var n: Size = 0
    for c in s where (c as UChar) & 0xC0 != 0x80 do
        n += 1
    ret n

/** Whether `s` is valid UTF-8: no broken sequences, overlong forms or surrogates. */
fn isUtf8(s: String): Bool do
    var i: Size = 0
    while i < s.len do
        val b = s[i] as UChar
        val n: Size = if b < 0x80 do 1 else if b >= 0xC2 and b <= 0xDF do 2 else if b >= 0xE0 and b <= 0xEF do 3 else if b >= 0xF0 and b <= 0xF4 do 4 else 0
        ret false if n == 0 or i + n > s.len
        for k in 1..<n where (s[i + k] as UChar) & 0xC0 != 0x80 do
            ret false
        if n == 3 do
            val b1 = s[i + 1] as UChar
            ret false if (b == 0xE0 and b1 < 0xA0) or (b == 0xED and b1 >= 0xA0)
        if n == 4 do
            val b1 = s[i + 1] as UChar
            ret false if (b == 0xF0 and b1 < 0x90) or (b == 0xF4 and b1 >= 0x90)
        i += n
    ret true

// ---------------------------------------------------------------------------------------------------
// StringBuilder

/**
 * Builds a String piece by piece, growing as needed. Its memory is in the pool that was current when it was
 * made (or the pool given), and so is the String that {@link build} gives.
 *
 *     val b = stringBuilder()
 *     for name in names do
 *         b.append(name)
 *         b.append(',')
 *     io.log(b.build())
 */
rec StringBuilder as
    /** What it holds so far, and room for more. */
    buffer: Char[]
    /** How many bytes of {@code buffer} it holds. */
    len: Size
    /** The pool its memory is in. */
    mem: Pool

/**
 * A new {@link StringBuilder}, in the current pool.
 * @param capacity how many bytes it has room for before it grows
 */
fn stringBuilder(capacity: Size = 16): @StringBuilder = new StringBuilder { new Char[if capacity == 0 do 16 else capacity], 0, pool }

/**
 * A new {@link StringBuilder}, in the pool `mem`.
 * @param mem the pool for its memory and the String it builds
 * @param capacity how many bytes it has room for before it grows
 */
fn stringBuilder(mem: Pool, capacity: Size): @StringBuilder = new StringBuilder { new Char[if capacity == 0 do 16 else capacity] in mem, 0, mem } in mem

/** Makes room for `n` more bytes, so that appending them doesn't grow the buffer again. */
fn reserve(b: @StringBuilder, n: Size) do
    if b.len + n > b.buffer.len do
        var capacity = b.buffer.len * 2
        while capacity < b.len + n do
            capacity *= 2
        val grown = new Char[capacity] in b.mem
        memcpy(grown.data, b.buffer.data, b.len)
        b.buffer = grown

/** Appends `s`. */
fn append(b: @StringBuilder, s: String) do
    reserve(b, s.len)
    memcpy(b.buffer[b.len..<b.buffer.len].data, s.data, s.len)
    b.len += s.len

/** Appends the character `c`. */
fn append(b: @StringBuilder, c: Char) do
    reserve(b, 1)
    b.buffer[b.len] = c
    b.len += 1

/** Appends the C string `s`. */
fn append(b: @StringBuilder, s: CString) do
    val n = strlen(s)
    reserve(b, n)
    memcpy(b.buffer[b.len..<b.buffer.len].data, s, n)
    b.len += n

/** Appends `n` in decimal. */
fn append(b: @StringBuilder, n: Long) do
    append(b, format(b.mem, "${n}"))

/** Appends `x`, written as `"${x}"` writes it. */
fn append(b: @StringBuilder, x: Double) do
    append(b, format(b.mem, "${x}"))

/** Appends `s` and a line break. */
fn appendLine(b: @StringBuilder, s: String) do
    append(b, s)
    append(b, '\n')

/** Starts again from an empty String. Strings that {@link build} gave before stay as they were. */
fn clear(b: @StringBuilder) do
    b.buffer = new Char[b.buffer.len] in b.mem
    b.len = 0

/** The String built so far, without copying it: appending more later doesn't change it. */
fn build(b: @StringBuilder): String = b.buffer[0..<b.len] !as String
