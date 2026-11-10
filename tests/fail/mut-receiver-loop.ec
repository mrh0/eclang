// error: `a.clear(...)` changes 'a' (the first parameter of 'clear' is `mut`) inside a loop, where a check before the loop narrowed its type

fn clear(mut x: Int?) do
    x = null

fn main() do
    var a: Int? = 1
    var t = 0
    if a != null do
        while t < 3 do
            t += a
            a.clear()
