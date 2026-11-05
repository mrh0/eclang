/**
 * Tests: assertions that say what differs, and a small runner.
 *
 *     use "core:test.ec"
 *
 *     fn addition(t: @test.Test) do
 *         test.equal(t, 1 + 1, 2)
 *         test.isTrue(t, 2 > 1, "2 is more than 1")
 *         test.equal(t, render(), "line one\nline two\n", "the rendered text")
 *
 *     fn parsing(t: @test.Test) throws Error do           // an error that gets out fails the test
 *         val n = try parseInt("12")
 *         test.equal(t, n, 12)
 *
 *     fn main(): Int do
 *         val s = test.suite()
 *         test.run(s, "addition", fn addition)
 *         test.run(s, "parsing", fn parsing)
 *         ret test.finish(s)                              // 0, or 1 when something failed
 *
 * A failed check doesn't end the test: the next ones still run, and each is reported. {@link require} is for a check
 * the rest of the test can't do without. Strings that differ are shown as a line by line diff, or with a mark at
 * the first difference when they are one line:
 *
 *     FAIL addition
 *       check 3, the rendered text: the strings differ (- expected, + actual)
 *           line one
 *         - line two
 *         + line 2
 *
 * A program run with an argument only runs the tests whose name has it in it: `./tests parsing`.
 *
 * Tests are listed by hand, and a failure names the check by its number and the `what` given to it, not by its line,
 * since a function can't see where it was called from. A panic ends the whole run, with the results so far printed.
 * Each test runs in its own pool. Everything is written to stdout.
 */
module "test"
use "core:errors.ec"
use "core:string.ec"
use "core:structures.ec"
use "core:unicode.ec"
use "core:io.ec"

// ---------------------------------------------------------------------------------------------------
// Running

/** A group of tests that are run and counted together: {@link suite}. */
rec Suite as
    /** Only tests with this in their name run, unless it is empty. */
    filter: String
    /** How many tests passed. */
    passed: Size
    /** The names of the tests that failed. */
    failed: @List<String>
    /** How many tests the filter left out. */
    skipped: Size

/** The test that is running, which its function is given. */
rec Test as
    /** Its name. */
    name: String
    /** How many checks it has made. */
    checks: Size
    /** How many of them failed. */
    failures: Size
    /** What to print about them. */
    report: @StringBuilder
    /** Whether {@link require} ended it. */
    stopped: Bool = false

/** A new empty suite, in the current pool. The first command-line argument of the program, if any, is its filter. */
fn suite(): @Suite do
    val args = io.args()
    val names: @List<String> = list()
    ret new Suite { if args.len > 0 do args[0] else "", 0, names, 0 }

/** A new suite that only runs the tests with `filter` in their name. */
fn suite(filter: String): @Suite do
    val names: @List<String> = list()
    ret new Suite { filter, 0, names, 0 }

/** Whether `s` has `part` in it. */
priv fn has(s: String, part: String): Bool = indexOf(s, part) != null

/** Prints how `t` went and counts it. */
priv fn conclude(s: @Suite, t: @Test) do
    if t.failures == 0 do
        io.log("ok   ${t.name}")
        s.passed += 1
    else do
        io.log("FAIL ${t.name}")
        io.print(t.report.build())
        s.failed.push(clone(t.name, s.failed.mem))
    // A crash in the next test shouldn't take this result with it.
    io.flush()

/** Whether the test called `name` is to run. */
priv fn wanted(s: @Suite, name: String): Bool do
    ret true if has(name, s.filter)
    s.skipped += 1
    ret false

/**
 * Runs a test, in a pool of its own, and prints how it went.
 * @param s the suite
 * @param name what to call it
 * @param body the test
 */
fn run(s: @Suite, name: String, body: (@Test) => None) do
    if wanted(s, name) do
        pool do
            val t = new Test { name, 0, 0, stringBuilder(256) }
            body(t)
            conclude(s, t)

/**
 * Runs a test that can fail with an error, in a pool of its own, and prints how it went. An error that gets out of it fails the test.
 * @param s the suite
 * @param name what to call it
 * @param body the test
 */
fn run(s: @Suite, name: String, body: (@Test) => None throws Error) do
    if wanted(s, name) do
        pool do
            val t = new Test { name, 0, 0, stringBuilder(256) }
            try body(t) catch e do
                if not t.stopped do
                    t.failures += 1
                    t.report.append("  it stopped with an error: ${describe(e)}\n")
            conclude(s, t)

/**
 * Prints how many tests passed and failed, naming those that failed.
 * @return what `main` should return: 0 when all passed, and 1 when one failed, or the filter left nothing to run
 */
