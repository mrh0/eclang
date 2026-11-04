// error: `it` is used 2 times after this `|>`, so the value before it must be stored first
fn f(x: Int): Int = x
fn main(): Int do
    var i = 0
    while (f(i) |> it * it) < 10 do
        i += 1
    ret 0
