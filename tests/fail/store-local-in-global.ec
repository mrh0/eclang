// error: Can't store this in 'saved': it points to 'x', a local variable, which is freed when its block ends, but 'saved' outlives this function
var saved: @Int? = null

fn keep() do
    var x = 1
    saved = addrof x

fn main(): Int = 0
