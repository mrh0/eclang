/**
 * Regular expressions: matching, capturing, replacing and splitting. The engine runs in linear time, so no pattern,
 * however bad, can hang a program.
 *
 *     use "core:regex.ec"
 *     val date = try regex.compile("(\\d{4})-(\\d{2})-(?P<day>\\d{2})")
 *     io.log(regex.matches(date, "2024-02-29"))           // the whole text: true
 *     io.log(regex.contains(date, "on 2024-02-29!"))      // somewhere in it: true
 *
 *     if val m = regex.find(date, "on 2024-02-29!") do
 *         io.log(regex.text(m))                           // 2024-02-29
 *         io.log(regex.group(m, 1) ?? "")                 // 2024
 *         io.log(regex.group(m, "day") ?? "")             // 29
 *         io.log(regex.start(m))                          // 3
 *
 *     for m in regex.findAll(try regex.compile("\\w+"), "two words") do
 *         io.log(regex.text(m))
 *
 *     val swapped = try regex.replaceAll(date, text, "$3/$2/$1")
 *     val parts = regex.split(try regex.compile(",\\s*"), "a, b,c")
 *     val quiet = try regex.compile("hello", ignoreCase = true)
 *     val exact = try regex.compile(regex.escape(userText))
 *
 * What a pattern can hold:
 *
 * - `.` any character but a line break; `\d \w \s` digits, word characters, white space, and `\D \W \S` the opposite (ASCII)
 * - `[abc]`, `[a-z]`, `[^abc]`, with escapes and `[:alpha:]`-style classes inside; `\t \n \r \f \v \a`, `\xHH`, `\x{H...}`
 * - `a|b`, `(...)`, `(?:...)` without capturing, `(?P<name>...)` or `(?<name>...)` with a name
 * - `* + ?`, `{n}`, `{n,}`, `{n,m}`, each with `?` after it to take as little as possible
 * - `^ $` the start and end of the text (or of a line, with `multiline`), `\A \z` of the text always, `\b \B` word edges
 * - `(?i)`, `(?m)`, `(?s)`, for `ignoreCase`, `multiline` and `dotAll`, also as `(?i:...)` for a part, and `(?-i)` to end one
 *
 * There are no backreferences and no look-ahead or look-behind, because those are what makes other engines take
 * exponential time. A pattern goes through the text once, keeping track of every way it could match at once: at worst the
 * time is the size of the pattern times the length of the text. The match found is the one Perl or Python would find: the
 * leftmost, preferring what comes first in `a|b`, with repeats greedy unless they say otherwise. The exception is a loop
 * around something that can match nothing, as in `(\b|a)+`, which may go round once more or less, as in Go and RE2.
 *
 * Text is UTF-8, and a character is a code point: `.` takes all of "é", and `[à-ÿ]` works. A byte that isn't valid
 * UTF-8 counts as U+FFFD. Positions are bytes. `ignoreCase` and the classes `\d \w \s \b` are ASCII only, since there are no
 * Unicode tables here. A pattern is limited in size: {@link compile} refuses one that would be too large.
 *
 * A {@link Regex} doesn't change once it is made, so threads can share it. A match shares the memory of the text.
 * What is made is allocated in the current pool.
 */
module "regex"
use "core:errors.ec"
use "core:string.ec"
use "core:array.ec"
use "core:structures.ec"
use "core:unicode.ec"

// ---------------------------------------------------------------------------------------------------
// What a compiled pattern is made of

/** The highest count in `{n,m}`. */
priv const MAX_REPEAT: Int = 1000
/** The most instructions a pattern can compile to. */
priv const MAX_PROGRAM: Size = 100000
/** How deeply groups can be nested. */
priv const MAX_DEPTH: Int = 500
/** The most memory for captures, as instructions times capture slots. */
priv const MAX_SLOTS: Size = 4000000
/** Stands for a capture that didn't take part in the match. */
priv const UNSET: Size = 0x7FFFFFFFFFFFFFFF

/** The instruction takes the character `c`. */
priv const OP_CHAR = 0
/** The instruction takes any character. */
priv const OP_ANY = 1
/** The instruction takes any character but "\n". */
priv const OP_ANY_NOT_NL = 2
/** The instruction takes a character of the set `x`. */
priv const OP_SET = 3
/** The instruction goes on at `x`, or else at `y`. */
priv const OP_SPLIT = 4
/** The instruction goes on at `x`. */
priv const OP_JUMP = 5
/** The instruction notes the position in the capture slot `x`. */
priv const OP_SAVE = 6
/** The instruction goes on when the assertion `x` holds. */
priv const OP_ASSERT = 7
/** The instruction ends a match. */
priv const OP_MATCH = 8

/** An assertion: the start of the text. */
priv const AT_TEXT_START: Size = 0
/** An assertion: the end of the text. */
priv const AT_TEXT_END: Size = 1
/** An assertion: the start of a line. */
priv const AT_LINE_START: Size = 2
/** An assertion: the end of a line. */
priv const AT_LINE_END: Size = 3
/** An assertion: the edge of a word. */
priv const AT_WORD_EDGE: Size = 4
/** An assertion: not the edge of a word. */
priv const AT_NOT_WORD_EDGE: Size = 5

/** One step of a compiled pattern. */
priv rec Inst as
    /** What it does: one of the `OP_*` constants. */
    op: Int
    /** The character it takes. */
    c: UInt = 0
    /** See the `OP_*` constants. */
    x: Size = 0
    /** See the `OP_*` constants. */
    y: Size = 0

/** A set of characters. */
priv rec CharSet as
    /** The ranges, as first and last character in turn, in order, not touching. */
    ranges: UInt[]
    /** Whether each ASCII character is in it. */
    ascii: Bool[]

/** A compiled pattern: {@link compile}. */
rec Regex as
    /** The pattern, as it was written. */
    pattern: String
    /** How many groups capture, not counting the whole match. */
    groups: Size
    /** The name of each group, "" for none. Group 0 is the whole match. */
    names: String[]
    /** The steps of the pattern. */
    program: Inst[]
    /** The sets of characters that {@code program} refers to. */
    sets: CharSet[]
    /** The first byte of every match, or -1 when there is no such thing. */
    firstByte: Int
    /** Whether a match can only start at the start of the text. */
    anchored: Bool

// ---------------------------------------------------------------------------------------------------
// Parsing

/** A node is nothing: it matches the empty text. */
priv const NODE_EMPTY = 0
/** A node takes the character `c`. */
priv const NODE_CHAR = 1
/** A node takes any character. */
priv const NODE_ANY = 2
/** A node takes any character but "\n". */
priv const NODE_ANY_NOT_NL = 3
/** A node takes a character of the set `value`. */
priv const NODE_SET = 4
/** A node is its `kids`, one after the other. */
priv const NODE_SEQUENCE = 5
/** A node is one of its `kids`, in the order of preference. */
priv const NODE_CHOICE = 6
/** A node is its first kid, `min` to `max` times; `max` is -1 for no limit. */
priv const NODE_REPEAT = 7
/** A node is its first kid, captured as group `value`. */
priv const NODE_GROUP = 8
/** A node holds when the assertion `value` does. */
priv const NODE_ASSERT = 9

