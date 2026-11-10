// error: `+` needs two numbers, but got Int? and 1.

fn f(mut x: Int?): Int do
    if x != null do
        x = null
        ret x + 1
    ret 0

fn main() do
    var a: Int? = 1
    val r = f(mut a)
