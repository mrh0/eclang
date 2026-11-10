// `[]` on a pointer or a C array has no length to check, so it needs `unsafe`; `p[0]` is `@p` and doesn't.
use "core:io.ec"

fn second(p: @Int): Int = unsafe p[1]

fn main() do
    var a = [1, 2, 3]
    val p = a.data !as @Int
    io.log(p[0])
    io.log(second(p))
    unsafe a.data[2] = 30
    unsafe p[1] = 20
    val c = [4, 5, 6]c
    var total = 0
    unsafe do
        for i in 0..<3 do
            total += c[i]
    io.log("${a[0]} ${a[1]} ${a[2]} ${total}")
