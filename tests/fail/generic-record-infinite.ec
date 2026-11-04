// error: keeps making bigger instances of itself
rec Grow<T> as
    next: @Grow<@T>?

fn main(): Int do
    val g = Grow<Int> { null }
    ret 0
