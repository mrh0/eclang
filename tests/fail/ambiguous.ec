// error: fits several functions equally well
fn f(a: Int, b: Long) do
    pass

fn f(a: Long, b: Int) do
    pass

fn main(): Int do
    val s: Short = 1
    f(s, s)
    ret 0
