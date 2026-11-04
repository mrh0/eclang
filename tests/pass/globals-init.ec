use "core:lib.ec"

// C only allows constant initializers for globals; the others are set at the start of the program, in order.
fn twice(x: Int): Int do
    ret x * 2

val a = twice(4)
val b = a + 1
var c: Int | CString = 7
val d = 1 << 3
val names = ["x"c, "y"c]

fn main(): Int do
    log(a)
    log(b)
    log(c as Int)
    log(d)
    log(names[1])
    ret 0
