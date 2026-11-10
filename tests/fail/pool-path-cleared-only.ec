// error: Can't use 's' here: it points to memory of pool 'p' that `clearPool(p)` at line 9 freed.
// A path that clears the pool without giving s a new value still leaves it dangling.
use "core:io.ec"

fn main() do
    val p = new Pool
    var s = format(p, "old")
    if true and s.len > 1 do
        clearPool(p)
    else do
        s = format(p, "new")
    io.log(s)
