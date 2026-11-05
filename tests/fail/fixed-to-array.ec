// error: slice it: `a[..]`
fn f(a: Int[]): Size = a.len

fn main(): Int do
    val a: Int[2] = [1, 2]
    f(a)
    ret 0
