// error: Can't use 's' here: from the second round of the loop on, it points to memory of pool 'p'
use "core:io.ec"

fn main() do
    val p = new Pool
    var s = format(p, "start")
    for i in 0..<4 do
        io.log(s)
        if i % 2 == 1 do
            clearPool(p)
