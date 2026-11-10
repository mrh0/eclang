// error: Can't use the pool 'h2.p' here: `destroy h.p` at line 7 freed it
rec H as
    p: Pool
fn main(): Int do
    val h = H { p = new Pool }
    val h2 = h
    destroy h.p
    destroy h2.p
    ret 0
