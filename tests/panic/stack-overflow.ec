// panic: out of stack: the calls went too deep
use "core:io.ec"

fn deep(n: Int): Int do
    ret 0 if n < 0
    ret deep(n + 1) + 1

fn main() do
    io.log("started")
    io.log("${deep(0)}")
