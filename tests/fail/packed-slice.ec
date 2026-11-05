// error: inside a `packed` record
packed rec P as
    a: UChar
    b: Int[2]

fn main(): Int do
    var p = P { 1, [1, 2] }
    val q = p.b[..]
    ret 0
