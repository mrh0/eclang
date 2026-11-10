// error: Can't destroy 'q': it holds the current pool
fn main(): Int do
    val q = pool
    destroy q
    ret 0
