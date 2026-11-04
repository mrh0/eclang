// error: f(...) can fail, and `?.` has no way to handle that
use "core:errors.ec"

fn f(x: @Int): Int throws Error do
    ret @x

fn main(): Int do
    val a: @Int? = new 1
    val b = a?.f()
    ret 0
