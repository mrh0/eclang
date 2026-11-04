// error: contains itself
rec Loop<T> as
    next: Loop<T>

fn main(): Int do
    var g: @Loop<Int>? = null
    ret 0
