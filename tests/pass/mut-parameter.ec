// `mut` parameters: the function changes the caller's variable, which the call passes as `mut x`.
use "core:io.ec"

mut rec Point as
    x: Int
    y: Int

fn move(mut p: Point, dx: Int) do
    p.x += dx
    p.y += 1

fn reset(mut p: Point) do
    p = Point { 0, 0 }

fn swap(mut a: Int, mut b: Int) do
    val t = a
    a = b
    b = t

fn swapAny(mut a: <T>, mut b: <T>) do
    val t = a
    a = b
    b = t

fn bump(mut n: Int): Int do
    n += 1
    ret n

// A `mut` parameter is passed on with `mut`, or as the receiver.
fn twice(mut p: Point) do
    move(mut p, 10)
    p.move(5)

fn show(mut n: Int): String = "n is ${n}"

fn add(mut total: Int, step: Int = total) do
    total += step

fn put(mut x: Int, v: Int | CString) do
    if v is Int do
        x = v
    else
        x = -1

// A check narrows a `mut` parameter like a variable, until it is assigned.
fn take(mut x: Int?): Int do
    if x != null do
        x += 1
        val r = x
        x = null
        ret r
    x = 10
    ret 0

fn describe(mut s: String | Int) do
    if s is Int do
        io.log("int ${s + 1}")
    else
        io.log("str ${s}")

mut rec Countdown as
    left: Int

fn next(mut c: Countdown): Int? do
    ret null if c.left == 0
    c.left -= 1
    ret c.left + 1

fn main() do
    var p = Point { 1, 2 }
    move(mut p, 3)
    io.log("${p}")
    p.move(1)
    io.log("${p}")
    twice(mut p)
    io.log("${p}")
    reset(mut p)
    io.log("${p}")

    var a = 1
    var b = 2
    swap(mut a, mut b)
    io.log("${a} ${b}")
    // Left to right: the call changes a before the second `${...}` reads it.
    io.log("${bump(mut a)} ${a}")
    var s1 = "x"
    var s2 = "y"
    swapAny(mut s1, mut s2)
    io.log("${s1}${s2}")

    // Elements and fields are places too.
    var pts = [Point { 1, 1 }, Point { 2, 2 }]
    move(mut pts[1], 100)
    pts[0].move(7)
    swap(mut pts[0].x, mut pts[1].x)
    io.log("${pts[0]} ${pts[1]}")
    // A pointer goes to a `mut` parameter as the receiver.
    val pp = addrof p
    pp.move(9)
    io.log("${p}")

    var n = 5
    io.log(show(mut n))
    add(mut n)
    add(mut n, 1)
    io.log("${n}")
    put(mut n, 7)
    io.log("${n}")
    put(mut n, "s"c)
    io.log("${n}")

    var m: Int? = 1
    io.log("${take(mut m)} ${m == null}")
    io.log("${take(mut m)} ${m ?? -1}")
    var v: String | Int = 4
    describe(mut v)
    v = "hi"
    describe(mut v)

    // `for` copies the iterator; `addrof` makes it advance the variable.
    var c = Countdown { 3 }
    for k in c do
        io.log("${k}")
    for k in addrof c do
        io.log("${k}")
    io.log("${c.left}")
