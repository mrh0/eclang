// error: Can't assign to a slice
fn main(): Int do
    var a = [1, 2]
    a[1..] = a
    ret 0
