// error: Can't tell which Box this is
rec Box<T> as
    value: T

fn main(): Int do
    val b = Box { 1 }
    ret 0
