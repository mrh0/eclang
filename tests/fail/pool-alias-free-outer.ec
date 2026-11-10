// error: Can't destroy 'outer': it holds the current pool
fn main(): Int do
    val outer = pool
    pool do
        destroy outer
    ret 0
