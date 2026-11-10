// `|` and `&` take two operands each in the grammar, and the Visitor flattens them into one union: with a list of
// operands as well, a union of 40 types took longer to parse than anyone would wait. `A | B[]` is `A | (B[])`, and
// `A | B throws E` throws from the whole union.
use "core:io.ec"
use "core:errors.ec"

fn name(a: :a0 | :a1 | :a2 | :a3 | :a4 | :a5 | :a6 | :a7 | :a8 | :a9 | :a10 | :a11 | :a12 | :a13 | :a14 | :a15 | :a16 | :a17 | :a18 | :a19 | :a20 | :a21 | :a22 | :a23 | :a24 | :a25 | :a26 | :a27 | :a28 | :a29 | :a30 | :a31 | :a32 | :a33 | :a34 | :a35 | :a36 | :a37 | :a38 | :a39): String do
    ret "first" if a == :a0
    ret "last" if a == :a39
    ret "other"

fn count(x: Int | String[]): Int do
    if x is Int do
        ret x
    else do
        ret x.len as Int

fn parse(n: Int): Int | String throws Error do
    throw Error { 1, "negative" } if n < 0
    ret "ten" if n == 10
    ret n

fn mixed(v: :x & :y | :z & :w): String = if v == :w do "w" else "not w"

fn main(): Int do
    io.log(name(:a0))
    io.log(name(:a39))
    io.log(name(:a20))
    io.log(count(5))
    io.log(count(["a", "b", "c"]))
    for n in [10, 3, 0 - 1] do
        val v = parse(n) ?? 0
        if v is Int do
            io.log("Int ${v}")
        else do
            io.log("String ${v}")
    io.log(mixed(:w))
    io.log(mixed(:x))
    ret 0
