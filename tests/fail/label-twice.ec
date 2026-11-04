// error: A loop around this one is already labelled 'outer'
fn main(): Int do
    outer: for i in 0..<3 do
        outer: for j in 0..<3 do
            break outer
    ret 0
