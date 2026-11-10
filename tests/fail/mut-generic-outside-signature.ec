// error: `<mut T>` says what a function's callers may give for T, so it goes in the function's parameter or result types. Here, write `<T>`.
fn main() do
    val x: <mut T>? = null
