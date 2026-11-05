// error: inside a `packed` record
packed rec P as
    a: UChar
    b: Int

fn main(): Int do
    var p = P { 1, 2 }
    val q = addrof p.b
    ret 0
