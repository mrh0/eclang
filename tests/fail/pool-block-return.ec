// error: it points to memory from the pool of the `pool` block at line 6, which is freed when that block ends. Keep a copy instead: wrap the value in `keep(...)`
use "core:io.ec"

fn name(n: Int): String do
    io.log(n)
    pool do
        ret "n is ${n}"
    ret "none"

fn main(): Int = 0
