// error: it points to 'x'
fn f(p: @Int): @Int do
    var x = 1
    var a = addrof x
    var b = p
    (a, b) = (b, a)
    ret b

fn main(): Int = 0
