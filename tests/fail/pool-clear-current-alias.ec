// error: Can't clear 'outer', the current pool of this function
fn main(): Int do
    val outer = pool
    pool do
        clearPool(outer)
    ret 0
