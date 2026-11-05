// panic: index 3 is out of bounds for length 3
use "core:io.ec"

fn main(): Int do
    var a: Int[3]
    var i = 2
    i += 1
    io.log(a[i])
    ret 0
