// error: Can't destroy 'h2.p': it holds the current pool
rec H as
    p: Pool
fn main(): Int do
    val h = H { p = pool }
    val h2 = h
    destroy h2.p
    ret 0
