// error: (a, b)` names 2 parts, but this tuple has 3
fn main(): Int do
    val (a, b) = (1, 2, 3)
    ret a
