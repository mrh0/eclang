// error: 'l' is a `val`, so its fields can't change either.
mut rec Point as
    x: Int
    y: Int

mut rec Line as
    a: Point
    b: Point

fn main(): Int do
    val p = Point { x = 1, y = 2 }
    val l = Line { a = p, b = p }
    l.a.x = 3
    ret 0
