// error: that `clearPool(p)` at line 7 freed
use "core:io.ec"

fn main(): Int do
    val p = newPool()
    val (a, b) = (new 1 in p, 2)
    clearPool(p)
    io.log(@a)
    ret 0
