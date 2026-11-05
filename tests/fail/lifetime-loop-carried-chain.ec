// error: This happens from the second round of the loop on
var g = 0
fn f(): @Int do
    var x = 1
    var a: @Int = addrof g
    var b: @Int = addrof g
    var c: @Int = addrof g
    for i in 0..<4 do
        if i == 3 do
            ret c
        c = b
        b = a
        a = addrof x
    ret addrof g
fn main(): Int do
    f()
    ret 0
