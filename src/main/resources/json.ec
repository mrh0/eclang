/**
 * JSON (RFC 8259): parsing with error positions, building values, and writing them, compact or pretty.
 *
 *     use "core:json.ec"
 *     val v = try json.parse("{\"name\": \"Ada\", \"langs\": [\"c\", \"ec\"], \"age\": 36}")
 *     io.log(json.asString(json.field(v, "name")) ?? "?")              // Ada
 *     io.log(json.asLong(json.find(v, "age")) ?? 0)                    // 36
 *     io.log(json.asString(json.find(v, "langs[1]")) ?? "?")           // ec
 *     for lang in json.items(json.field(v, "langs")) do
 *         io.log(json.asString(lang) ?? "?")
 *
 *     val out = json.object()
 *     json.put(out, "ok", true)
 *     json.put(out, "count", 3)
 *     val list = json.array()
 *     json.add(list, "a")
 *     json.put(out, "list", list)
 *     io.log(json.stringify(out))                                      // {"ok":true,"count":3,"list":["a"]}
 *     io.log(json.pretty(out))
 *
 * The readers (`asString`, `field`, `find`, ...) take and give null, so they chain: what isn't there, or isn't of
 * that kind, comes out as null at the end. The fields of an object stay in the order they were added. Whole numbers
 * that fit a Long stay exact; others are Doubles. Everything is allocated in the current pool.
 */
module "json"
use "core:errors.ec"
use "core:string.ec"
use "core:structures.ec"
use "core:unicode.ec"
use "core:c/stdio.ec"
use "core:c/stdlib.ec"

/** What a {@link Value} is. */
declare type Kind as :null | :bool | :number | :string | :array | :object

/** A JSON value. Make one with {@link parse} or the functions below, and read it with {@link asString} and the like. */
ref rec Value as
    /** What it is; the fields that go with that are set. */
    kind: Kind
    /** The value of a `:bool`. */
    flag: Bool = false
    /** The value of a `:number`. */
    number: Double = 0.0
    /** The value of a `:number` that is whole and fits, when {@code isWhole}. */
    whole: Long = 0
    /** Whether a `:number` is exactly {@code whole}. */
    isWhole: Bool = false
    /** The value of a `:string`. */
    text: String = ""
    /** The elements of an `:array`. */
    elements: List<Value>? = null
    /** The fields of an `:object`. */
    fields: Map<String, Value>? = null

/** Text isn't valid JSON. */
rec JsonError extends ParseError as
    /** The line of the problem, from 1. */
    line: Size
    /** The column of the problem, in bytes, from 1. */
    column: Size

// ---------------------------------------------------------------------------------------------------
// Making values

/** The JSON value `null`. */
fn nil(): Value = new Value { kind = :null }

/** A JSON `true` or `false`. */
fn boolean(b: Bool): Value = new Value { kind = :bool, flag = b }

/** A JSON number. */
fn number(x: Double): Value = new Value { kind = :number, number = x }

/** A JSON number that stays exact. */
fn number(x: Long): Value = new Value { kind = :number, number = x as Double, whole = x, isWhole = true }

/** A JSON string. */
fn string(s: String): Value = new Value { kind = :string, text = s }

/** A new empty JSON array; see {@link add}. */
fn array(): Value do
    val elements: List<Value> = list()
    ret new Value { kind = :array, elements = elements }

/** A new empty JSON object; see {@link put}. */
fn object(): Value do
    val fields: Map<String, Value> = map()
    ret new Value { kind = :object, fields = fields }

/** Adds `item` at the end of the array `a`. The program stops when `a` isn't an array. */
fn add(a: Value, item: Value) do
    assert a.kind == :array, "json.add needs an array; make one with json.array()"
    a.elements!.push(item)

/** Adds a string at the end of the array `a`. */
fn add(a: Value, item: String) do
    add(a, string(item))

/** Adds a number at the end of the array `a`. */
fn add(a: Value, item: Long) do
    add(a, number(item))

/** Adds a number at the end of the array `a`. */
fn add(a: Value, item: Double) do
    add(a, number(item))

/** Adds a `true` or `false` at the end of the array `a`. */
fn add(a: Value, item: Bool) do
    add(a, boolean(item))

/** Sets the field `key` of the object `o`. The program stops when `o` isn't an object. */
fn put(o: Value, key: String, value: Value) do
    assert o.kind == :object, "json.put needs an object; make one with json.object()"
    o.fields!.put(key, value)

