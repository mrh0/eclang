use "core:io.ec"

rec Handler as
    run: ((Int) => Int)?
    name: String

fn twice(x: Int): Int = x * 2

fn call(h: Handler, x: Int): Int do
    val f = h.run ?? ret 0
    ret f(x)

fn main() do
    io.log(call(Handler { run = null, name = "none" }, 2))
    io.log(call(Handler { run = fn twice, name = "twice" }, 2))
