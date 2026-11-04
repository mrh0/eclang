// error: Can't return this: it points to 'x', a local variable, which is freed when the function returns
fn leak(): @Int do
    var x = 42
    ret addrof x

fn main(): Int = 0