/** A part of a parsed pattern. */
priv rec Node as
    /** What it is: one of the `NODE_*` constants. */
    kind: Int
    /** See the `NODE_*` constants. */
    c: UInt = 0
    /** See the `NODE_*` constants. */
    value: Size = 0
    /** See the `NODE_*` constants. */
    kids: @List<@Node>? = null
    /** See the `NODE_*` constants. */
    min: Int = 0
    /** See the `NODE_*` constants. */
    max: Int = 0
    /** Whether a repeat takes as much as it can. */
    greedy: Bool = true

/** A range of characters, while a set is put together. */
priv rec Range as
    /** The first character. */
    first: UInt
    /** The last character. */
    last: UInt

/** Where parsing is. */
priv rec Parser as
    /** The pattern. */
    text: String
    /** The next byte. */
    at: Size
    /** Whether letters match in either case from here on. */
    ignoreCase: Bool
    /** Whether `^` and `$` are about lines from here on. */
    multiline: Bool
    /** Whether `.` takes "\n" too from here on. */
    dotAll: Bool
    /** The name of each group so far, "" for none. */
    names: @List<String>
    /** The sets of characters so far. */
    sets: @List<CharSet>

/** The error for what is at byte `at` of the pattern. */
priv fn problem(p: @Parser, message: String, at: Size): ParseError = parseError("in the pattern at byte ${at}: ${message}", p.text, at)

/** Whether the pattern has ended. */
priv fn atEnd(p: @Parser): Bool = p.at >= p.text.len

/** Whether the pattern goes on with `s` here. */
priv fn looksAt(p: @Parser, s: String): Bool = p.at + s.len <= p.text.len and p.text[p.at..<p.at + s.len] == s

/** Whether the pattern goes on with `c` here. */
priv fn looksAt(p: @Parser, c: Char): Bool = p.at < p.text.len and p.text[p.at] == c

/** Goes past `s` if the pattern goes on with it here, and says so. */
priv fn takes(p: @Parser, s: String): Bool do
    ret false if not looksAt(p, s)
    p.at += s.len
    ret true

/** Takes the code point at the current byte, which must exist. */
priv fn takeChar(p: @Parser): UInt do
    val (cp, n) = unicode.decode(p.text, p.at)
    p.at += n
    ret cp

/** The node for an empty part of a pattern. */
priv fn emptyNode(): @Node = new Node { kind = NODE_EMPTY }

/** A list of the one node `n`. */
priv fn only(n: @Node): @List<@Node> do
    val kids: @List<@Node> = list(1)
    kids.push(n)
    ret kids

/** Adds the ranges of the other case of what is between `first` and `last`, which are in `from` to `until`. */
priv fn addOtherCase(ranges: @List<Range>, first: UInt, last: UInt, from: UInt, until: UInt, target: UInt) do
    val low = if first < from do from else first
    val high = if last > until do until else last
    ranges.push(Range { low - from + target, high - from + target }) if low <= high

/** Adds the characters from `first` to `last`, and when letters match in either case, those in the other case. */
priv fn addRange(p: @Parser, ranges: @List<Range>, first: UInt, last: UInt) do
    ranges.push(Range { first, last })
    if p.ignoreCase do
        addOtherCase(ranges, first, last, 'A' as UInt, 'Z' as UInt, 'a' as UInt)
        addOtherCase(ranges, first, last, 'a' as UInt, 'z' as UInt, 'A' as UInt)

/** Whether `a` comes before `b`. */
priv fn rangeBefore(a: Range, b: Range): Bool = a.first < b.first

/** The ranges, sorted and joined where they touch. */
priv fn normalised(ranges: @List<Range>): @List<Range> do
    sortBy(ranges.items(), fn rangeBefore)
    val out: @List<Range> = list(ranges.len + 1)
    for r in ranges do
        if out.len > 0 and r.first <= out.buffer[out.len - 1].last + 1 do
            if r.last > out.buffer[out.len - 1].last do
                out.buffer[out.len - 1].last = r.last
        else do
            out.push(r)
    ret out

/** What isn't in the ranges, which are normalised. */
priv fn inverted(ranges: @List<Range>): @List<Range> do
    val out: @List<Range> = list(ranges.len + 2)
    var from: UInt = 0
    for r in ranges do
        out.push(Range { from, r.first - 1 }) if r.first > from
        from = r.last + 1
    out.push(Range { from, unicode.MAX }) if from <= unicode.MAX
    ret out

/** Adds a set of `ranges` to the pattern. */
priv fn makeSet(p: @Parser, ranges: @List<Range>, negated: Bool): @Node do
    val sorted = normalised(ranges)
    val final = if negated do inverted(sorted) else sorted
    val flat = new UInt[final.len * 2]
    val ascii = new Bool[128]
    for i, r in final.items() do
        flat[i * 2] = r.first
        flat[i * 2 + 1] = r.last
        var c = r.first
        while c <= r.last and c < 128 do
            ascii[c as Size] = true
            c += 1
    p.sets.push(CharSet { flat, ascii })
    ret new Node { kind = NODE_SET, value = p.sets.len - 1 }

/** Adds the characters of `\d`, `\w` or `\s`, with `c` the letter in lower case. */
priv fn addShorthand(ranges: @List<Range>, c: Char) do
    when c do
        'd' do
            ranges.push(Range { '0' as UInt, '9' as UInt })
        'w' do
            ranges.push(Range { '0' as UInt, '9' as UInt })
            ranges.push(Range { 'A' as UInt, 'Z' as UInt })
            ranges.push(Range { '_' as UInt, '_' as UInt })
            ranges.push(Range { 'a' as UInt, 'z' as UInt })
        else do
            ranges.push(Range { 9, 13 })
            ranges.push(Range { 32, 32 })

/** Whether `c` names `\d`, `\w`, `\s` or their opposites. */
priv fn isShorthand(c: Char): Bool = c == 'd' or c == 'w' or c == 's' or c == 'D' or c == 'W' or c == 'S'

/** Adds what a shorthand stands for to a set. */
priv fn addShorthandTo(ranges: @List<Range>, c: Char) do
    if isLower(c) do
        addShorthand(ranges, c)
    else do
        val opposite: @List<Range> = list()
        addShorthand(opposite, toLower(c))
        ranges.pushAll(inverted(normalised(opposite)).items())

/**
 * Reads `count` hexadecimal digits at the current byte.
 * @throws when they aren't there
 */
