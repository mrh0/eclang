// error: Can't return this: it points to 'x', a local variable
fn f(): @Int do
    var x = 1
    val a = [addrof x]
    for e in a do
        ret e
    ret new 1
fn main(): Int do
    f()
    ret 0
