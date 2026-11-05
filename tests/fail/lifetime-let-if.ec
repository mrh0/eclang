// error: Can't return this: it points to 'x', a local variable
fn f(): @Int? do
    var x = 1
    val m: @Int? = addrof x
    if val q = m do
        ret q
    ret null
fn main(): Int do
    f()
    ret 0
