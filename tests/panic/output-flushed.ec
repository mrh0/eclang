// panic: printed before
use "core:io.ec"

// What the program printed before it stopped is still written (stdout is flushed before a panic).
fn main(): Int do
    io.log("printed before"c)
    val a = [1, 2, 3]
    var i: Size = 5
    io.log(a[i])
    ret 0
