// error: Can't return this: it points to 'x', a local variable
rec R as
    p: @Any
fn f(): R do
    var x = 1
    ret clone(R { p = addrof x })
fn main(): Int do
    f()
    ret 0
