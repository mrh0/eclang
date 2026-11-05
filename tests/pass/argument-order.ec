use "core:io.ec"

// Arguments and the parts of a `${...}` run left to right, whatever C would do with them.

var n: Int = 0

fn bump(): Int do
    n += 1
    ret n

fn add3(a: Int, b: Int, c: Int): String = "${a} ${b} ${c}"

fn named(first: Int, second: Int = 0, third: Int = 0): String = "${first} ${second} ${third}"

fn fails(v: Int): Int throws String do
    n += 10
    ret v

fn word(s: String): String do
    n += 1
    ret "${s}${n}"

fn all(...values: Int): String do
    var text = ""
    for v in values do
        text = "${text}${v} "
    ret text

fn raise(p: @Int): Int do
    @p += 100
    ret @p

rec Box as
    value: Int
    make: (Int, Int) => Int

fn sum(a: Int, b: Int): Int = a * 10 + b

fn box(): Box do
    n += 1
    ret Box { n, fn sum }

fn change(b: @Box): Int do
    b.value += 1
    ret b.value

fn method(b: Box, v: Int): String = "${b.value} ${v}"

fn withTry(): String throws String do
    val text = add3(bump(), try fails(1), bump())
    ret text

fn tryInOperator(): Int throws String do
    val value = bump() * 100 + sum(bump(), try fails(3))
    ret value

fn check(a: Int, b: Int): Bool = a < b

fn main() do
    io.log(add3(bump(), bump(), bump()))

    // A read of a global sees the calls before it, and not the ones after it.
    io.log("${bump()} ${n}")
    io.log("${n} ${bump()}")
    io.log(add3(n, bump(), n))

    // Calls in calls.
    io.log(add3(bump(), sum(bump(), bump()), bump()))

    // Named arguments run as they are written.
    n = 0
    io.log(named(third = bump(), first = bump(), second = bump()))

    // `try` moves ahead of the statement, and takes the arguments before it along.
    n = 0
    io.log(withTry() ?? "failed")

    n = 0
    val operator = tryInOperator() ?? 0
    io.log("${operator} ${n}")

    // A local can't change, except through a pointer.
    n = 0
    var local = 1
    io.log(add3(local, bump(), local))
    var reached = 1
    val p = addrof reached
    io.log(add3(reached, raise(p), reached))

    // Fields, variadics, `concat`, methods and function values.
    var b = Box { 1, fn sum }
    io.log(add3(b.value, change(addrof b), b.value))
    n = 0
    io.log(all(bump(), bump(), bump()))
    n = 0
    io.log(concat(word("a"), word("b"), word("c")))
    n = 0
    io.log(box().method(bump()))
    n = 0
    val f = fn sum
    io.log("${f(bump(), bump())}")
    n = 0
    io.log("${box().make(bump(), bump())}")

    // Code that may not run, or runs more than once, can't have statements before it.
    n = 0
    var rounds = 0
    while check(bump(), bump()) and rounds < 2 do
        rounds += 1
    io.log("${rounds} ${n}")
    val shortcut = n > 100 and check(bump(), bump())
    io.log("${shortcut} ${n}")
    n = 0
    val late = true and check(bump(), bump())
    io.log("${late} ${n}")

    // After a call in the same statement, a comma expression keeps the order.
    n = 0
    val total = bump() * 1000 + sum(bump(), bump())
    io.log("${total}")

    // Operators, whose sides gcc would interleave.
    n = 0
    io.log("${bump() - bump()} ${(bump() << 2) + bump()}")
    n = 0
    io.log("${bump() < bump()} ${bump() == bump()} ${n * 10 + bump()}")

    // In a lambda, and in `defer`.
    n = 0
    val l = fn (x: Int) = sum(bump(), bump()) + x
    io.log("${l(100)}")
    deferred()

fn deferred() do
    n = 0
    defer io.log(add3(bump(), bump(), n))
