// error: Can't use 's' here: it points to memory of pool 'h.p' that `destroy h.p` at line 8 freed
use "core:io.ec"
rec H as
    p: Pool
fn main(): Int do
    val h = H { p = new Pool }
    val s = format(h.p, "x${1}")
    destroy h.p
    io.log(s)
    ret 0
