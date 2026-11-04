// error: An inline `if` is written like a statement `if`, with `do`: `if x > 0 do 1 else 0`.
fn main(): Int do
    val x = 1
    ret if (x > 0) 1 else 0
