// panic: bitfield-compound.ec:9: -9 doesn't fit bit-field 'delta', which has 4 bit(s): it holds -8 to 7
use "core:io.ec"

mut rec Flags as
    delta: Int : 4

fn main(): Int do
    var f = Flags { -8 }
    f.delta -= 1
    io.log("${f}")
    ret 0
