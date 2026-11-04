// error: `if val` needs a value that may be null (a `T?`) or a call that can fail, but this is always a Int.
fn main(): Int do
    val a = 3
    if val b = a do
        ret b
    ret 0
