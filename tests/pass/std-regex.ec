use "core:io.ec"
use "core:string.ec"
use "core:time.ec"
use "core:regex.ec"

/** The whole match and each group of the first match, or "no match". */
fn show(pattern: String, text: String) do
    val re = regex.compile(pattern) ?? panic("doesn't compile")
    val m = regex.find(re, text) ?? ret io.log("/${pattern}/ no match")
    val b = stringBuilder()
    b.append("/${pattern}/ ${regex.start(m)}-${regex.end(m)}")
    for i in 0..=re.groups do
        val part = regex.group(m, i)
        if part != null do
            b.append(" ")
            b.append(quote(part))
        else do
            b.append(" null")
    io.log(b.build())

fn all(pattern: String, text: String) do
    val re = regex.compile(pattern) ?? panic("doesn't compile")
    val b = stringBuilder()
    for m in regex.findAll(re, text) do
        b.append("[${regex.start(m)}-${regex.end(m)}]")
    io.log("/${pattern}/ ${b.build()}")

fn bad(pattern: String) do
    try regex.compile(pattern) catch e do
        io.log("${e.message} (${e.position})")

fn startOf(m: @regex.Match?): Size do
    if val found = m do
        ret regex.start(found)
    ret 99

fn upper(m: @regex.Match): String = regex.text(m).toUpper()

