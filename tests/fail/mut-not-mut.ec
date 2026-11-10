// error: Argument 1 of 'twice' is passed with `mut`, but parameter 'x' isn't `mut`, so the function can't change it.

fn twice(x: Int): Int = x * 2

fn main() do
    var a = 1
    val b = twice(mut a)
