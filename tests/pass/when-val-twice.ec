use "core:io.ec"

fn f(n: Int): Int = n + 1

fn main() do
    when val v = f(0) do
        1 do io.log("one")
        else do io.log("other")
    when val v = f(1) do
        1 do io.log("one")
        else do io.log("other ${v}")
    val a = when val v = f(2) do
        3 do "three"
        else do "other"
    val b = when val v = f(3) do
        w if w > 3 do "big ${w}"
        else do "small"
    io.log(a)
    io.log(b)
