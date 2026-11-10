// error: `escaping` can't go with `mut`
fn keep(escaping mut n: Int) do
    ret

fn main() do
    var x = 1
    keep(mut x)
