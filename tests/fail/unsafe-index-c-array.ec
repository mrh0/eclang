// error: `[]` on a Int[]c isn't checked
fn main(): Int do
    val c = [1, 2, 3]c
    ret c[0]
