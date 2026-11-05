use "core:io.ec"

fn f(a: Int, b: String = "x", c: String? = null): String do
    val shown = c ?? "none"
    ret "${a} ${b} ${shown}"

fn main() do
    io.log(f(1))
    io.log(f(1, "y"))
    io.log(f(1, "y", "z"))
    io.log(f(1, c = "z"))
    io.log(f(1, b = "y"))