fn finish(s: @Suite): Int do
    val total = s.passed + s.failed.len
    val left = if s.skipped > 0 do format(", ${s.skipped} left out") else ""
    io.log("")
    io.log("${total} test(s): ${s.passed} passed, ${s.failed.len} failed${left}")
    for name in s.failed do
        io.log("  failed: ${name}")
    io.flush()
    if total == 0 do
        io.log("no test has '${s.filter}' in its name")
        ret 1
    ret if s.failed.len > 0 do 1 else 0

// ---------------------------------------------------------------------------------------------------
// Reporting

/** Counts a failed check, with `details` under a line saying what failed. */
priv fn failure(t: @Test, what: String, details: String) do
    t.failures += 1
    val label = if what.len == 0 do "" else concat(", ", what)
    val first = before(details, "\n")
    t.report.append("  check ${t.checks}${label}: ${first}\n")
    if first.len < details.len do
        t.report.append(details[first.len + 1..<details.len])

/** `v` as `${...}` shows a value inside a tuple, which is the way for a `<V>` of any kind: Strings get their quotes. */
priv fn shown(v: <V>): String do
    val text = "${(v, 0)}"
    ret text[1..<text.len - 4]

/** Counts a check that is made. Each assertion starts with this. */
priv fn counted(t: @Test, passed: Bool): Bool do
    t.checks += 1
    ret passed

/** The detail lines that show two values. */
priv fn values(actual: String, expected: String): String = "the values differ\n      expected: ${expected}\n      actual:   ${actual}\n"

// ---------------------------------------------------------------------------------------------------
// Diffs

/** How big a table {@link diff} may make; two longer texts are shown as replaced, not compared. */
priv const DIFF_LIMIT: Size = 4000000
/** How many unchanged lines {@link diff} shows around a change. */
priv const DIFF_CONTEXT: Size = 2
/** The longest String that is shown whole in the report of a difference in one line. */
priv const SHOWN_LIMIT: Size = 76

/** A line of a diff. */
priv rec DiffLine as
    /** `' '` when it is in both texts, `'-'` when only in the expected one, `'+'` when only in the actual one. */
    mark: Char
    /** The line. */
    text: String

/** `line` so that what can't be seen can be: as a literal when it has a control character, ends in a space, or is empty. */
priv fn visible(line: String): String do
    ret quote(line) if line.len == 0
    val last = line[line.len - 1]
    ret quote(line) if last == ' ' or last == '\t'
    for c in line do
        ret quote(line) if (c >= 0 and c < ' ') or c == 127
    ret line

/** The lines of a diff of `a` and `b`, which have `from` lines in common at their start and `end` at their end. */
priv fn compareLines(out: @List<DiffLine>, a: String[], b: String[], from: Size, end: Size) do
    val n = a.len - from - end
    val m = b.len - from - end
    if n * m > DIFF_LIMIT do
        for i in 0..<n do
            out.push(DiffLine { '-', a[from + i] })
        for j in 0..<m do
            out.push(DiffLine { '+', b[from + j] })
    else do
        // common[i * width + j] is the length of the longest common subsequence of what follows line i and line j.
        val width = m + 1
        val common = new Size[(n + 1) * width]
        var i = n
        while i > 0 do
            i -= 1
            var j = m
            while j > 0 do
                j -= 1
                if a[from + i] == b[from + j] do
                    common[i * width + j] = common[(i + 1) * width + j + 1] + 1
                else do
                    val down = common[(i + 1) * width + j]
                    val right = common[i * width + j + 1]
                    common[i * width + j] = if down > right do down else right
        i = 0
        var j2: Size = 0
        while i < n or j2 < m do
            if i < n and j2 < m and a[from + i] == b[from + j2] do
                out.push(DiffLine { ' ', a[from + i] })
                i += 1
                j2 += 1
            else if i < n and (j2 >= m or common[(i + 1) * width + j2] >= common[i * width + j2 + 1]) do
                out.push(DiffLine { '-', a[from + i] })
                i += 1
            else do
                out.push(DiffLine { '+', b[from + j2] })
                j2 += 1

/**
 * A diff of two texts, line by line: the lines only in `expected` start with "- ", those only in `actual` with "+ ", and
 * the two lines around a change with two spaces. Other lines are left out, with a "...". What can't be seen in a line is
 * shown as a literal: an ending space, a "\r".
 * @return the diff, with a line break after each line; "" when the texts are the same
 */
