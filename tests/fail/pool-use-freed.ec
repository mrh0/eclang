// error: Can't use the pool 'p' here: `destroy p` at line 6 freed it.
use "core:io.ec"

fn main(): Int do
    val p = new Pool
    destroy p
    io.log(format(p, "x"))
    ret 0
