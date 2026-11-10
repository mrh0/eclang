// error: `checks` isn't anything eclang knows after a function's signature
fn f(x: Int): Int checks x > 0 do
    ret x
fn main() do
    f(1)
