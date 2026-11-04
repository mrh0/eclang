// error: Only arrays and Strings can be sliced, but this is a Int[]c. A C array has no length
fn main(): Int do
    val a = [1, 2, 3]c
    val b = a[0..<2]
    ret 0
