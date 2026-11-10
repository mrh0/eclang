// error: Can't return the pool 'p': `destroy p` at line 4 frees it
fn f(): Pool do
    val p = new Pool
    defer destroy p
    ret p
fn main(): Int do
    f()
    ret 0
