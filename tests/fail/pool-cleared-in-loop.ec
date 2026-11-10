// error: Can't use 's' here: from the second round of the loop on, it points to memory of pool 'p' that `clearPool(p)` at line 9 freed.
use "core:io.ec"

fn main(): Int do
    val p = new Pool
    val s = format(p, "hello ${1}")
    for i in 0..<3 do
        io.log(s)
        clearPool(p)
    ret 0
