// error: Can't take the address of what a call returns
fn f(): Int = 1
fn main() do
    val p = addrof f()