fn main(): Int throws Error do
    // Characters, classes and escapes.
    show("abc", "xxabcxx")
    show("a.c", "a\nc abc")
    show("\\d+", "abc 123 def")
    show("\\w+", "  hello_1!")
    show("\\s+", "a \t\nb")
    show("\\D+", "12ab34")
    show("\\W+", "ab, cd")
    show("\\S+", "  ab  ")
    show("[abc]+", "xxbcaxx")
    show("[a-c0-2]+", "xx1b2xx")
    show("[^a-c]+", "abxyzc")
    show("[]a]+", "x]a]")
    show("[^]a]+", "]xy]")
    show("[a-]+", "x-a-")
    show("[-a]+", "x-a-")
    show("[\\d.]+", "v1.25!")
    show("[\\D]+", "12ab")
    show("[\\]\\\\]+", "a]\\b")
    show("[[:alpha:]]+", "12ab34")
    show("[[:digit:][:space:]]+", "ab1 2cd")
    show("[[:^digit:]]+", "12ab34")
    show("\\.\\*\\+\\?\\(\\)\\[\\]\\{\\}\\|\\^\\$", "a.*+?()[]{}|^$b")
    show("\\t\\n\\r\\f\\v\\a", "\t\n\r\f\v\a")
    show("\\x41\\x{42}\\x{1F600}", "AB😀")
    show("[\\x41-\\x43]+", "xABCD")
    show("[\\b]", "a\x08b")
    show("a{", "a{")
    show("a{x}", "a{x}")
    show("a{,2}", "a{,2}")
    show("a{1", "a{1")
    show("}]", "}]")

    // Repeats.
    show("a*", "aaa")
    show("a+", "baaa")
    show("a?", "aaa")
    show("a{2}", "aaa")
    show("a{2,}", "aaaaa")
    show("a{1,2}", "aaaaa")
    show("a{0}b", "ab")
    show("a*?", "aaa")
    show("a+?", "aaa")
    show("a??", "aaa")
    show("a{1,3}?", "aaa")
    show("a{2,}?", "aaaa")
    show("<.+>", "<a> <b>")
    show("<.+?>", "<a> <b>")

    // Groups and alternatives.
    show("(a)(b)(c)", "abc")
    show("(a|b)+", "abba")
    show("(a)|(b)", "b")
    show("a|ab", "ab")
    show("ab|a", "ab")
    show("(?:a|b)c", "bc")
    show("(a(b(c)))", "abc")
    show("(a*)*", "b")
    show("(a*)+", "b")
    show("(a|b*)*", "bbab")
    show("()|a", "a")
    show("|a", "a")
    show("a|", "b")
    show("(|a)+", "aa")
    show("", "abc")
    show("(?:)", "abc")

    // Anchors.
    show("^abc", "abc")
    show("^abc", "xabc")
    show("abc$", "xabc")
    show("abc$", "abc\n")
    show("abc\\z", "abc")
    show("\\Aabc", "xabc")
    show("^$", "")
    show("\\bfoo\\b", "a foo b")
    show("\\bfoo\\b", "afoo foob")
    show("\\Boo\\b", "foo oo")
    show("(?m)^b$", "a\nb\nc")
    show("(?m)\\Ab", "a\nb")
    show("(?m)a\\z", "a\na")

    // Flags.
    show("(?i)HeLLo", "hello")
    show("(?i)[a-c]+", "xABCx")
    show("(?i)[^a]+", "Aab")
    show("(?i)\\x41", "a")
    show("(?s)a.b", "a\nb")
    show("a(?i)b", "aB")
    show("a(?i)b", "AB")
    show("(?i:a)b", "Ab")
    show("(?i:a)b", "AB")
    show("(?i)a(?-i)b", "AB")
    show("(?i)a(?-i)b", "Ab")
    show("((?i)a)b", "AB")
    show("(?im)^a$", "b\nA\nc")
    val folded = try regex.compile("hello", ignoreCase = true)
    io.log(regex.matches(folded, "HeLLo"))
    val lines = try regex.compile("^b$", multiline = true)
    io.log(regex.contains(lines, "a\nb\nc"))
    val dots = try regex.compile("a.b", dotAll = true)
    io.log(regex.matches(dots, "a\nb"))

    // Code points, and what isn't valid UTF-8.
    show(".", "é")
    show("é+", "aééb")
    show("[à-ÿ]+", "aéèb")
    show("[^a]", "é")
    show("..", "日本語")
    show("^.$", "\xFF")
    show("\\x{FFFD}", "a\xFFb")
    show("�", "a\xFFb")
    all("", "é日")

    // Whole texts and parts of them.
    val digits = try regex.compile("\\d+")
    io.log(regex.matches(digits, "123"))
    io.log(regex.matches(digits, "123a"))
    io.log(regex.contains(digits, "a123"))
    io.log(regex.contains(digits, "abc"))
    val either = try regex.compile("a|ab")
    io.log(regex.matches(either, "ab"))
    val anchored = try regex.compile("^a")
    io.log(regex.contains(anchored, "ba"))
    val nothing = try regex.compile("[^\\x00-\\x{10FFFF}]")
    io.log(regex.contains(nothing, "abc"))

    // Reading a match.
    val date = try regex.compile("(\\d{4})-(\\d{2})(-(?P<day>\\d{2}))?(?<zone>Z)?")
    val m = regex.find(date, "on 2024-02-29!") ?? panic("no match")
    io.log(regex.text(m))
    io.log(regex.group(m, 1) ?? "?")
    io.log(regex.group(m, "day") ?? "?")
    io.log(regex.group(m, "zone") ?? "no zone")
    io.log(regex.group(m, "other") ?? "no such group")
    io.log(regex.group(m, 9) ?? "no such group")
    io.log(regex.group(m, "") ?? "no such group")
    io.log(regex.span(m, 2)!.first)
    io.log(regex.span(m, 2)!.second)
    io.log(regex.span(m, 5) == null)
    io.log(date.groups)
    io.log(regex.groupIndex(date, "zone") ?? 99)
    val partial = regex.find(date, "2024-02") ?? panic("no match")
    io.log(regex.group(partial, "day") ?? "no day")

    // From a place in the text, which still sees what is before it.
    val word = try regex.compile("\\bb")
    io.log(startOf(regex.find(word, "ab b", 1)))
    val start = try regex.compile("^b")
    io.log(regex.find(start, "ab", 1) == null)
    io.log(startOf(regex.find(digits, "1 2", 1)))
    io.log(regex.find(digits, "1", 2) == null)
    io.log(startOf(regex.find(try regex.compile("x*"), "ab", 2)))
    io.log(regex.find(try regex.compile("x*"), "ab", 3) == null)

    // All matches, and the empty ones among them.
    all("a", "banana")
    all("a*", "baaac")
    all("\\b", "ab cd")
    all("", "abc")
    all("x*", "")
    all("a|", "bab")
    all("^", "a\nb")
    all("(?m)^", "a\nb\n")
    all("(?m)$", "a\nb\n")

    // Replacing.
    val pair = try regex.compile("(\\w+) (?P<second>\\w+)")
    io.log(try regex.replaceAll(pair, "hello world, big deal", "$2 $1"))
    io.log(try regex.replaceAll(pair, "hello world", "\${second}-\${1}-$0"))
    io.log(try regex.replaceAll(pair, "hello world", "$$1 costs $$$1"))
    io.log(try regex.replaceFirst(pair, "a b c d", "<$0>"))
    io.log(try regex.replaceAll(digits, "none here", "x"))
    io.log(try regex.replaceAll(try regex.compile("x*"), "abc", "-"))
    io.log(try regex.replaceAll(try regex.compile("a*"), "baaac", "-"))
    io.log(try regex.replaceAll(try regex.compile("(a)|b"), "ab", "[$1]"))
    io.log(try regex.replaceAll(try regex.compile("é"), "aéb", "e"))
    io.log(regex.replaceWith(digits, "a1b22", fn upper))
    io.log(regex.replaceWith(try regex.compile("[a-z]+"), "ab 12 cd", fn upper))
    try regex.replaceAll(pair, "", "$3") catch e1 do
        io.log(e1.message)
    try regex.replaceAll(pair, "", "\${nope}") catch e2 do
        io.log(e2.message)
    try regex.replaceAll(pair, "", "$") catch e3 do
        io.log(e3.message)
    try regex.replaceAll(pair, "", "\${1") catch e4 do
        io.log(e4.message)
    try regex.replaceAll(pair, "", "$x") catch e5 do
        io.log(e5.message)

    // Splitting.
    val commas = try regex.compile(",\\s*")
    io.log(join(regex.split(commas, "a, b,c"), "|"))
    io.log(join(regex.split(commas, ",a,"), "|"))
    io.log(regex.split(commas, "").len)
    io.log(regex.split(commas, "abc").len)
    io.log(join(regex.split(try regex.compile(""), "abc"), "|"))
    io.log(join(regex.split(try regex.compile("a*"), "baaac"), "|"))

    // Escaping.
    io.log(regex.escape("a.b*c"))
    val exact = try regex.compile(regex.escape("1+1=2 (really?) [ok] {x} ^$ | \\"))
    io.log(regex.matches(exact, "1+1=2 (really?) [ok] {x} ^$ | \\"))

    // What isn't valid says where and why.
    bad("(a")
    bad("a)")
    bad("[a")
    bad("[z-a]")
    bad("[\\d-z]")
    bad("[a-\\d]")
    bad("[[:nope:]]")
    bad("[[:alpha]")
    bad("*a")
    bad("+")
    bad("?")
    bad("{2}")
    bad("a**")
    bad("a*+")
    bad("a+?+")
    bad("a{2}{3}")
    bad("a{3,2}")
    bad("a{1001}")
    bad("\\")
    bad("[\\")
    bad("\\q")
    bad("\\1")
    bad("\\p{L}")
    bad("\\Z")
    bad("\\x")
    bad("\\x4")
    bad("\\x{")
    bad("\\x{}")
    bad("\\x{110000}")
    bad("\\x{zz}")
    bad("(?=a)")
    bad("(?!a)")
    bad("(?<=a)")
    bad("(?<!a)")
    bad("(?>a)")
    bad("(?x)")
    bad("(?i")
    bad("(?P<1a>x)")
    bad("(?P<>x)")
    bad("(?P<a")
    bad("(?P<a>x)(?P<a>y)")
    bad("\xFF")

    // A pattern can't take too much memory or time to compile.
    bad("(?:a{1000}){1000}")
    bad("(((a{100}){100}){100}){100}")
    bad("((((?:){1000}){1000}){1000}){1000}")
    bad(concat(repeat("(", 2000), repeat(")", 2000)))
    bad(repeat("(a)", 30000))
    val big = try regex.compile("a{1000}b{1000}")
    io.log(regex.matches(big, concat(repeat("a", 1000), repeat("b", 1000))))
    val deep = try regex.compile(concat(repeat("(", 400), "a", repeat(")", 400)))
    io.log(deep.groups)
    // A long choice, and text of a size that would overflow a recursive engine's stack.
    val many = try regex.compile(concat(repeat("a|", 20000), "b"))
    io.log(regex.matches(many, "b"))
    val long = repeat("ab", 500000)
    io.log(regex.matches(try regex.compile("(?:ab)*"), long))

    // What makes backtracking engines take exponential time takes no time here.
    val attack = repeat("a", 5000)
    val clock = time.instant()
    for pattern in ["(a+)+$", "(a|a)*$", "(a|aa)+$", "(a*)*$", "(.*)(.*)(.*)(.*)(.*)$", "(x+x+)+y", "^(a+)+b", "(a?){30}a{30}", "(?:a?){100}"] do
        val re = try regex.compile(pattern)
        val input = concat(attack, "!")
        io.log(regex.contains(re, input))
        io.log(regex.matches(re, input))
    val took = time.toSeconds(time.elapsed(clock))
    io.log(took < 20)
    ret 0
