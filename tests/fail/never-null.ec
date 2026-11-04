// error: A value of type Int can never be null
fn main(): Int do
    val x = 5
    if x == null do
        ret 1
    ret 0
