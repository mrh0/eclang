// error: A function type doesn't name its parameters
fn add(a: Int): Int = a

fn main(): Int do
    val f: (Int) => Int = fn add
    ret f(a = 1)
