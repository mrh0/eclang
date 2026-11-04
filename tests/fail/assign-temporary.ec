// error: This is a temporary value, so changing its field `.x` would have no effect.
rec Point as
    x: Int
    y: Int

fn make(): Point = Point { x = 1, y = 2 }

fn main(): Int do
    make().x = 3
    ret 0
