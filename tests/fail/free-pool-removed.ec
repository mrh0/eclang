// error: A pool is freed with the statement `destroy p`, not `freePool(p)`.
fn main(): Int do
    val p = new Pool
    freePool(p)
    ret 0
