// error: Can't clear the current pool here: it belongs to the callers too
fn main(): Int do
    clearPool(pool)
    ret 0
