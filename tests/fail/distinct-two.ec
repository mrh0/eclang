// error: Can't combine A and B
type A = distinct Int
type B = distinct Int

fn main(): Int do
    val a: A = 1
    val b: B = 1
    val same = a == b
    ret 0
