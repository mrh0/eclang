// `requires` is checked at each call (and narrows in the body), `ensures` where the function returns, with `result`.
use "core:io.ec"

fn at(a: Int[], i: Size): Int requires i < a.len ensures result != 0 = a[i]

fn length(s: String?): Size requires s != null, "length needs a String" = s.len

fn twice(n: Int): Int ensures result % 2 == 0 = n * 2

fn fill(mut n: Int, target: Int) requires target >= 0 ensures n == target do
    n = target

fn larger(x: <T>, y: <T>): <T> requires x != y ensures result == x or result == y do
    ret if x > y do x else y

fn pad(s: String, width: Size = 4): String requires width > 0 do
    var out = s
    while out.len < width do
        out = "${out}."
    ret out

fn half(n: Int): Int throws Error requires n >= 0 ensures result * 2 <= n do
    throw Error { 1, "odd" } if n % 2 != 0
    ret n / 2

fn early(n: Int): Int ensures result > 0 do
    if n > 10 do
        ret 10
    ret n + 1

fn note(n: Int) debug requires n < 100 do
    io.log("note ${n}")

fn main(): Int do
    val a = [4, 5, 6]
    io.log(at(a, 2))
    io.log(length("four"))
    io.log(twice(21))
    var n = 1
    fill(mut n, 7)
    io.log(n)
    io.log(larger(3, 9))
    io.log(larger(2.5, 1.5))
    io.log(pad("ab"))
    io.log(pad("ab", 3))
    io.log(half(8) ?? -1)
    io.log(half(7) ?? -1)
    io.log(early(50))
    io.log(early(2))
    note(5)
    // Through a function value, the function checks its `requires` itself.
    val f = fn twice
    io.log(f(4))
    ret 0
