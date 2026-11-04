// error: no loop around this is labelled 'outre'. Did you mean 'outer'?
fn main(): Int do
    outer: for i in 0..<3 do
        break outre
    ret 0
