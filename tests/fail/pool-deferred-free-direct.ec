// error: it points to memory of pool 'p', and `destroy p` at line 4 frees that when the function leaves
fn f(): String do
    val p = new Pool
    defer destroy p
    ret format(p, "x${1}")
fn main(): Int do
    ret f().len as Int
