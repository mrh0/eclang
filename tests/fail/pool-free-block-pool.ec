// error: 'p' is the pool of a `pool` block, which is freed when the block ends; destroying it here would free it twice. Use `clearPool(p)`
fn main(): Int do
    pool p do
        destroy p
    ret 0
