// error: Argument 2 of 'printf' is a String, which C's `...` can't take
use "core:c/stdio.ec"

fn main(): Int do
    printf("%s\n"c, "hi")
    ret 0
