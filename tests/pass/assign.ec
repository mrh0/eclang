use "core:io.ec"

// Assigning fields, elements and pointers, and the compound operators.
rec Point as
    x: Int
    y: Int

rec Line as
    a: Point
    b: Point

fn moveRight(p: @Point, by: Int) do
    p.x += by

fn reset(p: @Point) do
    @p = Point { x = 0, y = 0 }

fn main(): Int do
    var p = Point { x = 1, y = 2 }
    p.x = 5
    p.y *= 3
    io.log(p.x)
    io.log(p.y)

    // Through a pointer the pointer itself may be a val.
    val ptr = addrof(p)
    moveRight(ptr, 10)
    ptr.y = 7
    io.log(p.x)
    io.log(p.y)
    reset(ptr)
    io.log(p.x)

    var line = Line { a = p, b = p }
    line.b.y = 9
    (line.a).x = 4
    io.log(line.a.x)
    io.log(line.b.y)

    // Array elements live behind a pointer, so they can change even when the array is a val.
    val a = [1, 2, 3]
    a[0] = 10
    a[1] += 5
    a[2] <<= 2
    io.log(a[0])
    io.log(a[1])
    io.log(a[2])

    var n = 10
    n -= 3
    n %= 4
    n |= 8
    n &= 10
    n ^= 1
    n >>= 1
    io.log(n)

    var d = 1.5
    d /= 2
    io.log(d)

    var small: Short = 3
    small *= 2
    io.log(small)

    // `+=` on a narrowed variable uses (and keeps) the narrowed type.
    var maybe: Int? = 4
    if maybe != null do
        maybe += 1
        io.log(maybe)

    var cell = 1
    val at = addrof(cell)
    @at = 42
    @at += 1
    io.log(cell)
    ret 0
