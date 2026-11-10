// error: Can't use 's' here: it points to memory of pool 'c' that `destroy p` at line 7 freed
use "core:io.ec"
fn main(): Int do
    val p = new Pool
    val c = new Pool in p
    val s = format(c, "x${1}")
    destroy p
    io.log(s)
    ret 0
