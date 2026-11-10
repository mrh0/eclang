// error: The condition of `requires` must be a Bool, but it is Int.
fn half(n: Int): Int requires n = n / 2
fn main(): Int do
    ret half(2)
