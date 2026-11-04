// error: Can't use 's' here: it points to memory of pool 'p' that `clearPool(p)` at line 7 freed.
use "core:io.ec"

fn main(): Int do
    val p = newPool()
    val s = format(p, "hello ${1}")
    clearPool(p)
    io.log(s)
    ret 0
