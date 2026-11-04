// error: 'n' is already defined
fn main(): Int do
    val n = 1
    when 2 do
        n if n > 1 do ret 1
        else do ret 0
    ret 0
