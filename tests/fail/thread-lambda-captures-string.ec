// error: but the lambda captures 'name', and 'name' is a String, which points to its text
use "core:io.ec"
use "core:thread.ec"

// What a thread's lambda captures must be `flat`: name's text is in this function's pool.
fn main(): Int throws Error do
    val name = "worker ${1}"
    val t = try thread.spawn(fn () = io.log(name))
    try thread.join(t)
    ret 0
