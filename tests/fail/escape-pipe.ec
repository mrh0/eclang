// error: Can't store this in 'keep': it points to memory from the pool of the `pool` block at line 6
use "core:io.ec"

fn main(): Int do
    var keep = "x"
    pool do
        keep = "n ${1}" |> it
    io.log(keep)
    ret 0
