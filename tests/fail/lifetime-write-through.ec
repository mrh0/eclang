// error: Can't return this: it points to 'x', a local variable
var g = 0
fn f(): @Int do
    var x = 1
    var o: @Int = addrof g
    val pp = addrof o
    @pp = addrof x
    ret o
fn main(): Int do
    f()
    ret 0