priv fn hexDigits(p: @Parser, count: Size, start: Size): UInt throws ParseError do
    var value: UInt = 0
    for i in 0..<count do
        throw problem(p, "\\x needs two hexadecimal digits, or some in braces: \\x41, \\x{1F600}", start) if atEnd(p) or not isHexDigit(p.text[p.at])
        value = value * 16 + ((digitValue(p.text[p.at]) ?? 0) as UInt)
        p.at += 1
    ret value

/**
 * Reads what is after `\x`.
 * @throws when it isn't valid
 */
priv fn hexEscape(p: @Parser, start: Size): UInt throws ParseError do
    ret try hexDigits(p, 2, start) if not takes(p, "{")
    var value: UInt = 0
    var digits: Size = 0
    while not takes(p, "}") do
        throw problem(p, "\\x{ has no closing }", start) if atEnd(p)
        throw problem(p, "\\x{...} takes hexadecimal digits", p.at) if not isHexDigit(p.text[p.at])
        value = value * 16 + ((digitValue(p.text[p.at]) ?? 0) as UInt)
        throw problem(p, "that is above U+10FFFF, the last code point", start) if value > unicode.MAX
        p.at += 1
        digits += 1
    throw problem(p, "\\x{} needs hexadecimal digits", start) if digits == 0
    ret value

/**
 * Reads the character an escape stands for, at the byte after the backslash, which must exist.
 * @throws when it doesn't stand for one
 */
priv fn escapedChar(p: @Parser): UInt throws ParseError do
    val start = p.at - 1
    val c = p.text[p.at]
    if c >= 0 and not isAlnum(c) do
        ret takeChar(p)
    p.at += 1
    when c do
        't' do ret 9
        'n' do ret 10
        'v' do ret 11
        'f' do ret 12
        'r' do ret 13
        'a' do ret 7
        'x' do ret try hexEscape(p, start)
        '0' do ret 0
        'p', 'P' do throw problem(p, "\\${c} needs Unicode tables, and there are none: write the ranges in a [...]", start)
        '1', '2', '3', '4', '5', '6', '7', '8', '9' do throw problem(p, "backreferences aren't supported: they can't be matched in linear time", start)
        else do throw problem(p, "there is no escape \\${c}. A backslash before a letter or digit has a meaning of its own; there are \\d \\w \\s \\b \\t \\n and others", start)

/**
 * Reads a `[:name:]` inside a set, at the current byte, its "[:".
 * @throws when there is no such class
 */
priv fn posixClass(p: @Parser, ranges: @List<Range>) throws ParseError do
    val start = p.at
    p.at += 2
    val negated = takes(p, "^")
    val at = p.at
    while not atEnd(p) and isAlpha(p.text[p.at]) do
        p.at += 1
    val name = p.text[at..<p.at]
    throw problem(p, "a [: class ends with :]", start) if not takes(p, ":]")
    val found: @List<Range> = list()
    when name do
        "alpha" do
            addRange(p, found, 'A' as UInt, 'Z' as UInt)
            addRange(p, found, 'a' as UInt, 'z' as UInt)
        "digit" do addRange(p, found, '0' as UInt, '9' as UInt)
        "alnum" do
            addRange(p, found, '0' as UInt, '9' as UInt)
            addRange(p, found, 'A' as UInt, 'Z' as UInt)
            addRange(p, found, 'a' as UInt, 'z' as UInt)
        "upper" do addRange(p, found, 'A' as UInt, 'Z' as UInt)
        "lower" do addRange(p, found, 'a' as UInt, 'z' as UInt)
        "space" do addShorthand(found, 's')
        "blank" do
            addRange(p, found, 9, 9)
            addRange(p, found, 32, 32)
        "punct" do
            addRange(p, found, 33, 47)
            addRange(p, found, 58, 64)
            addRange(p, found, 91, 96)
            addRange(p, found, 123, 126)
        "xdigit" do
            addRange(p, found, '0' as UInt, '9' as UInt)
            addRange(p, found, 'A' as UInt, 'F' as UInt)
            addRange(p, found, 'a' as UInt, 'f' as UInt)
        "word" do addShorthand(found, 'w')
        "cntrl" do
            addRange(p, found, 0, 31)
            addRange(p, found, 127, 127)
        "print" do addRange(p, found, 32, 126)
        "graph" do addRange(p, found, 33, 126)
        "ascii" do addRange(p, found, 0, 127)
        else do throw problem(p, "there is no class [:${name}:]", start)
    ranges.pushAll((if negated do inverted(normalised(found)) else found).items())

/**
 * Reads one end of a range in a set, at the current byte, which must exist.
 * @return the character, or null when a `\d`-style escape was added to `ranges` instead
 * @throws when it isn't valid
 */
priv fn setChar(p: @Parser, ranges: @List<Range>): UInt? throws ParseError do
    ret takeChar(p) if not looksAt(p, '\\')
    p.at += 1
    throw problem(p, "a backslash at the end of the pattern has nothing to escape", p.at - 1) if atEnd(p)
    val c = p.text[p.at]
    if isShorthand(c) do
        p.at += 1
        addShorthandTo(ranges, c)
        ret null
    if c == 'b' do
        p.at += 1
        ret 8
    ret try escapedChar(p)

/**
 * Reads a set of characters, at the byte after its `[`.
 * @throws when it isn't valid
 */
priv fn parseSet(p: @Parser): @Node throws ParseError do
    val start = p.at - 1
    val negated = takes(p, "^")
    val ranges: @List<Range> = list()
    var first = true
    while first or not looksAt(p, ']') do
        throw problem(p, "this [ has no closing ]", start) if atEnd(p)
        first = false
        if looksAt(p, "[:") do
            try posixClass(p, ranges)
        else do
            val at = p.at
            if val low = try setChar(p, ranges) do
                if looksAt(p, '-') and p.at + 1 < p.text.len and p.text[p.at + 1] != ']' do
                    p.at += 1
                    val high = try setChar(p, ranges)
                    throw problem(p, "a range in a set needs a character at each end; write a plain - as \\-", at) if high == null
                    throw problem(p, "this range is backwards: its first character comes after the last", at) if high < low
                    addRange(p, ranges, low, high)
                else do
                    addRange(p, ranges, low, low)
    p.at += 1
    ret makeSet(p, ranges, negated)

/** The node for the character `cp`. Letters that match in either case are a set of both. */
priv fn charNode(p: @Parser, cp: UInt): @Node do
    if p.ignoreCase and cp < 128 and isAlpha(cp as Char) do
        val ranges: @List<Range> = list(2)
        addRange(p, ranges, cp, cp)
        ret makeSet(p, ranges, false)
    ret new Node { kind = NODE_CHAR, c = cp }

/** The node for an assertion. */
priv fn assertNode(what: Size): @Node = new Node { kind = NODE_ASSERT, value = what }

