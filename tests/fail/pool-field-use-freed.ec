// error: Can't use 's' here: it points to memory of pool 'h.p' that `freePool(h.p)` at line 8 freed
use "core:io.ec"
rec H as
    p: Pool
fn main(): Int do
    val h = H { p = newPool() }
    val s = format(h.p, "x${1}")
    freePool(h.p)
    io.log(s)
    ret 0
