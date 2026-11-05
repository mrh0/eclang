// error: Can't free 'outer': it holds the current pool
fn main(): Int do
    val outer = pool
    pool do
        freePool(outer)
    ret 0
