// error: Can't `destroy p` here: `h.p`, the caller's memory, still holds this pool
mut rec H as
    p: Pool?

fn keep(h: @H) do
    val p = new Pool
    h.p = p
    destroy p

fn main(): Int do
    var h = H { p = null }
    keep(addrof h)
    ret 0
