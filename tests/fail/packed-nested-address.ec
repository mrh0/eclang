// error: inside a `packed` record
rec I as
    x: Int

packed rec P as
    a: UChar
    i: I

fn main(): Int do
    var p = P { 1, I { 2 } }
    val q = addrof p.i.x
    ret 0
