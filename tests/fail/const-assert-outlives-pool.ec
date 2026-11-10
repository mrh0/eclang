// error: which this function can free (`clearPool`, `destroy`)
rec Node as
    value: Int
fn main(): Int do
    val p = new Pool
    defer destroy p
    val a = new Node { value = 1 } in p
    pool do
        val d = new Node { value = 4 }
        const assert a outlives d
    ret 0
