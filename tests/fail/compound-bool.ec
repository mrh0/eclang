// error: `|=` needs whole numbers, but 'a' is a Bool and the value is a Bool. For Bools write `x = x or y`.
fn main(): Int do
    var a = true
    a |= false
    ret 0
