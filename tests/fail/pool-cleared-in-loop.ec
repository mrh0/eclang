// error: Can't use 's' here: it points to memory of pool 'p' that an earlier round of the loop at line 7
use "core:io.ec"

fn main(): Int do
    val p = newPool()
    val s = format(p, "hello ${1}")
    for i in 0..<3 do
        io.log(s)
        clearPool(p)
    ret 0
