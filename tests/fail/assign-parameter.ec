// error: 'n' is a parameter, so it can't be assigned a new value. Copy it into a `var` to change it here, or make it a `mut` parameter

fn f(n: Int) do
    n = 2

fn main() do
    f(1)
