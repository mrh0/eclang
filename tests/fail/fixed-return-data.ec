// error: it points to 'a', a local variable
fn f(): Int[]c do
    var a: Int[2]
    ret a.data

fn main(): Int do
    ret 0
