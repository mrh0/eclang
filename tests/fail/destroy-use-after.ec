// error: Can't use the pool 'p' here: `destroy p` at line 4 freed it
fn main(): Int do
    val p = new Pool
    destroy p
    val s = format(p, "x${1}")
    ret 0
