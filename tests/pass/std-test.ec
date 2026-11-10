use "core:io.ec"
use "core:string.ec"
use "core:test.ec"

rec Point as
    x: Int
    name: String

fn passing(t: test.Test) do
    test.equal(t, 1 + 1, 2)
    test.equal(t, 5l, 5)
    test.equal(t, 1.5, 1.5)
    test.equal(t, true, true)
    test.equal(t, 'a', 'a')
    test.equal(t, :on, :on)
    test.equal(t, "same", "same")
    test.equal(t, Point { 1, "a" }, Point { 1, "a" })
    test.equal(t, (1, "a"), (1, "a"))
    test.equalArrays(t, [1, 2], [1, 2])
    test.equalArrays(t, ["a"], ["a"])
    test.notEqual(t, 1, 2)
    test.notEqual(t, "a", "b")
    test.isTrue(t, true)
    test.isFalse(t, false)
    test.near(t, 0.1 + 0.2, 0.3)
    test.near(t, 1.0, 1.05, 0.1)
    val none: Int? = null
    val some: Int? = 1
    test.isNull(t, none)
    test.notNull(t, some)
    test.hasText(t, "hello", "ell")

fn mismatches(t: test.Test) do
    test.equal(t, 1 + 1, 3)
    test.equal(t, 5l, 6, "the count")
    test.equal(t, 1.5, 2.5)
    test.equal(t, 'a', 'b')
    test.equal(t, true, false)
    test.equal(t, :on, :off)
    test.equal(t, Point { 1, "a" }, Point { 1, "b" })
    test.equal(t, (1, "a"), (2, "a"))
    test.equalArrays(t, [1, 2, 3], [1, 2, 4])
    test.equalArrays(t, [1, 2, 3], [1, 2])
    test.equalArrays(t, ["a"], ["b"], "the names")
    test.notEqual(t, 1, 1)
    test.notEqual(t, "a", "a", "the text")
    test.isTrue(t, false, "thing")
    test.isTrue(t, false)
    test.isFalse(t, true)
    test.near(t, 1.0, 1.1, 0.01)
    val some: String? = "x"
    val none: String? = null
    test.isNull(t, some, "the name")
    test.notNull(t, none)
    test.hasText(t, "hello", "z")
    test.fail(t, "this shouldn't happen")

fn strings(t: test.Test) do
    test.equal(t, "hello world", "hello there")
    test.equal(t, "abc", "abcd")
    test.equal(t, "", "x")
    test.equal(t, "tab\there", "tab\tthere")
    test.equal(t, "héllo wörld", "héllo wörlx")
    test.equal(t, "日本語x", "日本語y")
    // The last byte of a code point is what differs: the mark is at its start.
    test.equal(t, "aé", "aè")
    // Long strings are cut around the difference.
    val long = repeat("abcdefghij", 20)
    test.equal(t, concat(long, "X", long), concat(long, "Y", long))
    test.equal(t, concat("X", long), concat("Y", long))
    test.equal(t, concat(long, "X"), concat(long, "Y"))

fn diffs(t: test.Test) do
    test.equal(t, "a\nb\nc\nd\ne\nf\ng\nh\ni\n", "a\nb\nc\nd\nX\nf\ng\nh\ni\n", "one line")
    test.equal(t, "a\nb\nc\n", "a\nc\n", "a line too many")
    test.equal(t, "a\nc\n", "a\nb\nc\n", "a line missing")
    test.equal(t, "a\nb\n", "a\nb", "the last line break")
    test.equal(t, "a\r\nb\n", "a\nb\n", "a carriage return")
    test.equal(t, "a \nb\n", "a\nb\n", "a space at the end")
    test.equal(t, "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n", "0\n2\n3\n4\n5\n6\n7\n8\n9\nX\n", "two changes")
    test.equal(t, "\n\nx", "\ny", "empty lines")
    test.equal(t, "one", "one\ntwo", "only one has a line break")

fn sized(t: test.Test) do
    // Too large to compare line by line: it is shown as replaced.
    val b = stringBuilder()
    val c = stringBuilder()
    for i in 0..<2100 do
        b.append("left ${i}\n")
        c.append("right ${i}\n")
    val text = test.diff(b.build(), c.build())
    test.equal(t, text.lines().len, 4200)
    test.equal(t, text.lines()[0], "- left 0")
    test.equal(t, text.lines()[2100], "+ right 0")

fn stopping(t: test.Test) throws Error do
    val n = try parseInt("12")
    test.equal(t, n, 12)
    try test.require(t, n == 12, "passes")
    try test.require(t, n == 13, "the number")
    io.log("not reached")

fn failing(t: test.Test) throws Error do
    test.equal(t, 1, 1)
    val n = try parseInt("x")
    io.log("not reached")

fn fine(t: test.Test) throws Error do
    val n = try parseInt("12")
    test.equal(t, n, 12)

fn main(): Int do
    io.log(test.diff("same", "same").len)
    io.print(test.diff("a\nb", "a\nc"))

    // NaN is near nothing.
    var zero = 0.0
    val t = new test.Test { "nan", 0, 0, stringBuilder() }
    test.near(t, zero / zero, 1.0)
    io.log(t.failures)

    val s = test.suite()
    test.run(s, "passing", fn passing)
    test.run(s, "mismatches", fn mismatches)
    test.run(s, "strings", fn strings)
    test.run(s, "diffs", fn diffs)
    test.run(s, "sized", fn sized)
    test.run(s, "stopping", fn stopping)
    test.run(s, "failing", fn failing)
    test.run(s, "fine", fn fine)
    io.log(test.finish(s))

    // A filter leaves tests out.
    val some = test.suite("pass")
    test.run(some, "passing", fn passing)
    test.run(some, "mismatches", fn mismatches)
    test.run(some, "stopping", fn stopping)
    io.log(test.finish(some))

    val none = test.suite("nothing")
    test.run(none, "passing", fn passing)
    io.log(test.finish(none))
    ret 0
