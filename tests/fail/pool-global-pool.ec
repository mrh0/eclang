// error: Can't use 's' here: it points to memory of pool 'gp'
use "core:io.ec"
var gp = new Pool
fn main(): Int do
    val s = format(gp, "x${1}")
    unsafe clearPool(gp)
    io.log(s)
    ret 0
