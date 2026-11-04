use "core:lib.ec"

// `**` on whole numbers stays a whole number, computed exactly; otherwise it is a Double.
fn main(): Int do
    log(2 ** 10)
    val x: Long = 3
    log(x ** 39)
    val n = 2
    log(n ** 30)
    log(n ** -1)
    val one = -1
    log(one ** 3)
    val u: UInt = 3
    log(u ** 4)
    log(2.0 ** 0.5)
    val d = 1.5
    log(d ** 2)
    ret 0
