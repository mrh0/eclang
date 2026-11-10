// panic: fixed-char-too-long.ec:6: a String of 5 bytes doesn't fit a Char[4]
use "core:io.ec"

fn main(): Int do
    val s = "hello"
    val c: Char[4] = s
    io.log(c == "hell")
    ret 0
