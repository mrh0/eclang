// error: Can't use 'pt' here: it points to memory of pool 'p' that `destroy p` at line 10 freed.
use "core:io.ec"

rec Point as
    x: Int

fn main(): Int do
    val p = new Pool
    val pt = new Point { x = 1 } in p
    destroy p
    io.log(pt.x)
    ret 0
