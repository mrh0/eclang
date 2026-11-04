// error: `a to b` is written as a range: `for i in 0..<3 do` stops before 3, and `0..=3` includes it.
fn main(): Int do
    for i in 0 to 3 do
        pass
    ret 0
