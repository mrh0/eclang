// error: 'y' is the pool of a `pool` block
fn main(): Int do
    pool x do
        val y = x
        destroy y
    ret 0
