// error: Can't use 's' here: it points to memory of pool 'c' that `freePool(p)` at line 7 freed
use "core:io.ec"
fn main(): Int do
    val p = newPool()
    val c = newPool(p)
    val s = format(c, "x${1}")
    freePool(p)
    io.log(s)
    ret 0
