// error: Read its fields one by one instead
rec Point as
    x: Int
    y: Int

fn main(): Int do
    val (a, b) = Point { 1, 2 }
    ret a
