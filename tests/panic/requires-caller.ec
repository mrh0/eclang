// panic: requires-caller.ec:8: requirement of half failed: n % 2 == 0
use "core:io.ec"

fn half(n: Int): Int requires n % 2 == 0 = n / 2

fn main(): Int do
    io.log(half(4))
    io.log(half(3))
    ret 0
