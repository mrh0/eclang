// panic: index -1 is out of bounds for length 2
fn main(): Int do
    var a: Int[2]
    var i = 0
    i -= 1
    a[i] = 1
    ret 0
