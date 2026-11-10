// error: the `debug` check of 'x' at line 5 doesn't count here, because a release build leaves it out
use "core:io.ec"
fn main(): Int do
    val x: Int? = if io.args().len > 5 do 3 else null
    debug assert x != null
    io.log(x + 1)
    ret 0