/**
 * Reads an escape outside a set, at the byte after the backslash.
 * @throws when it isn't valid
 */
priv fn parseEscape(p: @Parser): @Node throws ParseError do
    throw problem(p, "a backslash at the end of the pattern has nothing to escape", p.at - 1) if atEnd(p)
    val c = p.text[p.at]
    if isShorthand(c) do
        p.at += 1
        val ranges: @List<Range> = list()
        addShorthandTo(ranges, c)
        ret makeSet(p, ranges, false)
    when c do
        'b' do
            p.at += 1
            ret assertNode(AT_WORD_EDGE)
        'B' do
            p.at += 1
            ret assertNode(AT_NOT_WORD_EDGE)
        'A' do
            p.at += 1
            ret assertNode(AT_TEXT_START)
        'z' do
            p.at += 1
            ret assertNode(AT_TEXT_END)
        'Z' do throw problem(p, "\\Z isn't supported: use \\z for the end of the text", p.at - 1)
        else do
            val cp = try escapedChar(p)
            ret charNode(p, cp)

/**
 * Reads the flags of `(?i)` or `(?i-m:`, at the first letter.
 * @return whether a group follows, after the ":"
 * @throws when they aren't valid
 */
priv fn parseFlags(p: @Parser, start: Size): Bool throws ParseError do
    var turnOn = true
    while true do
        throw problem(p, "this group has no closing )", start) if atEnd(p)
        val c = p.text[p.at]
        p.at += 1
        when c do
            'i' do p.ignoreCase = turnOn
            'm' do p.multiline = turnOn
            's' do p.dotAll = turnOn
            '-' do turnOn = false
            ':' do ret true
            ')' do ret false
            else do throw problem(p, "'${c}' isn't a flag: the flags are i, m and s", p.at - 1)

/**
 * Reads the name of a group, at the byte after its `<`, and its `>`.
 * @throws when it isn't valid
 */
priv fn groupName(p: @Parser, start: Size): String throws ParseError do
    val at = p.at
    while not atEnd(p) and (isAlnum(p.text[p.at]) or p.text[p.at] == '_') do
        p.at += 1
    val name = p.text[at..<p.at]
    throw problem(p, "the name of a group is letters, digits and _, and ends with >", start) if not takes(p, ">") or name.len == 0
    throw problem(p, "the name of a group can't start with a digit", start) if isDigit(name[0])
    for other in p.names do
        throw problem(p, "there are two groups called '${name}'", start) if other == name
    ret name

/**
 * Reads a group, at the byte after its `(`.
 * @throws when it isn't valid
 */
priv fn parseGroup(p: @Parser, depth: Int): @Node throws ParseError do
    val start = p.at - 1
    throw problem(p, "the groups are nested too deeply", start) if depth >= MAX_DEPTH
    val ignoreCase = p.ignoreCase
    val multiline = p.multiline
    val dotAll = p.dotAll
    var index: Size = 0
    if takes(p, "?") do
        throw problem(p, "look-ahead isn't supported: it can't be matched in linear time", start) if looksAt(p, '=') or looksAt(p, '!')
        throw problem(p, "look-behind isn't supported: it can't be matched in linear time", start) if looksAt(p, "<=") or looksAt(p, "<!")
        throw problem(p, "atomic groups aren't supported: a plain (...) matches the same or more", start) if looksAt(p, '>')
        if takes(p, "P<") or takes(p, "<") do
            val name = try groupName(p, start)
            index = p.names.len
            p.names.push(name)
        else if not takes(p, ":") do
            // Without a group, flags go on to the end of the group around them.
            ret emptyNode() if not try parseFlags(p, start)
    else do
        index = p.names.len
        p.names.push("")
    val body = try parseChoice(p, depth + 1)
    throw problem(p, "this ( has no closing )", start) if not takes(p, ")")
    p.ignoreCase = ignoreCase
    p.multiline = multiline
    p.dotAll = dotAll
    ret body if index == 0
    ret new Node { kind = NODE_GROUP, value = index, kids = only(body) }

/**
 * Reads a `{n}`, `{n,}` or `{n,m}` at the current byte, a `{`.
 * @return the counts, with -1 for no limit, and `p` past the `}`; null when this isn't one, and then a `{` is an ordinary character
 * @throws when the counts are out of range
 */
priv fn parseCounts(p: @Parser): (Int, Int)? throws ParseError do
    val start = p.at
    var at = p.at + 1
    var min = -1
    var max = -1
    var number = -1
    var comma = false
    while at < p.text.len and p.text[at] != '}' do
        val c = p.text[at]
        if isDigit(c) do
            number = (if number < 0 do 0 else number) * 10 + ((c - '0') as Int)
            throw problem(p, "a repeat can count to ${MAX_REPEAT} at most", start) if number > MAX_REPEAT
        else if c == ',' and not comma do
            comma = true
            min = number
            number = -1
        else do
            ret null
        at += 1
    ret null if at >= p.text.len
    if comma do
        max = number
    else do
        min = number
        max = number
    ret null if min < 0
    throw problem(p, "the counts of this repeat are backwards: {${min},${max}}", start) if max >= 0 and max < min
    p.at = at + 1
    ret (min, max)

/**
 * Reads what is at the current byte, up to any repeat after it.
 * @throws when it isn't valid
 */
priv fn parseAtom(p: @Parser, depth: Int): @Node throws ParseError do
    val start = p.at
    val c = p.text[p.at]
    when c do
        '(' do
            p.at += 1
            ret try parseGroup(p, depth)
        '[' do
            p.at += 1
            ret try parseSet(p)
        '.' do
            p.at += 1
            ret new Node { kind = if p.dotAll do NODE_ANY else NODE_ANY_NOT_NL }
        '^' do
            p.at += 1
            ret assertNode(if p.multiline do AT_LINE_START else AT_TEXT_START)
        '$' do
            p.at += 1
            ret assertNode(if p.multiline do AT_LINE_END else AT_TEXT_END)
        '\\' do
            p.at += 1
            ret try parseEscape(p)
        '*', '+', '?' do throw problem(p, "this ${c} has nothing before it to repeat. Write \\${c} for the character itself", start)
        '{' do
            val counts = try parseCounts(p)
            throw problem(p, "this repeat has nothing before it. Write \\{ for the character itself", start) if counts != null
            p.at += 1
            ret charNode(p, '{' as UInt)
        else do
            val cp = takeChar(p)
            ret charNode(p, cp)

/**
 * Reads an atom and the repeat after it, if any.
 * @throws when it isn't valid
 */
