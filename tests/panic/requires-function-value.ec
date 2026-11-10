// panic: requires-function-value.ec:4 (half called through a function value or from C): only even numbers
use "core:io.ec"

fn half(n: Int): Int requires n % 2 == 0, "only even numbers" = n / 2

fn main(): Int do
    val f = fn half
    io.log(f(3))
    ret 0
