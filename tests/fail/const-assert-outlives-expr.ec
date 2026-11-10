// error: `outlives` compares two variables or fields
fn main(): Int do
    val a = 1
    const assert a[] outlives a
    ret 0
