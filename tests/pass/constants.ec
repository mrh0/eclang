use "core:io.ec"
use "modules/geo.ec"

// `const`: values known at compile time.

const LIMIT = 10
const TWICE = LIMIT * 2
const BIG: Long = 5000000000
const GREETING = "hello"c
const READY = true
const RATE = 0.5

// Other globals can use constants, also from another module.
val scaled = geo.UNIT * TWICE

fn main(): Int do
    io.log(LIMIT)
    io.log(TWICE)
    io.log(BIG)
    io.log(GREETING)
    io.log(READY)
    io.log(RATE)
    io.log(scaled)

    // An untyped constant adapts like a literal.
    val c: Char = LIMIT + 55
    io.log(c)
    val small: UChar = TWICE
    io.log(small)

    const LOCAL = TWICE + 1
    io.log(LOCAL)
    ret 0
