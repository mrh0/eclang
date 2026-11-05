// error: Can't return the pool 'p': `freePool(p)` at line 4 frees it
fn f(): Pool do
    val p = newPool()
    defer freePool(p)
    ret p
fn main(): Int do
    f()
    ret 0
