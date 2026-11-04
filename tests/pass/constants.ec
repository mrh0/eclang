use "core:lib.ec"
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
    log(LIMIT)
    log(TWICE)
    log(BIG)
    log(GREETING)
    log(READY)
    log(RATE)
    log(scaled)

    // An untyped constant adapts like a literal.
    val c: Char = LIMIT + 55
    log(c)
    val small: UChar = TWICE
    log(small)

    const LOCAL = TWICE + 1
    log(LOCAL)
    ret 0
