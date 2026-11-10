// error: `out` is for C functions (`declare fn`)

fn f(out x: Int) do
    x = 1

fn main() do
    var a = 0
    f(mut a)
