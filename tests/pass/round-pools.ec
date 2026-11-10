// A loop whose rounds allocate in the current pool, where nothing a round allocates is used after it, gets a pool per
// round, as if its body were in `pool do`: every way out of a round frees it. What is kept (a list, `keep`, a returned
// value, an error passed on) stays where it was.

use "core:io.ec"
use "core:errors.ec"
use "core:structures.ec"

fn label(i: Int): String = "item-${i}"

fn total(n: Int): Size do
    var sum: Size = 0
    for i in 0..<n do
        sum += label(i).len
    ret sum

fn firstLong(n: Int): String do
    for i in 0..<n do
        val s = label(i * 1000)
        if s.len > 8 do
            ret s
    ret "none"

fn skipping(n: Int): Int do
    var count = 0
    var i = 0
    while i < n do
        i += 1
        val s = label(i)
        if s.len > 6 do
            break
        if i % 2 == 0 do
            continue
        count += s.len as Int
    ret count

fn nested(n: Int): Size do
    var sum: Size = 0
    for i in 0..<n do
        val outer = label(i)
        for j in 0..<n do
            sum += concat(outer, label(j)).len
    ret sum

fn kept(n: Int): List<String> do
    val out: List<String> = list()
    for i in 0..<n do
        val scratch = "${label(i)}-${label(i + 1)}"
        out.push(keep(scratch[0..<6]))
    ret out

fn check(i: Int): Int throws Error do
    if i == 3 do
        throw Error { 1, "bad ${i}" }
    ret i

fn failing(n: Int): Int throws Error do
    var sum = 0
    for i in 0..<n do
        val s = label(i)
        sum += try check(i) + s.len as Int
    ret sum

fn main(): Int do
    io.log(total(100000))
    io.log(firstLong(100))
    io.log(skipping(100))
    io.log(nested(20))
    for s in kept(3).items() do
        io.log(s)
    io.log(failing(3) ?? -1)
    try failing(5) catch err do
        io.log(err.message)
    ret 0
