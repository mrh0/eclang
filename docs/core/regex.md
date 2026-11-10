# Module `regex`

Regular expressions: matching, capturing, replacing and splitting. The engine runs in linear time, so no pattern,
however bad, can hang a program.

    use "core:regex.ec"
    val date = try regex.compile("(\\d{4})-(\\d{2})-(?P<day>\\d{2})")
    io.log(regex.matches(date, "2024-02-29"))           // the whole text: true
    io.log(regex.contains(date, "on 2024-02-29!"))      // somewhere in it: true

    if val m = regex.find(date, "on 2024-02-29!") do
        io.log(regex.text(m))                           // 2024-02-29
        io.log(regex.group(m, 1) ?? "")                 // 2024
        io.log(regex.group(m, "day") ?? "")             // 29
        io.log(regex.start(m))                          // 3

    for m in regex.findAll(try regex.compile("\\w+"), "two words") do
        io.log(regex.text(m))

    val swapped = try regex.replaceAll(date, text, "$3/$2/$1")
    val parts = regex.split(try regex.compile(",\\s*"), "a, b,c")
    val quiet = try regex.compile("hello", ignoreCase = true)
    val exact = try regex.compile(regex.escape(userText))

What a pattern can hold:

- `.` any character but a line break; `\d \w \s` digits, word characters, white space, and `\D \W \S` the opposite (ASCII)
- `[abc]`, `[a-z]`, `[^abc]`, with escapes and `[:alpha:]`-style classes inside; `\t \n \r \f \v \a`, `\xHH`, `\x{H...}`
- `a|b`, `(...)`, `(?:...)` without capturing, `(?P<name>...)` or `(?<name>...)` with a name
- `* + ?`, `{n}`, `{n,}`, `{n,m}`, each with `?` after it to take as little as possible
- `^ $` the start and end of the text (or of a line, with `multiline`), `\A \z` of the text always, `\b \B` word edges
- `(?i)`, `(?m)`, `(?s)`, for `ignoreCase`, `multiline` and `dotAll`, also as `(?i:...)` for a part, and `(?-i)` to end one

There are no backreferences and no look-ahead or look-behind, because those are what makes other engines take
exponential time. A pattern goes through the text once, keeping track of every way it could match at once: at worst the
time is the size of the pattern times the length of the text. The match found is the one Perl or Python would find: the
leftmost, preferring what comes first in `a|b`, with repeats greedy unless they say otherwise. The exception is a loop
around something that can match nothing, as in `(\b|a)+`, which may go round once more or less, as in Go and RE2.

