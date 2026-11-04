// error: Unknown escape `\q` in a string
use "core:io.ec"

fn main(): Int do
    io.log("a\qb")
    ret 0
