// panic: bitfield-too-large.ec:9: 8 doesn't fit bit-field 'mode', which has 3 bit(s): it holds 0 to 7
use "core:io.ec"

rec Flags as
    mode: UInt : 3

fn main(): Int do
    val m: UInt = 8
    var f = Flags { m }
    io.log("${f}")
    ret 0