priv fn parseRepeat(p: @Parser, depth: Int): @Node throws ParseError do
    val atom = try parseAtom(p, depth)
    ret atom if atEnd(p)
    val start = p.at
    var min = 0
    var max = -1
    when p.text[p.at] do
        '*' do p.at += 1
        '+' do
            p.at += 1
            min = 1
        '?' do
            p.at += 1
            max = 1
        '{' do
            val counts = try parseCounts(p) ?? ret atom
            min = counts.first
            max = counts.second
        else do ret atom
    val greedy = not takes(p, "?")
    if not atEnd(p) do
        val next = p.text[p.at]
        var counted = false
        if next == '{' do
            counted = (try parseCounts(p)) != null
        throw problem(p, "a repeat can't follow a repeat: put the first in a group, as in (?:a*)+", start) if next == '*' or next == '+' or next == '?' or counted
    ret new Node { kind = NODE_REPEAT, min = min, max = max, greedy = greedy, kids = only(atom) }

/**
 * Reads the parts of one alternative, up to a `|`, a `)` or the end.
 * @throws when it isn't valid
 */
priv fn parseSequence(p: @Parser, depth: Int): @Node throws ParseError do
    val kids: @List<@Node> = list()
    while not atEnd(p) and not looksAt(p, '|') and not looksAt(p, ')') do
        kids.push(try parseRepeat(p, depth))
    ret emptyNode() if kids.len == 0
    ret kids.buffer[0] if kids.len == 1
    ret new Node { kind = NODE_SEQUENCE, kids = kids }

/**
 * Reads alternatives, up to a `)` or the end.
 * @throws when they aren't valid
 */
priv fn parseChoice(p: @Parser, depth: Int): @Node throws ParseError do
    val first = try parseSequence(p, depth)
    ret first if not looksAt(p, '|')
    val kids = only(first)
    while takes(p, "|") do
        kids.push(try parseSequence(p, depth))
    ret new Node { kind = NODE_CHOICE, kids = kids }

// ---------------------------------------------------------------------------------------------------
// Compiling

/** The instructions so far. */
priv rec Builder as
    /** The pattern, for error messages. */
    pattern: String
    /** The instructions. */
    code: @List<Inst>
    /** How many nodes have been compiled. Empty ones add no instructions, but repeats of them still take time. */
    work: Size = 0

/**
 * Adds an instruction.
 * @return its index
 * @throws when the pattern has become too large
 */
priv fn add(b: @Builder, inst: Inst): Size throws ParseError do
    throw parseError("this pattern is too large: its repeats make more than ${MAX_PROGRAM} steps", b.pattern, 0) if b.code.len >= MAX_PROGRAM
    b.code.push(inst)
    ret b.code.len - 1

/** Whether `n` can match the empty text. */
priv fn nullable(n: @Node): Bool do
    when n.kind do
        NODE_EMPTY, NODE_ASSERT do ret true
        NODE_SEQUENCE do
            for kid in n.kids! do
                ret false if not nullable(kid)
            ret true
        NODE_CHOICE do
            for kid in n.kids! do
                ret true if nullable(kid)
            ret false
        NODE_REPEAT do ret n.min == 0 or nullable(n.kids!.get(0))
        NODE_GROUP do ret nullable(n.kids!.get(0))
        else do ret false

/** Points the split at `at` to the body after it and to `out`, in the order of preference. */
priv fn pointSplit(b: @Builder, at: Size, body: Size, out: Size, greedy: Bool) do
    b.code.buffer[at].x = if greedy do body else out
    b.code.buffer[at].y = if greedy do out else body

/**
 * Compiles `x+`, taking at least one.
 * @throws when the pattern has become too large
 */
priv fn emitPlus(b: @Builder, body: @Node, greedy: Bool) throws ParseError do
    val start = b.code.len
    try emit(b, body)
    val split = try add(b, Inst { op = OP_SPLIT })
    pointSplit(b, split, start, split + 1, greedy)

/**
 * Compiles `x*`.
 * @throws when the pattern has become too large
 */
priv fn emitStar(b: @Builder, body: @Node, greedy: Bool) throws ParseError do
    val split = try add(b, Inst { op = OP_SPLIT })
    if nullable(body) do
        // A loop back to the split would be a loop that takes nothing. As (x+)?, the order of preference stays right.
        try emitPlus(b, body, greedy)
    else do
        try emit(b, body)
        try add(b, Inst { op = OP_JUMP, x = split })
    pointSplit(b, split, split + 1, b.code.len, greedy)

/**
 * Compiles a repeat.
 * @throws when the pattern has become too large
 */
priv fn emitRepeat(b: @Builder, n: @Node) throws ParseError do
    val body = n.kids!.get(0)
    if n.max < 0 do
        if n.min == 0 do
            try emitStar(b, body, n.greedy)
        else do
            for i in 1..<n.min do
                try emit(b, body)
            try emitPlus(b, body, n.greedy)
    else do
        for i in 0..<n.min do
            try emit(b, body)
        // x{0,2} is (x(x)?)?: each split can go past all the rest.
        val splits: @List<Size> = list()
        for i in n.min..<n.max do
            splits.push(try add(b, Inst { op = OP_SPLIT }))
            try emit(b, body)
        for split in splits do
            pointSplit(b, split, split + 1, b.code.len, n.greedy)

/**
 * Compiles alternatives.
 * @throws when the pattern has become too large
 */
priv fn emitChoice(b: @Builder, n: @Node) throws ParseError do
    val jumps: @List<Size> = list()
    val kids = n.kids!
    for i, kid in kids.items() do
        if i + 1 < kids.len do
            val split = try add(b, Inst { op = OP_SPLIT })
            try emit(b, kid)
            jumps.push(try add(b, Inst { op = OP_JUMP }))
            pointSplit(b, split, split + 1, b.code.len, true)
        else do
            try emit(b, kid)
    for jump in jumps do
        b.code.buffer[jump].x = b.code.len

/**
 * Compiles a node.
 * @throws when the pattern has become too large
 */
priv fn emit(b: @Builder, n: @Node) throws ParseError do
    b.work += 1
    throw parseError("this pattern is too large: its repeats go too deep", b.pattern, 0) if b.work > MAX_PROGRAM * 8
    when n.kind do
        NODE_CHAR do try add(b, Inst { op = OP_CHAR, c = n.c })
        NODE_ANY do try add(b, Inst { op = OP_ANY })
        NODE_ANY_NOT_NL do try add(b, Inst { op = OP_ANY_NOT_NL })
        NODE_SET do try add(b, Inst { op = OP_SET, x = n.value })
        NODE_ASSERT do try add(b, Inst { op = OP_ASSERT, x = n.value })
        NODE_SEQUENCE do
            for kid in n.kids! do
                try emit(b, kid)
        NODE_CHOICE do try emitChoice(b, n)
        NODE_REPEAT do try emitRepeat(b, n)
        NODE_GROUP do
            try add(b, Inst { op = OP_SAVE, x = n.value * 2 })
            try emit(b, n.kids!.get(0))
            try add(b, Inst { op = OP_SAVE, x = n.value * 2 + 1 })

