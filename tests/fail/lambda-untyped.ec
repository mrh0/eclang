// error: The type of the lambda parameter 'x' isn't known here. Write it, e.g. `fn (x: Int) = ...`.
fn main(): Int do
    val f = fn (x) = x + 1
    ret 0
