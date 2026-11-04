// error: 'p' is a `val`, so its fields can't change either. Declare it with `var` to change them.
rec Point as
    x: Int
    y: Int

fn main(): Int do
    val p = Point { x = 1, y = 2 }
    p.x = 3
    ret p.x