/** What every match of `n` starts with: a node that isn't a group, or null when that isn't one thing. */
priv fn firstNode(n: @Node): @Node? do
    when n.kind do
        NODE_GROUP do ret firstNode(n.kids!.get(0))
        NODE_REPEAT do
            ret firstNode(n.kids!.get(0)) if n.min > 0
            ret null
        NODE_SEQUENCE do
            for kid in n.kids! do
                ret firstNode(kid) if kid.kind != NODE_EMPTY
            ret null
        NODE_CHOICE do ret null
        else do ret n

/** The byte that every match of `n` starts with, or -1 when there is no such byte. */
priv fn firstByteOf(n: @Node): Int do
    if val first = firstNode(n) do
        // A byte that isn't valid UTF-8 reads as U+FFFD, so that can start with any byte.
        if first.kind == NODE_CHAR and first.c != unicode.REPLACEMENT do
            ret (unicode.encode(first.c)[0] as UChar) as Int
    ret -1

/** Whether every match of `n` has to start at the start of the text. */
priv fn isAnchored(n: @Node): Bool do
    if val first = firstNode(n) do
        ret first.kind == NODE_ASSERT and first.value == AT_TEXT_START
    ret false

/**
 * Compiles a pattern; the header of this file says what a pattern can hold. Compile it once, and use it many times.
 * @param pattern the pattern. In an eclang string a backslash is written twice: "\\d+"
 * @param ignoreCase whether letters match in either case, as `(?i)` says; ASCII letters only
 * @param multiline whether `^` and `$` also match at the start and end of each line, as `(?m)` says
 * @param dotAll whether `.` also takes "\n", as `(?s)` says
 * @return the compiled pattern, in the current pool
 * @throws when the pattern isn't valid, isn't UTF-8, or is too large, with the position of the problem
 */
fn compile(pattern: String, ignoreCase: Bool = false, multiline: Bool = false, dotAll: Bool = false): @Regex throws ParseError do
    try unicode.validate(pattern)
    val names: @List<String> = list()
    names.push("")
    val sets: @List<CharSet> = list()
    val p = new Parser { pattern, 0, ignoreCase, multiline, dotAll, names, sets }
    val root = try parseChoice(p, 0)
    throw problem(p, "this ) has no ( before it. Write \\) for the character itself", p.at) if not atEnd(p)
    val code: @List<Inst> = list(64)
    val b = new Builder { pattern = pattern, code = code }
    try add(b, Inst { op = OP_SAVE, x = 0 })
    try emit(b, root)
    try add(b, Inst { op = OP_SAVE, x = 1 })
    try add(b, Inst { op = OP_MATCH })
    throw parseError("this pattern is too large: it has ${names.len - 1} groups in ${code.len} steps", pattern, 0) if code.len * names.len * 2 > MAX_SLOTS
    ret new Regex { pattern, names.len - 1, names.items(), code.items(), sets.items(), firstByteOf(root), isAnchored(root) }

// ---------------------------------------------------------------------------------------------------
// Matching

/** A set of threads, each at an instruction, in the order of preference. */
priv rec Queue as
    /** The instruction of each thread. */
    dense: Size[]
    /** Where each instruction is in {@code dense}, if it is there: see {@link isQueued}. */
    sparse: Size[]
    /** The capture slots of each thread, one after the other. */
    captures: Size[]
    /** How many threads there are. */
    len: Size

/** A run of a pattern over a text, with all the memory it needs. */
priv rec Machine as
    /** The pattern. */
    re: @Regex
    /** The text. */
    input: String
    /** How many capture slots are followed: 0 when only whether it matches is of interest. */
    slots: Size
    /** The threads at this character. */
    now: @Queue
    /** The threads at the next character. */
    later: @Queue
    /** The capture slots of the thread that is being followed. */
    work: Size[]
    /** The instructions still to follow, or {@link RESTORE}, in which case a capture slot gets its value back. */
    stackAt: Size[]
    /** The capture slot to set back. */
    stackSlot: Size[]
    /** The value to set it to. */
    stackValue: Size[]
    /** The capture slots of the match. */
    found: Size[]

/** Stands for a capture slot getting its value back on the stack of a {@link Machine}. */
priv const RESTORE: Size = 0x7FFFFFFFFFFFFFFF

/** A new empty {@link Queue}, in pool `mem`. */
priv fn newQueue(size: Size, slots: Size, mem: Pool): @Queue = new Queue { new Size[size] in mem, new Size[size] in mem, new Size[size * slots] in mem, 0 } in mem

/** A new {@link Machine}, in pool `mem`. */
priv fn newMachine(re: @Regex, input: String, slots: Size, mem: Pool): @Machine do
    val size = re.program.len
    val depth = size * 2 + 2
    ret new Machine { re, input, slots, newQueue(size, slots, mem), newQueue(size, slots, mem), new Size[slots] in mem, new Size[depth] in mem, new Size[depth] in mem, new Size[depth] in mem, new Size[slots] in mem } in mem

/** Whether instruction `at` is in `q`. */
priv fn isQueued(q: @Queue, at: Size): Bool do
    val i = q.sparse[at]
    ret i < q.len and q.dense[i] == at

/** Whether `c` is a character of `\w`. */
priv fn isWord(c: Char): Bool = isAlnum(c) or c == '_'

/** Whether the assertion `what` holds at byte `at` of `s`. */
priv fn holds(what: Size, s: String, at: Size): Bool do
    when what do
        AT_TEXT_START do ret at == 0
        AT_TEXT_END do ret at == s.len
        AT_LINE_START do ret at == 0 or s[at - 1] == '\n'
        AT_LINE_END do ret at == s.len or s[at] == '\n'
        else do
            val before = at > 0 and isWord(s[at - 1])
            val after = at < s.len and isWord(s[at])
            ret (before != after) == (what == AT_WORD_EDGE)

/**
 * Adds a thread at instruction `start` to `q`, and those it leads to without taking a character, in the order of
 * preference. Each instruction is added once, which is what keeps the time linear: `m.work` holds the captures of the thread.
 */
priv fn addThread(m: @Machine, q: @Queue, start: Size, at: Size) do
    var depth: Size = 1
    m.stackAt[0] = start
    while depth > 0 do
        depth -= 1
        val pc = m.stackAt[depth]
        if pc == RESTORE do
            m.work[m.stackSlot[depth]] = m.stackValue[depth]
        else if not isQueued(q, pc) do
            val index = q.len
            q.sparse[pc] = index
            q.dense[index] = pc
            q.len += 1
            val inst = m.re.program[pc]
            when inst.op do
                OP_JUMP do
                    m.stackAt[depth] = inst.x
                    depth += 1
                OP_SPLIT do
                    m.stackAt[depth] = inst.y
                    m.stackAt[depth + 1] = inst.x
                    depth += 2
                OP_SAVE do
                    if inst.x < m.slots do
                        m.stackAt[depth] = RESTORE
                        m.stackSlot[depth] = inst.x
                        m.stackValue[depth] = m.work[inst.x]
                        depth += 1
                        m.work[inst.x] = at
                    m.stackAt[depth] = pc + 1
                    depth += 1
                OP_ASSERT do
                    if holds(inst.x, m.input, at) do
                        m.stackAt[depth] = pc + 1
                        depth += 1
                else do
                    for i in 0..<m.slots do
                        q.captures[index * m.slots + i] = m.work[i]

