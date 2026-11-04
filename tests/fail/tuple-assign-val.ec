// error: 'a' is a `val`
fn main(): Int do
    val a = 1
    var b = 2
    (a, b) = (3, 4)
    ret b
