// Four union parameters make 81 copies of `describe` (and a warning); only the copies that are called end up in the C
// file. In most copies some parameter goes unused, which must not cause a C warning.
use "core:io.ec"

fn describe(a: Int | CString | Double, b: Int | CString | Double, c: Int | CString | Double, d: Int | CString | Double) do
    io.log(a) if a is Int
    io.log(b) if b is CString
    io.log(c) if c is Double
    io.log(d) if d is Int

fn twice(x: Int): Int = x * 2

fn neverCalled(x: Int | CString) do
    io.log(twice(1))

fn main(): Int do
    describe(1, "two"c, 3.5, 4)
    describe("one"c, 2, 3, "four"c)
    val f = fn (n: Int) = twice(n) + 1
    io.log(f(20))
    ret 0
