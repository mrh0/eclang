// error: Can't use the pool 'h2.p' here: `freePool(h.p)` at line 7 freed it
rec H as
    p: Pool
fn main(): Int do
    val h = H { p = newPool() }
    val h2 = h
    freePool(h.p)
    freePool(h2.p)
    ret 0
