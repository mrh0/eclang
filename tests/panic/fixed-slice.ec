// panic: slice 1..<5 is out of bounds for length 3
fn main(): Int do
    var a: Int[3]
    var n = 4
    n += 1
    val s = a[1..<n]
    ret 0
