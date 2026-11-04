// error: The error from parse(...) is ignored: it can fail with Error
use "core:errors.ec"

fn parse(c: Char): Int throws Error = 1

fn main(): Int do
    parse('1')
    ret 0
