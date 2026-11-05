// error: Can't return this: it points to 'x', a local variable
fn id(p: @Int): @Int = p
fn f(): @Int do
    var x = 1
    when id(addrof x) do
        q if @q == 1 do
            ret q
    ret new 1
fn main(): Int do
    f()
    ret 0
