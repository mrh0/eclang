// error: A lambda can't use 'base', a local variable of the function around it
fn main(): Int do
    val base = 10
    val f = fn (x: Int) = x + base
    ret f(1)
