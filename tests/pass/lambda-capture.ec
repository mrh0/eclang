use "core:io.ec"
use "core:array.ec"

// A lambda can use the variables around it: a copy of each `val` and parameter, and the `var` itself.

fn adder(n: Int): (Int) => Int = fn (x) = x + n

fn apply(f: (Int) => Int, x: Int): Int = f(x)

fn bump(mut c: Int) do
    c += 1

fn twice(f: () => None) do
    f()
    f()

fn main() do
    val limit = 3
    val nums = [1, 2, 3, 4, 5]
    val big = nums.filter(fn (x) = x > limit)
    io.log("filtered: ${big.len}")

    // A closure returned from the function that made it keeps its own copy of `n`.
    val add5 = adder(5)
    val add2 = adder(2)
    io.log("adders: ${add5(10)} ${apply(add2, 1)} ${apply(fn (x) = x * limit, 7)}")

    // A `var` is shared: a change through the lambda is seen outside.
    var count = 0
    twice(fn () = bump(mut count))
    io.log("count: ${count}")
    count = 10
    val read = fn () = count
    count = 20
    io.log("read: ${read()}")

    // A `val` checked before the lambda stays narrowed in it.
    val name: String? = "abc"
    if name != null do
        val measure = fn (s: String) = name.len + s.len
        val n = measure("xy")
        io.log("narrowed: ${n}")

    // A lambda inside a lambda captures through it.
    val curried = fn (a: Int) = fn (b: Int) = a + b + limit
    val plus1 = curried(1)
    io.log("nested: ${plus1(2)}")

    // `fn name` is a function value too.
    val make = fn adder
    val add7 = make(7)
    io.log("reference: ${add7(1)}")

    // A function value can be stored like any other.
    val table = [fn (x: Int) = x + limit, fn (x: Int) = x * limit]
    val first = table[0]
    val second = table[1]
    io.log("table: ${first(2)} ${second(2)}")
