// error: so its fields can't change either
fn main(): Int do
    val a: Int[2] = [1, 2]
    a[0] = 3
    ret 0
