// panic: out of stack
use "core:io.ec"

fn big(): Int do
    var a: Int[100000000]
    a[99999999] = 1
    ret a[99999999] + a[0]

fn main() do
    io.log("${big()}")
