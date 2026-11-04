// error: is never null, so `?.` is not needed. Write `.f(...)` instead
fn f(x: Int): Int = x

fn main(): Int do
    val a = 1
    ret a?.f() ?? 0
