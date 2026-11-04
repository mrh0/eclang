use "core:io.ec"

fn half(x: Int): Int = x / 2
fn half(x: Double): Double = x / 2

fn pick(x: Int): CString = "Int"c
fn pick(x: Long): CString = "Long"c
fn pick(x: Char): CString = "Char"c

fn main(): Int do
    // Untyped literals take the type they are used as.
    val c: Char = 65
    val big: Long = 5000000000
    val small: UChar = 255
    val f: Float = 2.5
    io.log(c)
    io.log(big)
    io.log(small)
    io.log(f)
    // Arithmetic on literals is computed at compile time, so it can still fit a small type.
    val folded: Char = 60 + 6
    io.log(folded)
    // Overloads: the closest match wins.
    io.log(pick(5))
    io.log(pick(5l))
    io.log(pick(c))
    val s: Short = 7
    io.log(pick(s))
    io.log(half(9))
    io.log(half(9.0))
    // Mixing: a literal adapts to the other side.
    val sum = c + 1
    io.log(sum)
    val d = 1.5 + 2
    io.log(d)
    io.log(0xFF)
    io.log(1_000_000)
    ret 0