fn diff(expected: String, actual: String): String do
    ret "" if expected == actual
    var a = expected.split('\n')
    var b = actual.split('\n')
    // When both end with a line break, that isn't one more line to show.
    if a[a.len - 1].len == 0 and b[b.len - 1].len == 0 and a.len > 1 and b.len > 1 do
        a = a[0..<a.len - 1]
        b = b[0..<b.len - 1]
    var from: Size = 0
    while from < a.len and from < b.len and a[from] == b[from] do
        from += 1
    var end: Size = 0
    while end < a.len - from and end < b.len - from and a[a.len - 1 - end] == b[b.len - 1 - end] do
        end += 1
    val lines: @List<DiffLine> = list(a.len + b.len)
    for i in 0..<from do
        lines.push(DiffLine { ' ', a[i] })
    compareLines(lines, a, b, from, end)
    for i in a.len - end..<a.len do
        lines.push(DiffLine { ' ', a[i] })

    val shownLines = new Bool[lines.len]
    for i, line in lines.items() where line.mark != ' ' do
        val first = if i < DIFF_CONTEXT do 0 else i - DIFF_CONTEXT
        val last = if i + DIFF_CONTEXT >= lines.len do lines.len - 1 else i + DIFF_CONTEXT
        for k in first..=last do
            shownLines[k] = true
    val out = stringBuilder(256)
    var skipped = false
    for i, line in lines.items() do
        if shownLines[i] do
            out.append("...\n") if skipped
            skipped = false
            if line.mark == ' ' do
                out.append("  ${line.text}\n")
            else do
                val text = visible(line.text)
                out.append("${line.mark} ${text}\n")
        else do
            skipped = true
    out.append("...\n") if skipped
    ret out.build()

/** Each line of `text` with `prefix` before it. */
priv fn indented(text: String, prefix: String): String do
    val out = stringBuilder(text.len + 64)
    for line in text.lines() do
        out.append(prefix)
        out.appendLine(line)
    ret out.build()

/** Whether byte `i` of `s`, if it has one, is inside a code point, and not at its start. */
priv fn insideCodePoint(s: String, i: Size): Bool = i < s.len and (s[i] as UChar) & 0xC0 == 0x80

/** The byte where two different Strings first differ: the start of the code point, when that is where they do. */
priv fn firstDifference(a: String, b: String): Size do
    var i: Size = 0
    while i < a.len and i < b.len and a[i] == b[i] do
        i += 1
    while i > 0 and (insideCodePoint(a, i) or insideCodePoint(b, i)) do
        i -= 1
    ret i

/** Where to start showing two Strings, the longer of them `longest` bytes, that first differ at byte `at`. */
priv fn windowStart(s: String, longest: Size, at: Size): Size do
    ret 0 if longest <= SHOWN_LIMIT or at < SHOWN_LIMIT / 2
    var start = at - SHOWN_LIMIT / 2
    while insideCodePoint(s, start) do
        start -= 1
    ret start

/** Shows `s` as a literal from `start`, saying so when something is cut off. */
priv fn literal(s: String, start: Size): String do
    var end = if start + SHOWN_LIMIT >= s.len do s.len else start + SHOWN_LIMIT
    while insideCodePoint(s, end) do
        end += 1
    ret concat(if start > 0 do "..." else "", quote(s[start..<end]), if end < s.len do "..." else "")

/** The detail lines for two Strings of one line each: both, with a mark under the first difference. */
priv fn oneLine(actual: String, expected: String): String do
    val at = firstDifference(actual, expected)
    val start = windowStart(actual, if actual.len > expected.len do actual.len else expected.len, at)
    val shownExpected = literal(expected, start)
    val shownActual = literal(actual, start)
    val before = quote(actual[start..<at])
    // Without the closing quote of what comes before the difference.
    val dots: Size = if start > 0 do 3 else 0
    val mark = repeat(" ", dots + unicode.width(before) - 1)
    ret "the strings differ at byte ${at}\n      expected: ${shownExpected}\n      actual:   ${shownActual}\n                ${mark}^\n"

// ---------------------------------------------------------------------------------------------------
// Assertions

/**
 * Checks a condition.
 * @param t the test
 * @param condition what must be true
 * @param what what is being checked, for the report
 */
fn isTrue(t: @Test, condition: Bool, what: String = "") do
    failure(t, what, "this should be true, and it is false") if not counted(t, condition)

/**
 * Checks that a condition is false.
 * @param t the test
 * @param condition what must be false
 * @param what what is being checked, for the report
 */
fn isFalse(t: @Test, condition: Bool, what: String = "") do
    failure(t, what, "this should be false, and it is true") if not counted(t, not condition)

/**
 * Checks that two values are equal, with `==`. They can be of any type `==` compares and `${...}` shows.
 * @param t the test
 * @param actual what the code gave
 * @param expected what it should give
 * @param what what is being checked, for the report
 */
