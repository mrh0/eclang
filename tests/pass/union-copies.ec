// Four union parameters make 81 copies of `describe` (and a warning); only the copies that are called end up in the C
// file. In most copies some parameter goes unused, which must not cause a C warning.
use "core:lib.ec"

fn describe(a: Int | CString | Double, b: Int | CString | Double, c: Int | CString | Double, d: Int | CString | Double) do
    log(a) if a is Int
    log(b) if b is CString
    log(c) if c is Double
    log(d) if d is Int

fn twice(x: Int): Int = x * 2

fn neverCalled(x: Int | CString) do
    log(twice(1))

fn main(): Int do
    describe(1, "two"c, 3.5, 4)
    describe("one"c, 2, 3, "four"c)
    val f = fn (n: Int) = twice(n) + 1
    log(f(20))
    ret 0
