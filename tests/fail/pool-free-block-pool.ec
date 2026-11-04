// error: 'p' is the pool of a `pool` block, which is freed when the block ends; freeing it here would free it twice. Use `clearPool(p)`
fn main(): Int do
    pool p do
        freePool(p)
    ret 0
