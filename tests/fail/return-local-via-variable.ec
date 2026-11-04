// error: Can't return this: it points to 'x', a local parameter
fn leak(x: Int): @Int do
    val p = addrof x
    ret p

fn main(): Int = 0
