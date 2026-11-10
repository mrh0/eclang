// error: `const assert a in pool` failed: 'a' may hold memory of the pool p.
rec Node as
    value: Int
fn main(): Int do
    val p = new Pool
    defer destroy p
    val a = new Node { value = 1 } in p
    const assert a in pool
    ret 0
