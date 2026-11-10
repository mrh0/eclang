// Words that mean something in one position only, and are ordinary names everywhere else: `it`, `here`, `where`,
// `extern`, `requires`, `ensures`, `module` and `struct`. `Volatile<T>` makes a `var` a C `volatile` variable.
use "core:io.ec"
use "core:errors.ec"

declare fn cAbs extern "abs" as (x: Int): Int

var counter: Volatile<Int> = 0

// `x - 1` after `requires`: `requires` isn't read as a `debug` before a word `x`.
fn dec(x: Int): Int requires x - 1 >= 0 ensures result >= 0 do
    ret x - 1

// `debug requires x - 1 ...` first parses as two contracts; the Visitor reads it again as one.
fn checked(x: Int): Int debug requires x - 1 >= 0, "x too small" do
    ret x

fn twice(x: Int): Int debug requires x < 100 debug ensures result < 200 do
    ret x * 2

// A parameter can be called `it`: outside a `|>` stage, it is a name like any other.
fn next(it: Int): Int = it + 1

fn main() do
    var v: Volatile<Int> = 3
    var w: Volatile<Double>
    v += 1
    w = 2.5
    counter = v
    val it = 5
    val where = 2
    val extern = 1
    val requires = 3
    val ensures = 4
    val module = 6
    val struct = 7
    io.log("${it} ${where} ${extern} ${requires} ${ensures} ${module} ${struct} ${v} ${w} ${counter}")
    // Inside a `|>` stage, `it` is the value before the `|>`, even with a variable `it` around.
    io.log("${10 |> it * 2} ${next(1)} ${dec(5)} ${checked(3)} ${twice(4)} ${cAbs(-3)}")
    for x in [1, 2, 3, 4] where x % 2 == 0 do
        io.log("${x}")
    val at = here
    io.log("${at.line} ${at.function}")
