// panic: divide-by-zero.ec:8: division by zero
use "core:io.ec"

fn main(): Int do
    var z = 4
    io.log(100 / z)
    z = 0
    io.log(10 / z)
    ret 0
