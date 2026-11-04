// error: `...` must be a Point to make a Point from it, but this is a Other
rec Point as
    x: Int

rec Other as
    x: Int

fn main(): Int do
    val o = Other { 1 }
    val p = Point { ...o, x = 2 }
    ret p.x
