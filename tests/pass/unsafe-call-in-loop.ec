use "core:io.ec"
use "core:structures.ec"

// A call inside `unsafe` isn't checked for what it keeps, also in the later rounds of a loop, which the compiler
// checks again with what earlier rounds left.
fn keepIn(l: List<String>, s: String) do
    l.push(s)

fn main(): Int do
    val names: List<String> = list()
    for i in 0..<3 do
        pool do
            // The Strings are only counted, never read after their pool is freed.
            unsafe keepIn(names, "${i}")
    io.log(names.len)
    ret 0
