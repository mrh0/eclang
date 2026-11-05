// error: Can't use 's' here: it points to memory of pool 'pool' that `clearPool(pool)` at line 6 freed
use "core:io.ec"
fn main(): Int do
    pool do
        val s = "x${1}"
        clearPool(pool)
        io.log(s)
    ret 0
