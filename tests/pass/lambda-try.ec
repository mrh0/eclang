use "core:io.ec"

// `try` in a lambda passes the error on from the lambda, which then can fail.

fn each(values: String[], f: (String) => Long throws Error): Long throws Error do
    var total: Long = 0
    for v in values do
        total += try f(v)
    ret total

fn main() do
    val offset: Long = 100
    // Without an expected type, the lambda throws what its `try`s can fail with.
    val parse = fn (s: String) = try parseInt(s) + offset
    val good = parse("12") ?? -1
    val bad = parse("x") ?? -1
    io.log("inferred: ${good} ${bad}")

    // Where a function type that `throws` is expected, the lambda has that type.
    val sum = each(["1", "2", "3"], fn (s) = try parseInt(s) * 2) ?? -1
    val failed = each(["1", "?"], fn (s) = try parseInt(s)) ?? -1
    io.log("expected: ${sum} ${failed}")

    // Or written out.
    val written = fn (s: String): Long throws Error = try parseInt(s) - offset
    val w = written("1") ?? 0
    io.log("written: ${w}")

    val caught = try parse("oops") catch e do -1
    io.log("caught: ${caught}")
