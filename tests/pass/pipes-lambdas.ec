use "core:io.ec"

val triple = fn (x: Int) = x * 3

fn double(x: Int): Int = x * 2
fn add(a: Int, b: Int): Int = a + b
fn apply(f: (Int) => Int, x: Int): Int = f(x)
fn twice(f: (Int) => Int, x: Int): Int = f(f(x))

fn main(): Int do
    // `|>` passes the value on: as the first argument, or wherever `it` is.
    io.log(5 |> double)
    io.log(5 |> add(10))
    io.log(5 |> double |> add(1) |> it * it)
    io.log(double(4) |> add(it, it))
    io.log(3 |> apply(fn (x: Int) = x + 1, it))
    io.log(10 |> fn (x: Int) = x - 1)
    // Lambdas: parameter types can come from the expected function type.
    val inc: (Int) => Int = fn (x) = x + 1
    io.log(inc(1))
    io.log(apply(fn (x) = x * x, 7))
    io.log(twice(triple, 2))
    io.log(twice(fn (x) = twice(fn (y) = y + 1, x), 0))
    val greet = fn (name: CString) = io.log(name)
    greet("hi"c)
    val wide = fn (x: Int): Long = x
    io.log(wide(9))
    ret 0