Text is UTF-8, and a character is a code point: `.` takes all of "é", and `[à-ÿ]` works. A byte that isn't valid
UTF-8 counts as U+FFFD. Positions are bytes. `ignoreCase` and the classes `\d \w \s \b` are ASCII only, since there are no
Unicode tables here. A pattern is limited in size: [`compile`](#regex.compile) refuses one that would be too large.

A [`Regex`](#regex.Regex) doesn't change once it is made, so threads can share it. A match shares the memory of the text.
What is made is allocated in the current pool.

## Types

### <a id="regex.Regex"></a>`Regex`

```ec
ref rec Regex
```

A compiled pattern: [`compile`](#regex.compile).

**Fields**

- <a id="regex.Regex.pattern"></a>`pattern: String` — The pattern, as it was written.
- <a id="regex.Regex.groups"></a>`groups: Size` — How many groups capture, not counting the whole match.
- <a id="regex.Regex.names"></a>`names: String[]` — The name of each group, "" for none. Group 0 is the whole match.
- <a id="regex.Regex.program"></a>`program: Inst[]` — The steps of the pattern.
- <a id="regex.Regex.sets"></a>`sets: CharSet[]` — The sets of characters that `program` refers to.
- <a id="regex.Regex.firstByte"></a>`firstByte: Int` — The first byte of every match, or -1 when there is no such thing.
- <a id="regex.Regex.anchored"></a>`anchored: Bool` — Whether a match can only start at the start of the text.

### <a id="regex.Match"></a>`Match`

```ec
ref rec Match
```

Where a match is and what it captured: [`find`](#regex.find).

**Fields**

- <a id="regex.Match.input"></a>`input: String` — The text it is in.
- <a id="regex.Match.re"></a>`re: Regex` — The pattern.
- <a id="regex.Match.spans"></a>`spans: Size[]` — Where each group starts and ends, in turn, from group 0, the whole match.

## Functions

### <a id="regex.compile"></a>`compile`

```ec
fn compile(pattern: String, ignoreCase: Bool = false, multiline: Bool = false, dotAll: Bool = false): Regex throws ParseError
```

Compiles a pattern; the header of this file says what a pattern can hold. Compile it once, and use it many times.

**Parameters**

- `pattern` — the pattern. In an eclang string a backslash is written twice: "\\d+"
- `ignoreCase` — whether letters match in either case, as `(?i)` says; ASCII letters only
- `multiline` — whether `^` and `$` also match at the start and end of each line, as `(?m)` says
- `dotAll` — whether `.` also takes "\n", as `(?s)` says

**Returns** the compiled pattern, in the current pool

**Throws**

- `ParseError` — when the pattern isn't valid, isn't UTF-8, or is too large, with the position of the problem

### <a id="regex.matches"></a>`matches`

```ec
fn matches(re: Regex, s: String): Bool
```

Whether all of `s` matches: `regex.matches(digits, "123")`.

### <a id="regex.contains"></a>`contains`

```ec
fn contains(re: Regex, s: String): Bool
```

Whether a part of `s` matches, which takes less work than [`find`](#regex.find).

### <a id="regex.find"></a>`find`

```ec
fn find(re: Regex, s: String, from: Size = 0): Match?
```

Finds the first match.

**Parameters**

- `re` — the pattern
- `s` — the text
- `from` — the byte to start looking at. What comes before it still counts for `^` and `\b`

**Returns** the match, or null when there is none

### <a id="regex.findAll"></a>`findAll`

```ec
fn findAll(re: Regex, s: String): (Match)[]
```

All the matches in `s`, one after the other, not overlapping. A match can be empty; one right after another match doesn't count.

**Parameters**

- `re` — the pattern
- `s` — the text

**Returns** the matches, in order

### <a id="regex.start"></a>`start`

```ec
fn start(m: Match): Size
```

Where the whole match starts.

### <a id="regex.end"></a>`end`

```ec
fn end(m: Match): Size
```

Where the whole match ends: the byte after it.

### <a id="regex.text"></a>`text`

```ec
fn text(m: Match): String
```

What the whole match took. It shares the memory of the text.

### <a id="regex.span"></a>`span`

```ec
fn span(m: Match, i: Size): (Size, Size)?
```

Where group `i` is.

**Returns** the byte where it starts and the byte after it; null when it took no part in the match, or there is no such group

### <a id="regex.group"></a>`group`

```ec
fn group(m: Match, i: Size): String?
```

What group `i` took. Groups count their `(` from 1, and group 0 is the whole match.

**Returns** what it took, sharing the memory of the text; null when it took no part in the match, or there is no such group

### <a id="regex.groupIndex"></a>`groupIndex`

```ec
fn groupIndex(re: Regex, name: String): Size?
```

The number of the group called `name`, or null when there is none.

### `group`

```ec
fn group(m: Match, name: String): String?
```

What the group called `name` took.

**Returns** what it took, sharing the memory of the text; null when it took no part in the match, or there is no such group

### <a id="regex.replaceAll"></a>`replaceAll`

```ec
fn replaceAll(re: Regex, s: String, template: String): String throws ArgumentError
```

`s` with every match replaced: `regex.replaceAll(words, "a b", "<$0>")` is "<a> <b>".

**Parameters**

- `re` — the pattern
- `s` — the text
- `template` — the replacement: `$1` or `${1}` for what a group took, with `$0` the whole match, `${name}` for a named
  group, and `$$` for a `$`. A group that took no part gives nothing. In an eclang string, `${` is written `\${`

**Throws**

- `ArgumentError` — when the template has a `$` that doesn't fit, or refers to a group the pattern doesn't have

### <a id="regex.replaceFirst"></a>`replaceFirst`

```ec
fn replaceFirst(re: Regex, s: String, template: String): String throws ArgumentError
```

`s` with the first match replaced.

**Parameters**

- `re` — the pattern
- `s` — the text
- `template` — the replacement, as in [`replaceAll`](#regex.replaceAll)

**Throws**

- `ArgumentError` — when the template has a `$` that doesn't fit, or refers to a group the pattern doesn't have

### <a id="regex.replaceWith"></a>`replaceWith`

```ec
fn replaceWith(re: Regex, s: String, f: (Match) => String): String
```

`s` with every match replaced by what a function makes of it, for when a template isn't enough.

**Parameters**

- `re` — the pattern
- `s` — the text
- `f` — what makes the replacement of a match

### <a id="regex.split"></a>`split`

```ec
fn split(re: Regex, s: String): String[]
```

The parts of `s` between the matches: `regex.split(commas, "a, b,c")` is ["a", "b", "c"]. They share the memory of `s`.
A pattern that matches the empty text splits between characters.

### <a id="regex.escape"></a>`escape`

```ec
fn escape(s: String): String
```

`s` with a backslash before everything a pattern gives a meaning to, so that it matches `s` itself.
