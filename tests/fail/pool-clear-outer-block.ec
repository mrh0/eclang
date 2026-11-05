// error: Can't clear 'a' here: a `pool` block inside it is still running
fn main(): Int do
    pool a do
        pool do
            clearPool(a)
    ret 0
