// panic: shift-too-far.ec:6: can't shift by 40 bits: the value has 32
use "core:io.ec"

fn main(): Int do
    var s = 40
    io.log(1 << s)
    ret 0
