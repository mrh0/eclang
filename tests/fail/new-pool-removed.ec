// error: A pool is made with `new Pool` (a child of the current pool), or `new Pool in parent`, not `newPool(...)`.
fn main(): Int do
    val p = newPool()
    ret 0
