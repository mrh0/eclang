// error: it points to memory from the pool of the `pool` block at line 4
fn make(n: Int, inner: Bool): (Int) => Int do
    if inner do
        pool do
            val f = fn (x: Int) = x + n
            ret f
    ret fn (x: Int) = x
fn main(): Int do
    val f = make(1, true)
    ret f(1)
