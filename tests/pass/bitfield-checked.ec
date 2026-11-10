// Values that fit a bit-field are stored as they are; see tests/panic/bitfield-* for those that don't.
use "core:io.ec"

mut rec Flags as
    mode: UInt : 3
    delta: Int : 4
    on: Bool : 1
    small: UChar : 8

fn make(m: UInt, d: Int): Flags = Flags { mode = m, delta = d, on = true, small = 1 }

fn main() do
    var f = make(7, -8)
    io.log("${f.mode} ${f.delta}")
    val m: UInt = 5
    f.mode = m
    f.delta = 7
    val b: UChar = 200
    f.small = b
    f.mode -= 2
    f.delta += -15
    f.mode <<= 1
    io.log("${f}")
    val g = Flags { ...f, mode = 1 + f.mode }
    io.log(g.mode)
