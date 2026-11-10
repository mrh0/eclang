use "core:io.ec"
use "core:c/stdlib.ec"

// Where C expects a function pointer, `fn name` and a lambda that uses no local variables are plain C functions.

fn descending(a: @Any, b: @Any): Int = @(b !as @Int) - @(a !as @Int)

fn show(values: Int[]) do
    io.log("${values[0]} ${values[1]} ${values[2]} ${values[3]}")

fn main() do
    val numbers = clone([3, 1, 4, 2])
    qsort(numbers.data, numbers.len, sizeof Int, fn (a, b) = @(a !as @Int) - @(b !as @Int))
    show(numbers)
    qsort(numbers.data, numbers.len, sizeof Int, fn descending)
    show(numbers)
