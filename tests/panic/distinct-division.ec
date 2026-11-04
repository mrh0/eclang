// panic: division by zero
type UserId = distinct Int

fn main(): Int do
    val a: UserId = 4
    val b: UserId = 0
    ret (a / b) as Int
