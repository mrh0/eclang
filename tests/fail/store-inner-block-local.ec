// error: Can't store this in 'p': it points to 'y', a local variable, which is freed when its block ends, but 'p' lives longer
fn main(): Int do
    var x = 1
    var p = addrof x
    if x > 0 do
        var y = 2
        p = addrof y
    ret @p