fn equal(t: @Test, actual: <V>, expected: <V>, what: String = "") do
    failure(t, what, values(shown(actual), shown(expected))) if not counted(t, actual == expected)

/**
 * Checks that two Strings are equal. The report shows where they differ: with a diff when they have several lines.
 * @param t the test
 * @param actual what the code gave
 * @param expected what it should give
 * @param what what is being checked, for the report
 */
fn equal(t: @Test, actual: String, expected: String, what: String = "") do
    if not counted(t, actual == expected) do
        if has(actual, "\n") or has(expected, "\n") do
            failure(t, what, concat("the strings differ (- expected, + actual)\n", indented(diff(expected, actual), "      ")))
        else do
            failure(t, what, oneLine(actual, expected))

/**
 * Checks that two atoms are the same.
 * @param t the test
 * @param actual what the code gave
 * @param expected what it should give
 * @param what what is being checked, for the report
 */
fn equal(t: @Test, actual: Atom, expected: Atom, what: String = "") do
    failure(t, what, values("${actual}", "${expected}")) if not counted(t, actual == expected)

/**
 * Checks that two arrays have the same elements. The report names the first one that differs.
 * @param t the test
 * @param actual what the code gave
 * @param expected what it should give
 * @param what what is being checked, for the report
 */
fn equalArrays(t: @Test, actual: <V>[], expected: <V>[], what: String = "") do
    var at: Size? = null
    val shorter = if actual.len < expected.len do actual.len else expected.len
    for i in 0..<shorter do
        if actual[i] != expected[i] do
            at = i
            break
    if not counted(t, at == null and actual.len == expected.len) do
        val intro = if at != null do format("element ${at} differs") else format("the lengths differ: ${expected.len} expected, ${actual.len} here")
        failure(t, what, concat(intro, "\n      expected: ", shown(expected), "\n      actual:   ", shown(actual), "\n"))

/**
 * Checks that two values aren't equal.
 * @param t the test
 * @param actual what the code gave
 * @param other what it shouldn't give
 * @param what what is being checked, for the report
 */
fn notEqual(t: @Test, actual: <V>, other: <V>, what: String = "") do
    failure(t, what, concat("these should differ, and both are ", shown(actual))) if not counted(t, actual != other)

/**
 * Checks that two numbers are close, for results of floating point arithmetic.
 * @param t the test
 * @param actual what the code gave
 * @param expected what it should give
 * @param tolerance how far off it may be
 * @param what what is being checked, for the report
 */
fn near(t: @Test, actual: Double, expected: Double, tolerance: Double = 0.000000001, what: String = "") do
    val distance = if actual < expected do expected - actual else actual - expected
    // Written this way, a NaN fails.
    if not counted(t, distance <= tolerance) do
        failure(t, what, "these should be within ${tolerance} of each other\n      expected: ${expected}\n      actual:   ${actual}\n")

/**
 * Checks that a value is null.
 * @param t the test
 * @param actual what the code gave
 * @param what what is being checked, for the report
 */
fn isNull(t: @Test, actual: <V>?, what: String = "") do
    if val value = actual do
        counted(t, false)
        failure(t, what, concat("this should be null, and it is ", shown(value)))
    else do
        counted(t, true)

/**
 * Checks that a value isn't null.
 * @param t the test
 * @param actual what the code gave
 * @param what what is being checked, for the report
 */
fn notNull(t: @Test, actual: <V>?, what: String = "") do
    failure(t, what, "this should have a value, and it is null") if not counted(t, actual != null)

/**
 * Checks that a String has another one in it.
 * @param t the test
 * @param actual what the code gave
 * @param part what should be in it
 * @param what what is being checked, for the report
 */
fn hasText(t: @Test, actual: String, part: String, what: String = "") do
    if not counted(t, has(actual, part)) do
        val wanted = quote(part)
        val got = quote(actual)
        failure(t, what, "the text doesn't have this in it\n      wanted: ${wanted}\n      actual: ${got}\n")

/**
 * Fails the test, for a place the code shouldn't reach, and goes on.
 * @param t the test
 * @param message what went wrong
 */
fn fail(t: @Test, message: String) do
    counted(t, false)
    failure(t, "", message)

/**
 * Checks something the rest of the test can't do without: `try test.require(t, list.len > 0, "the list")`.
 * @param t the test
 * @param condition what must be true
 * @param what what is being checked, for the report
 * @throws when it is false, which ends a test that `throws Error`
 */
fn require(t: @Test, condition: Bool, what: String = "") throws Error do
    isTrue(t, condition, what)
    if not condition do
        t.stopped = true
        throw Error { ERR_FAILED, "a required check failed" }
