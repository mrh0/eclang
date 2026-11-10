// error: nothing shows that 'n' was allocated in the current pool
fn main(): Int do
    val n = 5
    const assert n in pool
    ret 0