/** Sets the field `key` of the object `o` to a string. */
fn put(o: Value, key: String, value: String) do
    put(o, key, string(value))

/** Sets the field `key` of the object `o` to a number. */
fn put(o: Value, key: String, value: Long) do
    put(o, key, number(value))

/** Sets the field `key` of the object `o` to a number. */
fn put(o: Value, key: String, value: Double) do
    put(o, key, number(value))

/** Sets the field `key` of the object `o` to `true` or `false`. */
fn put(o: Value, key: String, value: Bool) do
    put(o, key, boolean(value))

// ---------------------------------------------------------------------------------------------------
// Reading values

/** Whether `v` is the JSON value `null`. False when there is no value at all. */
fn isNull(v: Value?): Bool do
    if val value = v do
        ret value.kind == :null
    ret false

/** The text of a string, or null for anything else. */
fn asString(v: Value?): String? do
    if val value = v do
        ret value.text if value.kind == :string
    ret null

/** The value of a number, or null for anything else. */
fn asNumber(v: Value?): Double? do
    if val value = v do
        ret value.number if value.kind == :number
    ret null

/** The value of a whole number that fits a Long, or null for anything else. 3.0 and 1e3 are whole; 3.5 isn't. */
fn asLong(v: Value?): Long? do
    if val value = v do
        if value.kind == :number do
            ret value.whole if value.isWhole
            val rounded = value.number as Long
            ret rounded if value.number > -9.0e18 and value.number < 9.0e18 and (rounded as Double) == value.number
    ret null

/** The value of a `true` or `false`, or null for anything else. */
fn asBool(v: Value?): Bool? do
    if val value = v do
        ret value.flag if value.kind == :bool
    ret null

/** The field `key` of an object; null when there is no such field, or `v` isn't an object. */
fn field(v: Value?, key: String): Value? do
    if val value = v do
        if val fields = value.fields do
            ret fields.get(key)
    ret null

/** Element `i` of an array; null when there is no such element, or `v` isn't an array. */
fn item(v: Value?, i: Size): Value? do
    if val value = v do
        if val elements = value.elements do
            ret elements.buffer[i] if i < elements.len
    ret null

/** The elements of an array, for `for`; none for anything else. */
fn items(v: Value?): (Value)[] do
    if val value = v do
        if val elements = value.elements do
            ret elements.items()
    val none: List<Value> = list(1)
    ret none.items()

/** The names of the fields of an object, in their order; none for anything else. */
fn keys(v: Value?): String[] do
    if val value = v do
        if val fields = value.fields do
            ret fields.keys()
    ret new String[0]

/** How many elements an array, or fields an object, has; 0 for anything else. */
fn count(v: Value?): Size do
    if val value = v do
        if val elements = value.elements do
            ret elements.len
        if val fields = value.fields do
            ret fields.len
    ret 0

/**
 * Follows a path of fields and indexes: `json.find(v, "users[2].name")`. Field names with "." or "[" in them need
 * {@link field}.
 * @return what is there, or null when the path leads nowhere
 */
fn find(v: Value?, path: String): Value? do
    var at = v
    var i: Size = 0
    while i < path.len do
        if path[i] == '[' do
            val close = indexOf(path, "]", i) ?? ret null
            val index = parseLong(path[i + 1..<close], 10) ?? ret null
            ret null if index < 0
            at = item(at, index as Size)
            i = close + 1
        else do
            val start = if path[i] == '.' do i + 1 else i
            var end = start
            while end < path.len and path[end] != '.' and path[end] != '[' do
                end += 1
            at = field(at, path[start..<end])
            i = end
    ret at

// ---------------------------------------------------------------------------------------------------
// Parsing

/** How deep arrays and objects can be nested: {@link parse} calls itself, and the stack is limited. */
priv const MAX_DEPTH = 512

/** Where {@link parse} is. */
priv mut rec Parser as
    /** The text. */
    input: String
    /** The byte it is at. */
    at: Size
    /** How many arrays and objects it is in. */
    depth: Int

