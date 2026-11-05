// error: it points to 'a', a local variable
fn f(): @Int do
    var a: Int[2]
    ret addrof a[1]

fn main(): Int do
    ret 0
