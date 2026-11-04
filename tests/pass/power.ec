use "core:io.ec"

// `**` on whole numbers stays a whole number, computed exactly; otherwise it is a Double.
fn main(): Int do
    io.log(2 ** 10)
    val x: Long = 3
    io.log(x ** 39)
    val n = 2
    io.log(n ** 30)
    io.log(n ** -1)
    val one = -1
    io.log(one ** 3)
    val u: UInt = 3
    io.log(u ** 4)
    io.log(2.0 ** 0.5)
    val d = 1.5
    io.log(d ** 2)
    ret 0