/** The {@link JsonError} `message`, for byte `position` of the text. */
priv fn problem(p: @Parser, message: String, position: Size): JsonError do
    var line: Size = 1
    var lineStart: Size = 0
    for i in 0..<position where p.input[i] == '\n' do
        line += 1
        lineStart = i + 1
    val column = position - lineStart + 1
    ret JsonError { ERR_PARSE, "line ${line}, column ${column}: ${message}", p.input, position, line, column }

/** Goes past spaces and line breaks. */
priv fn skipSpace(p: @Parser) do
    while p.at < p.input.len and isSpace(p.input[p.at]) do
        p.at += 1

/** Whether the text goes on with `word`, a keyword. */
priv fn startsHere(p: @Parser, word: String): Bool = p.at + word.len <= p.input.len and p.input[p.at..<p.at + word.len] == word

/** What is at the current byte, for messages. */
priv fn current(p: @Parser): String do
    val c = p.input[p.at]
    ret if c >= ' ' and c < 127 as Char do "'${c}'" else "byte ${(c as UChar) as Int}"

/**
 * The value of the four hexadecimal digits at `at`.
 * @throws when they aren't
 */
priv fn hex4(p: @Parser, at: Size): UInt throws JsonError do
    throw problem(p, "\\u needs four hexadecimal digits, such as \\u00e9", at) if at + 4 > p.input.len
    var value: UInt = 0
    for i in at..<at + 4 do
        val c = p.input[i]
        throw problem(p, "\\u needs four hexadecimal digits, such as \\u00e9", i) if not isHexDigit(c)
        value = value * 16 + ((digitValue(c) ?? 0) as UInt)
    ret value

/**
 * Reads the \u escape at the current byte, the one after the "u", and the second half of a surrogate pair.
 * @throws when it isn't valid
 */
priv fn unicodeEscape(p: @Parser, out: StringBuilder) throws JsonError do
    val start = p.at - 2
    val first = try hex4(p, p.at)
    p.at += 4
    if unicode.isLowSurrogate(first) do
        throw problem(p, "this \\u escape is the second half of a surrogate pair, and the first is missing", start)
    if unicode.isHighSurrogate(first) do
        throw problem(p, "this \\u escape is the first half of a surrogate pair, and the second is missing", start) if not startsHere(p, "\\u")
        val second = try hex4(p, p.at + 2)
        throw problem(p, "this \\u escape is the first half of a surrogate pair, and the second is missing", start) if not unicode.isLowSurrogate(second)
        p.at += 6
        unicode.appendCodePoint(out, unicode.joinSurrogates(first, second))
    else do
        unicode.appendCodePoint(out, first)

/**
 * Reads the string at the current byte, a `"`.
 * @throws when it isn't valid
 */
priv fn parseString(p: @Parser): String throws JsonError do
    val start = p.at
    p.at += 1
    val out = stringBuilder(32)
    while true do
        throw problem(p, "this string has no closing '\"'", start) if p.at >= p.input.len
        val c = p.input[p.at]
        if c == '"' do
            p.at += 1
            val text = out.build()
            throw problem(p, "this string isn't valid UTF-8", start) if not isUtf8(text)
            ret text
        throw problem(p, "a line break or control character can't be in a string; write it as an escape, such as \\n", p.at) if (c as UChar) < 32
        p.at += 1
        if c != '\\' do
            out.append(c)
        else do
            throw problem(p, "this string has no closing '\"'", start) if p.at >= p.input.len
            val e = p.input[p.at]
            p.at += 1
            if e == 'u' do
                try unicodeEscape(p, out)
            else if e == '"' or e == '\\' or e == '/' do
                out.append(e)
            else if e == 'n' do
                out.append('\n')
            else if e == 't' do
                out.append('\t')
            else if e == 'r' do
                out.append('\r')
            else if e == 'b' do
                out.append(8 as Char)
            else if e == 'f' do
                out.append(12 as Char)
            else do
                throw problem(p, "unknown escape: the escapes are \\\" \\\\ \\/ \\b \\f \\n \\r \\t and \\u", p.at - 2)

/** Goes past digits. @return how many */
priv fn skipDigits(p: @Parser): Size do
    val start = p.at
    while p.at < p.input.len and isDigit(p.input[p.at]) do
        p.at += 1
    ret p.at - start

/**
 * Reads the number at the current byte.
 * @throws when it isn't valid
 */
