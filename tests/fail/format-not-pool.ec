// error: `format` takes a String with `${...}` in it, and optionally the pool to allocate it in first
fn main(): Int do
    val n = 3
    val s = format(n, "n is ${n}")
    ret 0
