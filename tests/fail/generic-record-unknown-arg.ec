// error: Strng
rec Box<T> as
    value: T

fn main(): Int do
    val b: Box<Strng> = Box { "a" }
    ret 0
