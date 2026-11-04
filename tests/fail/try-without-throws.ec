// error: `try` passes the error on, but main doesn't declare that it can fail
use "core:lib.ec"

fn parse(c: Char): Int throws Error = 1

fn main(): Int do
    val n = try parse('1')
    ret n
