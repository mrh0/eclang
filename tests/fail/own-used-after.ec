// error: Can't use 's' here: it points to memory of pool 'p' that `drop(...)` at line 10, which took the pool over (`own`), freed
use "core:io.ec"

fn drop(own p: Pool) do
    destroy p

fn main(): Int do
    val p = new Pool
    val s = format(p, "x ${1}")
    drop(p)
    io.log(s)
    ret 0
