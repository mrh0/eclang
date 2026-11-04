use "core:lib.ec"

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
    log(p.x)
    log(p.y)

    // Through a pointer the pointer itself may be a val.
    val ptr = addrof(p)
    moveRight(ptr, 10)
    ptr.y = 7
    log(p.x)
    log(p.y)
    reset(ptr)
    log(p.x)

    var line = Line { a = p, b = p }
    line.b.y = 9
    (line.a).x = 4
    log(line.a.x)
    log(line.b.y)

    // Array elements live behind a pointer, so they can change even when the array is a val.
    val a = [1, 2, 3]
    a[0] = 10
    a[1] += 5
    a[2] <<= 2
    log(a[0])
    log(a[1])
    log(a[2])

    var n = 10
    n -= 3
    n %= 4
    n |= 8
    n &= 10
    n ^= 1
    n >>= 1
    log(n)

    var d = 1.5
    d /= 2
    log(d)

    var small: Short = 3
    small *= 2
    log(small)

    // `+=` on a narrowed variable uses (and keeps) the narrowed type.
    var maybe: Int? = 4
    if maybe != null do
        maybe += 1
        log(maybe)

    var cell = 1
    val at = addrof(cell)
    @at = 42
    @at += 1
    log(cell)
    ret 0
