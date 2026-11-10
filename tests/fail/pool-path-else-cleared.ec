// error: Can't use 's' here: it points to memory of pool 'p' that `clearPool(p)` at line 11 freed.
use "core:io.ec"

fn main() do
    val p = new Pool
    var s = format(p, "old")
    val c = s.len > 1
    if c do
        s = format(p, "new")
    else do
        clearPool(p)
    io.log(s)
