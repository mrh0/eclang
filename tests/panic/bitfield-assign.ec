// panic: bitfield-assign.ec:9: 4294967295 doesn't fit bit-field 'mode', which has 3 bit(s): it holds 0 to 7
use "core:io.ec"

mut rec Flags as
    mode: UInt : 3

fn main(): Int do
    var f = Flags { 0 }
    f.mode -= 1
    io.log("${f}")
    ret 0
