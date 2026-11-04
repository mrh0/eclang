// error: it points to memory from the pool of the `pool` block at line 6, which is freed when the function returns, so it would point to freed memory. Code in a `pool` block allocates in its pool
use "core:io.ec"

fn name(n: Int): String do
    io.log(n)
    pool do
        ret "n is ${n}"
    ret "none"

fn main(): Int = 0
