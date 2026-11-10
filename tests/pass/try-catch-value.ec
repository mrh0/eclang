// `val x = try f() catch e do ...`: the value of f, or the value after `do` (or a block that leaves) when f fails.
use "core:io.ec"

fn half(i: Int): Int throws Error do
    throw Error { i, "odd: ${i}" } if i % 2 != 0
    ret i / 2

fn name(i: Int): String throws Error do
    throw Error { 1, "no name" } if i < 0
    ret "n${i}"

fn twice(i: Int): Int throws Error do
    val h = try half(i) catch e do
        io.log("half failed: ${e.message}")
        throw Error { 2, "twice: ${e.message}" }
    ret h * 2

fn main() do
    val a = try half(4) catch e do -1
    val b = try half(3) catch e do -e.code
    io.log("${a} ${b}")
    var total = 0
    for i in [1, 2, 3, 4, 6] do
        val h = try half(i) catch e do
            io.log("skip ${e.message}")
            continue
        total += h
    io.log("total ${total}")
    val s = try name(-1) catch e do e.message
    val t = try name(3) catch e do "?"
    val u = t
    io.log("${s} ${u}")
    var c = 0
    c = try twice(5) catch e do 99
    io.log("${c}")
    io.log("${twice(8) ?? 0}")