priv fn parseNumber(p: @Parser): Value throws JsonError do
    val start = p.at
    val negative = p.input[p.at] == '-'
    if negative do
        p.at += 1
    val digitsStart = p.at
    val digits = skipDigits(p)
    throw problem(p, "expected a digit after the '-'", p.at) if digits == 0
    throw problem(p, "a number can't start with 0 unless it is 0", digitsStart) if digits > 1 and p.input[digitsStart] == '0'
    var whole = true
    if p.at < p.input.len and p.input[p.at] == '.' do
        whole = false
        p.at += 1
        throw problem(p, "expected a digit after the '.'", p.at) if skipDigits(p) == 0
    if p.at < p.input.len and (p.input[p.at] == 'e' or p.input[p.at] == 'E') do
        whole = false
        p.at += 1
        if p.at < p.input.len and (p.input[p.at] == '+' or p.input[p.at] == '-') do
            p.at += 1
        throw problem(p, "expected a digit in the exponent", p.at) if skipDigits(p) == 0
    if whole and digits <= 19 do
        var value: ULong = 0
        for i in digitsStart..<digitsStart + digits do
            value = value * 10 + ((p.input[i] - '0') as ULong)
        if value <= 9223372036854775807ul do
            val exact = if negative do -(value as Long) else value as Long
            ret number(exact)
    val x = strtod(cstr(p.input[start..<p.at]), null)
    throw problem(p, "this number is too large for a Double", start) if x > 1.7976931348623157e308 or x < -1.7976931348623157e308
    ret number(x)

/**
 * Reads the array at the current byte, a `[`.
 * @throws when it isn't valid
 */
priv fn parseArray(p: @Parser): Value throws JsonError do
    val start = p.at
    p.at += 1
    val a = array()
    skipSpace(p)
    if p.at < p.input.len and p.input[p.at] == ']' do
        p.at += 1
        ret a
    while true do
        add(a, try parseValue(p))
        skipSpace(p)
        throw problem(p, "this array has no closing ']'", start) if p.at >= p.input.len
        val c = p.input[p.at]
        p.at += 1
        ret a if c == ']'
        throw problem(p, "expected ',' or ']' after an array element", p.at - 1) if c != ','
        val comma = p.at - 1
        skipSpace(p)
        throw problem(p, "a ',' must be followed by another element; remove this one", comma) if p.at < p.input.len and p.input[p.at] == ']'

/**
 * Reads the object at the current byte, a `{`.
 * @throws when it isn't valid
 */
priv fn parseObject(p: @Parser): Value throws JsonError do
    val start = p.at
    p.at += 1
    val o = object()
    skipSpace(p)
    if p.at < p.input.len and p.input[p.at] == '}' do
        p.at += 1
        ret o
    while true do
        throw problem(p, "this object has no closing '}'", start) if p.at >= p.input.len
        throw problem(p, "the name of a field is a string in double quotes", p.at) if p.input[p.at] != '"'
        val key = try parseString(p)
        skipSpace(p)
        throw problem(p, "expected ':' after the name of a field", p.at) if p.at >= p.input.len or p.input[p.at] != ':'
        p.at += 1
        put(o, key, try parseValue(p))
        skipSpace(p)
        throw problem(p, "this object has no closing '}'", start) if p.at >= p.input.len
        val c = p.input[p.at]
        p.at += 1
        ret o if c == '}'
        throw problem(p, "expected ',' or '}' after a field", p.at - 1) if c != ','
        val comma = p.at - 1
        skipSpace(p)
        throw problem(p, "a ',' must be followed by another field; remove this one", comma) if p.at < p.input.len and p.input[p.at] == '}'

/**
 * Reads the array or object at the current byte.
 * @throws when it isn't valid or too deep
 */
priv fn parseNested(p: @Parser, isArray: Bool): Value throws JsonError do
    throw problem(p, "arrays and objects are nested more than ${MAX_DEPTH} deep", p.at) if p.depth >= MAX_DEPTH
    p.depth += 1
    if isArray do
        val a = try parseArray(p)
        p.depth -= 1
        ret a
    val o = try parseObject(p)
    p.depth -= 1
    ret o

/**
 * Reads the value at the current byte, after any spaces.
 * @throws when it isn't valid
 */
