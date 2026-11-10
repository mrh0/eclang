// error: `mut` parameter 'x' can't have a default value

fn f(mut x: Int = 1) do
    x += 1

fn main() do
    var a = 0
    f(mut a)
