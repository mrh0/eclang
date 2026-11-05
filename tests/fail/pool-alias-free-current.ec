// error: Can't free 'q': it holds the current pool
fn main(): Int do
    val q = pool
    freePool(q)
    ret 0
