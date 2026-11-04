module "fib"

use "core:lib.ec"

fn main(): Int do
    var a = 0i
    var b = 1i
    for i in 0..<40 do
        log(a)
        val next = a + b
        a = b
        b = next
    ret 0
