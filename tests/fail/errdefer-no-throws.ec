// error: `errdefer` runs when an error leaves the function, but main doesn't declare `throws`
use "core:c/stdio.ec"

fn main(): Int do
    errdefer printf("x\n"c)
    ret 0
