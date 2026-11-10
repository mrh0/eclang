// error: This `"` ends the string, although it is inside `${...}`
use "core:io.ec"

fn f(s: String): String = s

fn main() do
    val c = true
    if c do
        io.log("x")
    else do
        io.log("a ${f("x")} b")
