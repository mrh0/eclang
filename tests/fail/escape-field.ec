// error: Can't store this in 'keep': it points to memory from the pool of the `pool` block at line 9
use "core:io.ec"

rec R as
    s: String

fn main(): Int do
    var keep = "x"
    pool do
        val r = R { s = "n ${1}" }
        keep = r.s
    io.log(keep)
    ret 0
