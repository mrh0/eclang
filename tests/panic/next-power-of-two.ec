// panic: the result doesn't fit in a Long
use "core:io.ec"
use "core:math.ec"

fn main(): Int do
    io.log(math.nextPowerOfTwo(math.LONG_MAX))
    ret 0
