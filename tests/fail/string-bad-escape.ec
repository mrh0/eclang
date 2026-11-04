// error: Unknown escape `\q` in a string
use "core:lib.ec"

fn main(): Int do
    log("a\qb")
    ret 0
