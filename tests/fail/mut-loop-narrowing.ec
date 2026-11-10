// error: `+=` needs numbers, but 't' is a Int and the value is a Int?.

fn clear(mut x: Int?) do
    x = null

fn main() do
    var a: Int? = 1
    var t = 0
    if a != null do
        while t < 3 do
            t += a
            clear(mut a)
