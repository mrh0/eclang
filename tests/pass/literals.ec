use "core:lib.ec"

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
    log(c)
    log(big)
    log(small)
    log(f)
    // Arithmetic on literals is computed at compile time, so it can still fit a small type.
    val folded: Char = 60 + 6
    log(folded)
    // Overloads: the closest match wins.
    log(pick(5))
    log(pick(5l))
    log(pick(c))
    val s: Short = 7
    log(pick(s))
    log(half(9))
    log(half(9.0))
    // Mixing: a literal adapts to the other side.
    val sum = c + 1
    log(sum)
    val d = 1.5 + 2
    log(d)
    log(0xFF)
    log(1_000_000)
    ret 0
