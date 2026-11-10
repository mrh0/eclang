// error: The block after `catch e do` has no value, so it must leave
use "core:errors.ec"
fn f(): Int throws Error = 1

fn main(): Int do
    val x = try f() catch e do
        val y = e.code
    ret x
