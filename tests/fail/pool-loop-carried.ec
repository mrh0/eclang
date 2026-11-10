// error: Can't use 's' here: from the second round of the loop on, it points to memory of pool 'p'
use "core:io.ec"
fn main(): Int do
    val p = new Pool
    var s = ""
    for i in 0..<3 do
        io.log(s)
        s = format(p, "x${i}")
        clearPool(p)
    ret 0
