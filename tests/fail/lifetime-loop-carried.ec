// error: This happens from the second round of the loop on
var g = 0
fn f(): @Int do
    var x = 1
    var p: @Int = addrof g
    for i in 0..<3 do
        if i == 1 do
            ret p
        p = addrof x
    ret addrof g
fn main(): Int do
    f()
    ret 0
