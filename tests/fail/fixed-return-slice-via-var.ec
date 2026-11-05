// error: it points to 'a', a local variable
fn f(): Int[] do
    var a: Int[2]
    val s = a[0..<1]
    ret s

fn main(): Int do
    ret 0
