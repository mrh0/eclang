// error: 'h.p' is reached through the parameter 'h', which the caller may still use
rec H as
    p: Pool?

fn release(h: @H) do
    destroy h.p

fn main(): Int do
    var h = H { p = new Pool }
    release(addrof h)
    ret 0
