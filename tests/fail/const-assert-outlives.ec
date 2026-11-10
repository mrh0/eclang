// error: which is freed when that block ends, before 'x', a local variable
rec Node as
    value: Int
fn main(): Int do
    val x = Node { value = 3 }
    val b = addrof x
    pool do
        val d = new Node { value = 4 }
        const assert d outlives b
    ret 0
