// error: This lambda is passed to C as a plain function pointer, which can't carry variables, so it can't use 'size'
use "core:c/stdlib.ec"
fn main(): Int do
    val size = 4
    val numbers = new Int[3]
    qsort(numbers.data, 3, 4, fn (a, b) = if size > 0 do 1 else 0)
    ret 0
