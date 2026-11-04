use "core:io.ec"

// C only allows constant initializers for globals; the others are set at the start of the program, in order.
fn twice(x: Int): Int do
    ret x * 2

val a = twice(4)
val b = a + 1
var c: Int | CString = 7
val d = 1 << 3
val names = ["x"c, "y"c]

fn main(): Int do
    io.log(a)
    io.log(b)
    io.log(c as Int)
    io.log(d)
    io.log(names[1])
    ret 0
