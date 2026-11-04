// error: Cannot use 1.5 as Int (right side of `+=` on 'a')
fn main(): Int do
    var a = 1
    a += 1.5
    ret a
