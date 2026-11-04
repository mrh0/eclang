// error: `new` needs a pool (a `Pool`) to allocate in, but this is a Int. Leave it out to use the current pool
fn main(): Int do
    val p = new 5 in 3
    ret 0
