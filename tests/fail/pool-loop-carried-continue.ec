// error: Can't use 's' here: from the second round of the loop on, it points to memory of pool 'p'
use "core:io.ec"
fn main(): Int do
    val p = newPool()
    var s = ""
    for i in 0..<3 do
        io.log(s)
        if i == 1 do
            s = format(p, "x${i}")
            clearPool(p)
            continue
        s = ""
    ret 0
