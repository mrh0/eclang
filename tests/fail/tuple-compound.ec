// error: `+=` changes one value
fn main(): Int do
    var a = 1
    var b = 2
    (a, b) += (1, 1)
    ret a
