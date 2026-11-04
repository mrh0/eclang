// error: `**` on 10 and 30: the result is too big for a Long
use "core:lib.ec"

fn main(): Int do
    log(10 ** 30)
    ret 0
