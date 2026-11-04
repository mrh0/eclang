// error: Can't use the pool 'p' here: `freePool(p)` at line 6 freed it.
use "core:io.ec"

fn main(): Int do
    val p = newPool()
    freePool(p)
    io.log(format(p, "x"))
    ret 0