/** Whether the character set `set` has `cp`. */
priv fn inSet(set: CharSet, cp: UInt): Bool do
    ret set.ascii[cp as Size] if cp < 128
    var low: Size = 0
    var high = set.ranges.len / 2
    while low < high do
        val middle = (low + high) / 2
        if cp < set.ranges[middle * 2] do
            high = middle
        else if cp > set.ranges[middle * 2 + 1] do
            low = middle + 1
        else do
            ret true
    ret false

/** Whether the instruction `inst` takes the character `cp`. */
priv fn takesChar(re: @Regex, inst: Inst, cp: UInt): Bool do
    when inst.op do
        OP_CHAR do ret cp == inst.c
        OP_ANY do ret true
        OP_ANY_NOT_NL do ret cp != 10
        OP_SET do ret inSet(re.sets[inst.x], cp)
        else do ret false

/** Goes from `s` to the next place where a match can start, when no thread is running: `start`, or the end of the text. */
priv fn skipAhead(re: @Regex, s: String, start: Size): Size do
    ret start if re.firstByte < 0
    val wanted = re.firstByte as UChar
    var at = start
    while at < s.len and (s[at] as UChar) != wanted do
        at += 1
    ret at

/**
 * Runs the pattern from byte `from` of the text. The first match found is what a backtracking engine would have found,
 * and its captures are in `m.found`.
 * @param m the machine
 * @param from where to start looking
 * @param whole whether a match must start at `from` and go to the end of the text
 * @return whether it matches
 */
priv fn run(m: @Machine, from: Size, whole: Bool): Bool do
    ret false if from > m.input.len
    val s = m.input
    val re = m.re
    var at = from
    var matched = false
    m.now.len = 0
    m.later.len = 0
    while true do
        if m.now.len == 0 do
            break if matched
            if not whole do
                at = skipAhead(re, s, at)
        if not matched and (at == from or not (whole or re.anchored)) do
            for i in 0..<m.slots do
                m.work[i] = UNSET
            addThread(m, m.now, 0, at)
        val inText = at < s.len
        var cp: UInt = 0
        var width: Size = 0
        if inText do
            val (decoded, n) = unicode.decode(s, at)
            cp = decoded
            width = n
        for i in 0..<m.now.len do
            val pc = m.now.dense[i]
            val inst = re.program[pc]
            if inst.op == OP_MATCH do
                if not whole or at == s.len do
                    for k in 0..<m.slots do
                        m.found[k] = m.now.captures[i * m.slots + k]
                    matched = true
                    // The threads after this one are worth less than this match.
                    break
            else if inText and takesChar(re, inst, cp) do
                for k in 0..<m.slots do
                    m.work[k] = m.now.captures[i * m.slots + k]
                addThread(m, m.later, pc + 1, at + width)
        val done = m.now
        m.now = m.later
        m.later = done
        m.later.len = 0
        break if not inText or (matched and m.slots == 0)
        at += width
    ret matched

/** Where a match is and what it captured: {@link find}. */
rec Match as
    /** The text it is in. */
    input: String
    /** The pattern. */
    re: @Regex
    /** Where each group starts and ends, in turn, from group 0, the whole match. */
    spans: Size[]

/** The {@link Match} that `m` has found, in the current pool. */
priv fn matchOf(m: @Machine): @Match do
    val spans = new Size[m.slots]
    for i, value in m.found do
        spans[i] = value
    ret new Match { m.input, m.re, spans }

/** Whether all of `s` matches: `regex.matches(digits, "123")`. */
fn matches(re: @Regex, s: String): Bool do
    val scratch = newPool()
    defer freePool(scratch)
    ret run(newMachine(re, s, 0, scratch), 0, true)

/** Whether a part of `s` matches, which takes less work than {@link find}. */
fn contains(re: @Regex, s: String): Bool do
    val scratch = newPool()
    defer freePool(scratch)
    ret run(newMachine(re, s, 0, scratch), 0, false)

/**
 * Finds the first match.
 * @param re the pattern
 * @param s the text
 * @param from the byte to start looking at. What comes before it still counts for `^` and `\b`
 * @return the match, or null when there is none
 */
fn find(re: @Regex, s: String, from: Size = 0): @Match? do
    val scratch = newPool()
    defer freePool(scratch)
    val m = newMachine(re, s, (re.groups + 1) * 2, scratch)
    ret matchOf(m) if run(m, from, false)
    ret null

/** Goes through the matches of `re` in `s`: {@link findAll} and all that is built on it. */
priv rec Scan as
    /** The machine. */
    m: @Machine
    /** Where to look next. */
    at: Size
    /** Where the last match ended, or {@link UNSET}. */
    lastEnd: Size

/** The next match, with its captures in `c.m.found`, or false at the end. An empty match right after another doesn't count. */
priv fn advance(c: @Scan): Bool do
    val s = c.m.input
    while c.at <= s.len do
        ret false if not run(c.m, c.at, false)
        val start = c.m.found[0]
        val end = c.m.found[1]
        val accepted = not (start == end and start == c.lastEnd)
        if end == c.at do
            // An empty match here: go on one character further.
            if c.at < s.len do
                val (_, n) = unicode.decode(s, c.at)
                c.at += n
            else do
                c.at = s.len + 1
        else do
            c.at = end
        c.lastEnd = end
        ret true if accepted
    ret false

/**
 * All the matches in `s`, one after the other, not overlapping. A match can be empty; one right after another match doesn't count.
 * @param re the pattern
 * @param s the text
 * @return the matches, in order
 */
fn findAll(re: @Regex, s: String): (@Match)[] do
    val scratch = newPool()
    defer freePool(scratch)
    val c = new Scan { newMachine(re, s, (re.groups + 1) * 2, scratch), 0, UNSET } in scratch
    val all: @List<@Match> = list()
    while advance(c) do
        all.push(matchOf(c.m))
    ret all.items()

// ---------------------------------------------------------------------------------------------------
// Reading a match

/** Where the whole match starts. */
fn start(m: @Match): Size = m.spans[0]

/** Where the whole match ends: the byte after it. */
fn end(m: @Match): Size = m.spans[1]

/** What the whole match took. It shares the memory of the text. */
fn text(m: @Match): String = m.input[m.spans[0]..<m.spans[1]]

