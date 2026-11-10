// error: This call returns nothing, so `try ... catch` has no value to give
use "core:errors.ec"
fn f() throws Error do
    ret

fn main(): Int do
    val x = try f() catch e do 0
    ret 0
