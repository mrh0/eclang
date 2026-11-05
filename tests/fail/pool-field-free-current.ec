// error: Can't free 'h2.p': it holds the current pool
rec H as
    p: Pool
fn main(): Int do
    val h = H { p = pool }
    val h2 = h
    freePool(h2.p)
    ret 0
