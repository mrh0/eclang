use "core:io.ec"

// Function values: parameters, variables and record fields of function type.

fn double(x: Int): Int = x * 2
fn square(x: Int): Int = x * x
fn half(x: Int): Int throws Error do
    throw Error { code = 1, message = "odd" } if x % 2 != 0
    ret x / 2

fn apply(f: (Int) => Int, value: Int): Int = f(value)

fn twice(f: (Int) => Int, value: Int): Int = f(f(value))

rec Button as
    label: CString
    onClick: (Int) => Int

fn pick(big: Bool): (Int) => Int do
    ret fn square if big
    ret fn double

fn main(): Int do
    io.log(apply(fn double, 5))
    io.log(twice(fn square, 3))

    val f: (Int) => Int = fn double
    io.log(f(21))

    var g = pick(true)
    io.log(g(4))
    g = pick(false)
    io.log(g(4))

    val b = Button { label = "ok"c, onClick = fn square }
    io.log(b.onClick(9))
    val p = addrof(b)
    io.log(p.onClick(2))

    val h: (Int) => Int throws Error = fn half
    io.log(h(10) ?? -1)
    io.log(h(7) ?? -1)

    val maybe: ((Int) => Int)? = fn double
    if maybe != null do
        io.log(maybe(50))
    ret 0
