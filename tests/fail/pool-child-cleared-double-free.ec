// error: Can't use the pool 'c' here: `clearPool(p)` at line 5 freed it
fn main(): Int do
    val p = new Pool
    val c = new Pool in p
    clearPool(p)
    destroy c
    ret 0
