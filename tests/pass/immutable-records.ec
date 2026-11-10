use "core:io.ec"
use "core:c/stdlib.ec"

// A record's fields are fixed once it is made, unless it is a `mut rec`.
rec Point as
    x: Int
    y: Int

ref rec Counter as
    name: String
    count: Int = 0

mut rec Tally as
    count: Int

mut ref rec Score as
    points: Int

// An immutable record may extend a `mut rec`.
mut rec Animal as
    legs: Int

rec Dog extends Animal as
    name: String

// `<mut T>`: the function changes fields of T, so T must be a `mut rec`.
fn reset(mut v: <mut T>) do
    v.count = 0

rec Box<mut T> as
    item: T

// A `mut` parameter of an immutable record can be replaced, not changed field by field.
fn moved(mut p: Point, dx: Int) do
    p = { ...p, x = p.x + dx }

fn main() do
    var p = Point { 1, 2 }
    p = { ...p, x = 5 }
    moved(mut p, 2)
    p.moved(1)
    io.println("${p.x} ${p.y}")

    var c = new Counter { "clicks" }
    val before = c
    c = new Counter { ...@c, count = c.count + 1 }
    io.println("${c.name} ${c.count} ${before.count}")

    var t = Tally { 3 }
    t.count += 2
    io.println("${t.count}")
    reset(mut t)
    io.println("${t.count}")

    val s = new Score { 1 }
    s.points *= 10
    io.println("${s.points}")

    var b = Box<Tally> { Tally { 7 } }
    b = { ...b, item = Tally { 8 } }
    io.println("${b.item.count}")

    // C structs follow the same rule: `declare rec` is fixed, `declare mut rec` can change (see c/termios.ec).
    var d = div(17, 5)
    d = { ...d, rem = 0 }
    io.println("${d.quot} ${d.rem}")

    val dog = Dog { 4, "rex" }
    io.println("${dog.name} ${dog.legs}")
