// error: Can't return this: it points to 'x', the caller's variable passed as `mut x`

fn keep(mut x: Int): @Int = addrof x

fn main() do
    var a = 1
    val p = keep(mut a)
