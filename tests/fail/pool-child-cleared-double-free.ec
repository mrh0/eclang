// error: Can't use the pool 'c' here: `clearPool(p)` at line 5 freed it
fn main(): Int do
    val p = newPool()
    val c = newPool(p)
    clearPool(p)
    freePool(c)
    ret 0
