// panic: requirement of note failed: n < 100
use "core:io.ec"

fn note(n: Int) debug requires n < 100 do
    io.log(n)

fn main(): Int do
    note(500)
    ret 0
