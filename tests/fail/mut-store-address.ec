// error: Can't store this in 'g': it points to 'x', the caller's variable passed as `mut x`

var g: @Int? = null

fn keep(mut x: Int) do
    g = addrof x

fn main() do
    var a = 1
    keep(mut a)
