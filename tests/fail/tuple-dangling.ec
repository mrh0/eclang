// error: it points to 'x'
fn f(): @Int do
    var x = 1
    val (a, b) = (addrof x, 2)
    ret a

fn main(): Int = 0
