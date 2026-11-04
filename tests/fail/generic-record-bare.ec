// error: Box is generic, so it needs its type arguments
rec Box<T> as
    value: T

fn show(b: Box): Int = 0

fn main(): Int = 0
