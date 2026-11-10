// error: a `var` that the lambda uses (a lambda shares the `var`s it uses, it doesn't copy them)
fn counter(): () => Int do
    var count = 0
    ret fn () = count
fn main(): Int do
    val c = counter()
    ret c()
