// error: it points to memory of pool 'p', and `freePool(p)` at line 4 frees that when the function leaves
fn f(): String do
    val p = newPool()
    defer freePool(p)
    val s = format(p, "x${1}")
    ret s
fn main(): Int do
    ret f().len as Int
