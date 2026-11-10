// error: 'x' isn't a parameter of f
fn f(n: Int): Int do
    val x = n
    const assert not escapes(x)
    ret x
fn main(): Int do
    ret f(1)
