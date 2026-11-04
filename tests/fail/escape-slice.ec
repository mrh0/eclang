// error: Can't store this in 'keep': it points to memory from the pool of the `pool` block at line 6
use "core:io.ec"

fn main(): Int do
    var keep = "x"
    pool do
        keep = ("abc ${1}")[0..<2]
    io.log(keep)
    ret 0
