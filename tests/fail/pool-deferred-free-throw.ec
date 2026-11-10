// error: Can't throw this: it points to memory of pool 'p'
fn f(): Int throws String do
    val p = new Pool
    defer destroy p
    throw format(p, "x${1}")
fn main(): Int do
    ret f() ?? 0
