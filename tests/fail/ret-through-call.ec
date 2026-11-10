// error: Can't return this: it comes from `id(...)`, and the result of `id` points into what its parameter 's' was given here, memory of pool 'p'
// A deferred free under `ret` of a call's result: the compiler finds that id returns into s.
use "core:io.ec"

fn id(s: String): String = s

fn make(): String do
    val p = new Pool
    defer destroy p
    val s = format(p, "x${1}")
    ret id(s)

fn main(): Int do
    io.println(make())
    ret 0