/**
 * Where group `i` is.
 * @return the byte where it starts and the byte after it; null when it took no part in the match, or there is no such group
 */
fn span(m: @Match, i: Size): (Size, Size)? do
    ret null if i > m.re.groups or m.spans[i * 2] == UNSET or m.spans[i * 2 + 1] == UNSET
    ret (m.spans[i * 2], m.spans[i * 2 + 1])

/**
 * What group `i` took. Groups count their `(` from 1, and group 0 is the whole match.
 * @return what it took, sharing the memory of the text; null when it took no part in the match, or there is no such group
 */
fn group(m: @Match, i: Size): String? do
    val (from, until) = span(m, i) ?? ret null
    ret m.input[from..<until]

/** The number of the group called `name`, or null when there is none. */
fn groupIndex(re: @Regex, name: String): Size? do
    ret null if name.len == 0
    ret re.names.indexOf(name)

/**
 * What the group called `name` took.
 * @return what it took, sharing the memory of the text; null when it took no part in the match, or there is no such group
 */
fn group(m: @Match, name: String): String? do
    val i = groupIndex(m.re, name) ?? ret null
    ret group(m, i)

// ---------------------------------------------------------------------------------------------------
// Replacing and splitting

/** A part of a replacement: text, or what a group took. */
priv rec Piece as
    /** The text, when {@code group} is {@link UNSET}. */
    text: String
    /** The group. */
    group: Size

/**
 * Reads the group after the `$` at byte `i` of a template.
 * @return the group and where the template goes on
 * @throws when there is no such group
 */
priv fn templateGroup(re: @Regex, template: String, i: Size): (Size, Size) throws ArgumentError do
    var at = i + 1
    val braced = at < template.len and template[at] == '{'
    at += if braced do 1 else 0
    val start = at
    while at < template.len and (isAlnum(template[at]) or template[at] == '_') and (braced or isDigit(template[at])) do
        at += 1
    val name = template[start..<at]
    throw argumentError("template", "a $ in a replacement is followed by a group, as in $1 or \${name}. Write $$ for a plain $") if name.len == 0
    if braced do
        throw argumentError("template", "this \${ has no closing }") if at >= template.len or template[at] != '}'
        at += 1
    if isDigit(name[0]) do
        val number = parseInt(name) ?? -1
        throw argumentError("template", "there is no group ${name}: this pattern has ${re.groups}") if number < 0 or (number as Size) > re.groups
        ret (number as Size, at)
    val index = groupIndex(re, name) ?? throw argumentError("template", "there is no group called '${name}'")
    ret (index, at)

/**
 * The parts of a replacement.
 * @throws when it refers to a group that isn't there
 */
priv fn parseTemplate(re: @Regex, template: String): @List<Piece> throws ArgumentError do
    val pieces: @List<Piece> = list()
    var i: Size = 0
    var start: Size = 0
    while i < template.len do
        if template[i] == '$' do
            pieces.push(Piece { template[start..<i], UNSET }) if i > start
            if i + 1 < template.len and template[i + 1] == '$' do
                pieces.push(Piece { "$", UNSET })
                i += 2
            else do
                val (group, next) = try templateGroup(re, template, i)
                pieces.push(Piece { "", group })
                i = next
            start = i
        else do
            i += 1
    pieces.push(Piece { template[start..<i], UNSET }) if i > start
    ret pieces

/** Adds the replacement of a match found by `m`. */
priv fn addReplacement(out: @StringBuilder, m: @Machine, pieces: @List<Piece>) do
    for piece in pieces do
        if piece.group == UNSET do
            out.append(piece.text)
        else do
            val from = m.found[piece.group * 2]
            val until = m.found[piece.group * 2 + 1]
            out.append(m.input[from..<until]) if from != UNSET and until != UNSET

/** `s` with up to `limit` matches replaced. */
priv fn replaced(re: @Regex, s: String, pieces: @List<Piece>, limit: Size): String do
    val scratch = newPool()
    defer freePool(scratch)
    val c = new Scan { newMachine(re, s, (re.groups + 1) * 2, scratch), 0, UNSET } in scratch
    val out = stringBuilder(s.len + 16)
    var done: Size = 0
    var copied: Size = 0
    while done < limit and advance(c) do
        out.append(s[copied..<c.m.found[0]])
        addReplacement(out, c.m, pieces)
        copied = c.m.found[1]
        done += 1
    out.append(s[copied..<s.len])
    ret out.build()

/**
 * `s` with every match replaced: `regex.replaceAll(words, "a b", "<$0>")` is "<a> <b>".
 * @param re the pattern
 * @param s the text
 * @param template the replacement: `$1` or `${1}` for what a group took, with `$0` the whole match, `${name}` for a named
 *     group, and `$$` for a `$`. A group that took no part gives nothing. In an eclang string, `${` is written `\${`
 * @throws when the template has a `$` that doesn't fit, or refers to a group the pattern doesn't have
 */
fn replaceAll(re: @Regex, s: String, template: String): String throws ArgumentError do
    val pieces = try parseTemplate(re, template)
    ret replaced(re, s, pieces, UNSET)

/**
 * `s` with the first match replaced.
 * @param re the pattern
 * @param s the text
 * @param template the replacement, as in {@link replaceAll}
 * @throws when the template has a `$` that doesn't fit, or refers to a group the pattern doesn't have
 */
fn replaceFirst(re: @Regex, s: String, template: String): String throws ArgumentError do
    val pieces = try parseTemplate(re, template)
    ret replaced(re, s, pieces, 1)

/**
 * `s` with every match replaced by what a function makes of it, for when a template isn't enough.
 * @param re the pattern
 * @param s the text
 * @param f what makes the replacement of a match
 */
fn replaceWith(re: @Regex, s: String, f: (@Match) => String): String do
    val out = stringBuilder(s.len + 16)
    var copied: Size = 0
    for m in findAll(re, s) do
        out.append(s[copied..<start(m)])
        out.append(f(m))
        copied = end(m)
    out.append(s[copied..<s.len])
    ret out.build()

/**
 * The parts of `s` between the matches: `regex.split(commas, "a, b,c")` is ["a", "b", "c"]. They share the memory of `s`.
 * A pattern that matches the empty text splits between characters.
 */
fn split(re: @Regex, s: String): String[] do
    val parts: @List<String> = list()
    var from: Size = 0
    var until: Size = 0
    for m in findAll(re, s) do
        until = start(m)
        parts.push(s[from..<until]) if end(m) != 0
        from = end(m)
    parts.push(s[from..<s.len]) if until != s.len
    ret parts.items()

/** `s` with a backslash before everything a pattern gives a meaning to, so that it matches `s` itself. */
fn escape(s: String): String do
    val out = stringBuilder(s.len + 8)
    for c in s do
        out.append('\\') if "\\.+*?()|[]{}^$".indexOf(c) != null
        out.append(c)
    ret out.build()
