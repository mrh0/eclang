// error: 'h.p' is the pool of a `pool` block
rec H as
    p: Pool
fn main(): Int do
    pool x do
        val h = H { p = x }
        freePool(h.p)
    ret 0
