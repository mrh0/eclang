# `core:string.ec`

Strings: searching, changing case, splitting and joining, padding, parsing numbers, and a StringBuilder.

    use "core:string.ec"
    val name = " Ada Lovelace ".trim().toUpper()        // "ADA LOVELACE"
    val parts = "a, b, c".split(", ")                   // ["a", "b", "c"]
    val csv = join(parts, ";")                          // "a;b;c"
    val n = try parseDouble("2.5e3")                    // 2500.0, or a ParseError

The functions are top-level, so they can be called on a String: `s.replace("a", "b")`. Characters are
bytes; the character classes (isDigit, toUpper, ...) are ASCII only, whatever the C locale is.
Functions returning a part of a String (trim, before, removePrefix, ...) return a slice, which shares the
String's memory. Functions building a new String allocate it in the current pool.

## Types

### <a id="StringBuilder"></a>`StringBuilder`

```ec
mut ref rec StringBuilder
```

Builds a String piece by piece, growing as needed. Its memory is in the pool that was current when it was
made (or the pool given), and so is the String that [`build`](#build) gives.

    val b = stringBuilder()
    for name in names do
        b.append(name)
        b.append(',')
    io.log(b.build())

**Fields**

- <a id="StringBuilder.buffer"></a>`buffer: Char[]` — What it holds so far, and room for more.
- <a id="StringBuilder.len"></a>`len: Size` — How many bytes of `buffer` it holds.
- <a id="StringBuilder.mem"></a>`mem: Pool` — The pool its memory is in.

## Functions

### <a id="startsWith"></a>`startsWith`

```ec
fn startsWith(s: String, prefix: String): Bool
```

Whether `s` starts with `prefix`: `"photo.jpg".startsWith("photo")`. Every String starts with "".

### <a id="endsWith"></a>`endsWith`

```ec
fn endsWith(s: String, suffix: String): Bool
```

Whether `s` ends with `suffix`: `"photo.jpg".endsWith(".jpg")`. Every String ends with "".

### <a id="indexOf"></a>`indexOf`

```ec
fn indexOf(s: String, part: String): Size?
```

Finds `part` in `s`: `"banana".indexOf("na")` is 2.

**Returns** the index of the first `part`, or null when `s` doesn't contain it

### <a id="contains"></a>`contains`

```ec
fn contains(s: String, part: String): Bool
```

Whether `part` is somewhere in `s`.

### <a id="isSpace"></a>`isSpace`

```ec
fn isSpace(c: Char): Bool
```

Whether `c` is a space, a tab or a line break ("\n" or "\r").

### <a id="trim"></a>`trim`

```ec
fn trim(s: String): String
```

`s` without spaces, tabs and line breaks at its start and end: `" a b ".trim()` is "a b". See [`trimStart`](#trimStart) and [`trimEnd`](#trimEnd).

### <a id="split"></a>`split`

```ec
fn split(s: String, separator: Char): String[]
```

Splits `s` at each `separator`: `"a,b,,c".split(',')` is ["a", "b", "", "c"]. The array is in the current pool, and the parts share the memory of `s`.

### <a id="parseInt"></a>`parseInt`

```ec
fn parseInt(s: String): Long throws Error
```

Parses a whole number: an optional sign, then decimal digits, with spaces around them allowed. See [`parseLong`](#parseLong) for other bases.

**Returns** the number

**Throws**

- `Error` — when `s` isn't a number (`ERR_PARSE`) or the number doesn't fit in a Long (`ERR_RANGE`)

### <a id="isDigit"></a>`isDigit`

```ec
fn isDigit(c: Char): Bool
```

Whether `c` is a decimal digit, 0 to 9.

### <a id="isLower"></a>`isLower`

```ec
fn isLower(c: Char): Bool
```

Whether `c` is a lower-case letter, a to z.

### <a id="isUpper"></a>`isUpper`

```ec
fn isUpper(c: Char): Bool
```

Whether `c` is an upper-case letter, A to Z.

### <a id="isAlpha"></a>`isAlpha`

```ec
fn isAlpha(c: Char): Bool
```

Whether `c` is a letter, a to z or A to Z.

### <a id="isAlnum"></a>`isAlnum`

```ec
fn isAlnum(c: Char): Bool
```

Whether `c` is a letter or a decimal digit.

### <a id="isHexDigit"></a>`isHexDigit`

```ec
fn isHexDigit(c: Char): Bool
```

Whether `c` is a hexadecimal digit: 0 to 9, a to f or A to F.

### <a id="isPunct"></a>`isPunct`

```ec
fn isPunct(c: Char): Bool
```

Whether `c` is a printable character other than a letter, a digit or space: one of
``!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~``.

### <a id="toUpper"></a>`toUpper`

```ec
fn toUpper(c: Char): Char
```

`c` in upper case when it is a letter (a to z); other characters stay as they are.

### <a id="toLower"></a>`toLower`

```ec
fn toLower(c: Char): Char
```

`c` in lower case when it is a letter (A to Z); other characters stay as they are.

### <a id="digitValue"></a>`digitValue`

```ec
fn digitValue(c: Char): Int?
```

The value of a digit in bases up to 36: 0 to 9 for 0-9, then 10 to 35 for a-z or A-Z.

**Returns** the value, or null when `c` isn't a digit or letter

### <a id="fromC"></a>`fromC`

```ec
fn fromC(cs: CString): String
```

A copy of the C string `cs`, up to its terminating 0, in the current pool.

### <a id="charString"></a>`charString`

```ec
fn charString(c: Char): String
```

A String of the one character `c`.

### <a id="repeat"></a>`repeat`

```ec
fn repeat(s: String, n: Size): String
```

`s`, `n` times over: `"ab".repeat(3)` is "ababab".

### <a id="join"></a>`join`

```ec
fn join(parts: String[], separator: String): String
```

The parts, with `separator` between each two: `join(["a", "b", "c"], ", ")` is "a, b, c".

### <a id="isEmpty"></a>`isEmpty`

```ec
fn isEmpty(s: String): Bool
```

Whether `s` has no characters. See [`isBlank`](#isBlank).

### <a id="isBlank"></a>`isBlank`

```ec
fn isBlank(s: String): Bool
```

Whether `s` is empty or only has spaces, tabs and line breaks.

### `startsWith`

```ec
fn startsWith(s: String, c: Char): Bool
```

Whether `s` starts with the character `c`.

### `endsWith`

```ec
fn endsWith(s: String, c: Char): Bool
```

Whether `s` ends with the character `c`.

### `indexOf`

```ec
fn indexOf(s: String, c: Char): Size?
```

The index of the first `c` in `s`, or null when there is none.

### `indexOf`

```ec
fn indexOf(s: String, part: String, from: Size): Size?
```

Finds `part` in `s`, starting from `from`.

**Parameters**

- `from` — where to start looking

**Returns** the index of the first `part` at `from` or after it, or null

### <a id="lastIndexOf"></a>`lastIndexOf`

```ec
fn lastIndexOf(s: String, part: String): Size?
```

The index of the last `part` in `s`, or null when there is none.

### `lastIndexOf`

```ec
fn lastIndexOf(s: String, c: Char): Size?
```

The index of the last `c` in `s`, or null when there is none.

### `contains`

```ec
fn contains(s: String, c: Char): Bool
```

Whether the character `c` is somewhere in `s`.

### <a id="count"></a>`count`

```ec
fn count(s: String, part: String): Size
```

How many times `part` is in `s`, not counting overlaps (`"aaaa".count("aa")` is 2). An empty `part` counts 0.

### `count`

```ec
fn count(s: String, c: Char): Size
```

How many times the character `c` is in `s`.

### <a id="compare"></a>`compare`

```ec
fn compare(a: String, b: String): Int
```

Compares byte by byte, so "Z" comes before "a". See [`naturalCompare`](#naturalCompare) and [`equalsIgnoreCase`](#equalsIgnoreCase).

**Returns** -1, 0 or 1 as `a` comes before, is equal to or comes after `b`

### <a id="equalsIgnoreCase"></a>`equalsIgnoreCase`

```ec
fn equalsIgnoreCase(a: String, b: String): Bool
```

Whether `a` and `b` are equal when upper and lower case (ASCII) don't matter: "Yes" and "YES" are.

### <a id="naturalCompare"></a>`naturalCompare`

```ec
fn naturalCompare(a: String, b: String): Int
```

Compares like a person sorting file names: "file9" before "file10" (APR's apr_strnatcmp).

**Returns** less than 0, 0 or more than 0 as `a` comes before, is equal to or comes after `b`

### <a id="hash"></a>`hash`

```ec
fn hash(s: String): ULong
```

A 64-bit FNV-1a hash of `s`, for hash tables: equal Strings have equal hashes. Not for passwords or security.

### <a id="trimStart"></a>`trimStart`

```ec
fn trimStart(s: String): String
```

`s` without spaces, tabs and line breaks at its start.

### <a id="trimEnd"></a>`trimEnd`

```ec
fn trimEnd(s: String): String
```

`s` without spaces, tabs and line breaks at its end.

### `trim`

```ec
fn trim(s: String, chars: String): String
```

`s` without the characters of `chars` at its start and end: `trim("--x--", "-")` is "x".

**Parameters**

- `chars` — the characters to remove, in any order

### <a id="removePrefix"></a>`removePrefix`

```ec
fn removePrefix(s: String, prefix: String): String
```

`s` without `prefix` at its start, or `s` when it doesn't start with it.

### <a id="removeSuffix"></a>`removeSuffix`

```ec
fn removeSuffix(s: String, suffix: String): String
```

`s` without `suffix` at its end, or `s` when it doesn't end with it.

### <a id="before"></a>`before`

```ec
fn before(s: String, separator: String): String
```

The part of `s` before the first `separator`: `before("key=value", "=")` is "key".

**Returns** the part, or all of `s` when there is no `separator`

### <a id="after"></a>`after`

```ec
fn after(s: String, separator: String): String
```

The part of `s` after the first `separator`: `after("key=value", "=")` is "value".

**Returns** the part, or "" when there is no `separator`

### <a id="beforeLast"></a>`beforeLast`

```ec
fn beforeLast(s: String, separator: String): String
```

The part of `s` before the last `separator`: `beforeLast("a/b/c", "/")` is "a/b".

**Returns** the part, or all of `s` when there is no `separator`

### <a id="afterLast"></a>`afterLast`

```ec
fn afterLast(s: String, separator: String): String
```

The part of `s` after the last `separator`: `afterLast("a/b/c.txt", "/")` is "c.txt".

**Returns** the part, or "" when there is no `separator`

### <a id="take"></a>`take`

```ec
fn take(s: String, n: Size): String
```

The first `n` bytes of `s` (all of `s` when it is shorter).

### <a id="drop"></a>`drop`

```ec
fn drop(s: String, n: Size): String
```

`s` without its first `n` bytes ("" when it is shorter).

### `split`

```ec
fn split(s: String, separator: String): String[]
```

Splits `s` at each `separator`: `split("a, b", ", ")` is ["a", "b"]. An empty `separator` splits nothing. The parts share the memory of `s`.

### <a id="lines"></a>`lines`

```ec
fn lines(s: String): String[]
```

The lines of `s`, without their "\n" or "\r\n". A line break at the very end doesn't start another line.

### <a id="words"></a>`words`

```ec
fn words(s: String): String[]
```

The words of `s`: the parts between runs of spaces, tabs and line breaks. `words("  a  b ")` is ["a", "b"].

### `toUpper`

```ec
fn toUpper(s: String): String
```

A copy of `s` with its letters in upper case (ASCII).

### `toLower`

```ec
fn toLower(s: String): String
```

A copy of `s` with its letters in lower case (ASCII).

### <a id="capitalize"></a>`capitalize`

```ec
fn capitalize(s: String): String
```

`s` with its first character in upper case: `"hello".capitalize()` is "Hello".

### <a id="reverse"></a>`reverse`

```ec
fn reverse(s: String): String
```

`s` with its bytes in the opposite order (so a UTF-8 character of several bytes is broken).

### <a id="replace"></a>`replace`

```ec
fn replace(s: String, target: String, replacement: String): String
```

`s` with every `target` replaced by `replacement`: `"a-b-c".replace("-", ", ")` is "a, b, c". An empty `target` replaces nothing.

### `replace`

```ec
fn replace(s: String, target: Char, replacement: Char): String
```

`s` with every `target` character replaced by `replacement`.

### <a id="padStart"></a>`padStart`

```ec
fn padStart(s: String, width: Size, fill: Char = ' '): String
```

`s`, made at least `width` bytes long by adding `fill` at its start: `padStart("7", 3, '0')` is "007".

**Parameters**

- `width` — the length to reach; a longer `s` is returned as it is

### <a id="padEnd"></a>`padEnd`

```ec
fn padEnd(s: String, width: Size, fill: Char = ' '): String
```

`s`, made at least `width` bytes long by adding `fill` at its end: `padEnd("ab", 4, '.')` is "ab..".

**Parameters**

- `width` — the length to reach; a longer `s` is returned as it is

### <a id="center"></a>`center`

```ec
fn center(s: String, width: Size, fill: Char = ' '): String
```

`s` in the middle of `width` bytes of `fill`, with one more on the right when it can't be even.

**Parameters**

- `width` — the length to reach; a longer `s` is returned as it is

### <a id="quote"></a>`quote`

```ec
fn quote(s: String): String
```

`s` as an eclang (and C) string literal, with quotes, and escapes for quotes, backslashes and control characters: `quote("a\"b")` is `"a\"b"`.

### <a id="toBase"></a>`toBase`

```ec
fn toBase(n: Long, base: Int): String
```

`n` written in base `base`, with a '-' when it is negative: `toBase(255, 16)` is "ff".

**Parameters**

- `base` — 2 to 36; the digits after 9 are a to z

### <a id="hex"></a>`hex`

```ec
fn hex(n: ULong): String
```

`n` in hexadecimal, in lower case and without "0x": `hex(255)` is "ff".

### <a id="parseLong"></a>`parseLong`

```ec
fn parseLong(s: String, base: Int): Long throws ParseError
```

Parses a whole number in any base, with an optional sign; for base 16, also an optional "0x". Spaces around it are allowed. `parseLong("-ff", 16)` is -255.

**Parameters**

- `base` — 2 to 36

**Throws**

- `ParseError` — when `s` isn't a number in `base`, or the number doesn't fit in a Long

### <a id="parseDouble"></a>`parseDouble`

```ec
fn parseDouble(s: String): Double throws ParseError
```

Parses a decimal number: "3.25", "-1e9", "inf", "nan". Spaces around it are allowed.

**Throws**

- `ParseError` — when `s` isn't a number

### <a id="parseBool"></a>`parseBool`

```ec
fn parseBool(s: String): Bool throws ParseError
```

Parses a truth value, in any case: "true", "yes", "on" and "1" are true; "false", "no", "off" and "0" are false.

**Throws**

- `ParseError` — when `s` is none of them

### <a id="charCount"></a>`charCount`

```ec
fn charCount(s: String): Size
```

The number of Unicode characters (code points) in `s`, which must be valid UTF-8 (see [`isUtf8`](#isUtf8)); `s.len` counts bytes.

### <a id="isUtf8"></a>`isUtf8`

```ec
fn isUtf8(s: String): Bool
```

Whether `s` is valid UTF-8: no broken sequences, overlong forms or surrogates.

### <a id="stringBuilder"></a>`stringBuilder`

```ec
fn stringBuilder(capacity: Size = 16): StringBuilder
```

A new [`StringBuilder`](#StringBuilder), in the current pool.

**Parameters**

- `capacity` — how many bytes it has room for before it grows

### `stringBuilder`

```ec
fn stringBuilder(mem: Pool, capacity: Size): StringBuilder
```

A new [`StringBuilder`](#StringBuilder), in the pool `mem`.

**Parameters**

- `mem` — the pool for its memory and the String it builds
- `capacity` — how many bytes it has room for before it grows

### <a id="reserve"></a>`reserve`

```ec
fn reserve(b: StringBuilder, n: Size)
```

Makes room for `n` more bytes, so that appending them doesn't grow the buffer again.

### <a id="append"></a>`append`

```ec
fn append(b: StringBuilder, s: String)
```

Appends `s`.

### `append`

```ec
fn append(b: StringBuilder, c: Char)
```

Appends the character `c`.

### `append`

```ec
fn append(b: StringBuilder, s: CString)
```

Appends the C string `s`.

### `append`

```ec
fn append(b: StringBuilder, n: Long)
```

Appends `n` in decimal.

### `append`

```ec
fn append(b: StringBuilder, x: Double)
```

Appends `x`, written as `"${x}"` writes it.

### <a id="appendLine"></a>`appendLine`

```ec
fn appendLine(b: StringBuilder, s: String)
```

Appends `s` and a line break.

### <a id="clear"></a>`clear`

```ec
fn clear(b: StringBuilder)
```

Starts again from an empty String. Strings that [`build`](#build) gave before stay as they were.

### <a id="build"></a>`build`

```ec
fn build(b: StringBuilder): String
```

The String built so far, without copying it: appending more later doesn't change it.