priv fn parseValue(p: @Parser): Value throws JsonError do
    skipSpace(p)
    throw problem(p, "the text ended where a value should be", p.at) if p.at >= p.input.len
    val c = p.input[p.at]
    ret try parseNested(p, false) if c == '{'
    ret try parseNested(p, true) if c == '['
    if c == '"' do
        ret string(try parseString(p))
    ret try parseNumber(p) if c == '-' or isDigit(c)
    if startsHere(p, "true") do
        p.at += 4
        ret boolean(true)
    if startsHere(p, "false") do
        p.at += 5
        ret boolean(false)
    if startsHere(p, "null") do
        p.at += 4
        ret nil()
    throw problem(p, "strings use double quotes in JSON", p.at) if c == '\''
    throw problem(p, "JSON has no comments", p.at) if startsHere(p, "//") or startsHere(p, "/*")
    throw problem(p, "expected a value (a string, a number, an array, an object, true, false or null), not ${current(p)}", p.at)

/**
 * Parses JSON text. Of a field that appears twice, the last one counts.
 * @param text exactly one JSON value, with spaces around it if need be
 * @throws when it isn't valid; the error tells the line and column, and {@code position} is the byte
 */
fn parse(text: String): Value throws JsonError do
    val p = new Parser { text, 0, 0 }
    val v = try parseValue(p)
    skipSpace(p)
    throw problem(p, "there is more text after the value: ${current(p)}", p.at) if p.at < text.len
    ret v

// ---------------------------------------------------------------------------------------------------
// Writing

/** Adds `s` to `out` as a JSON string. */
priv fn writeString(out: StringBuilder, s: String) do
    out.append('"')
    for c in s do
        if c == '"' do
            out.append("\\\"")
        else if c == '\\' do
            out.append("\\\\")
        else if c == '\n' do
            out.append("\\n")
        else if c == '\r' do
            out.append("\\r")
        else if c == '\t' do
            out.append("\\t")
        else if (c as UChar) < 32 do
            out.append("\\u00")
            out.append("0123456789abcdef"[((c as UChar) >> 4) as Size])
            out.append("0123456789abcdef"[((c as UChar) & 15) as Size])
        else do
            out.append(c)
    out.append('"')

/** Adds `x` to `out` in the fewest digits that read back as the same number. JSON has no infinity or NaN: they are `null`. */
priv fn writeNumber(out: StringBuilder, x: Double) do
    if x != x or x > 1.7976931348623157e308 or x < -1.7976931348623157e308 do
        out.append("null")
    else do
        val buffer = new Char[40]
        val text = buffer.data !as CString
        var digits = 15
        snprintf(text, buffer.len, "%.*g"c, digits, x)
        while strtod(text, null) != x do
            digits += 1
            snprintf(text, buffer.len, "%.*g"c, digits, x)
        out.append(text)

/** A line break and the spaces of level `level`, when pretty printing. */
priv fn newline(out: StringBuilder, indent: Size, level: Size) do
    if indent > 0 do
        out.append('\n')
        for i in 0..<indent * level do
            out.append(' ')

/** Adds `v` to `out`. An `indent` of 0 is compact; `level` is how deep it is. */
priv fn write(out: StringBuilder, v: Value, indent: Size, level: Size) do
    when v.kind do
        :null do out.append("null")
        :bool do out.append(if v.flag do "true" else "false")
        :string do writeString(out, v.text)
        :number do
            if v.isWhole do
                out.append(v.whole)
            else do
                writeNumber(out, v.number)
        :array do
            val elements = items(v)
            out.append('[')
            for i, element in elements do
                out.append(',') if i > 0
                newline(out, indent, level + 1)
                write(out, element, indent, level + 1)
            newline(out, indent, level) if elements.len > 0
            out.append(']')
        :object do
            val names = keys(v)
            out.append('{')
            for i, name in names do
                out.append(',') if i > 0
                newline(out, indent, level + 1)
                writeString(out, name)
                out.append(':')
                out.append(' ') if indent > 0
                write(out, field(v, name)!, indent, level + 1)
            newline(out, indent, level) if names.len > 0
            out.append('}')

/** `v` as compact JSON text, on one line. */
fn stringify(v: Value): String do
    val out = stringBuilder(256)
    write(out, v, 0, 0)
    ret out.build()

/**
 * `v` as JSON text for people, on several lines.
 * @param v the value
 * @param indent how many spaces each level is indented
 */
fn pretty(v: Value, indent: Size = 2): String do
    val out = stringBuilder(256)
    write(out, v, if indent == 0 do 1 else indent, 0)
    ret out.build()
