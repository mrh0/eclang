use "ec:lib.ec"

val n = 10
val a = 0
val b = 1

fn main(): Int do
    for i in 0 to 100 do
        a = b
        b = a + b
        log(a)
    ret 0
