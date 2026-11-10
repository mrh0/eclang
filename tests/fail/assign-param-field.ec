// error: 'p' is a parameter, so its fields can't change. Copy it into a variable first (`var copy = p`), or make it a `mut` parameter (`mut p: Point`)
mut rec Point as
    x: Int
    y: Int

fn move(p: Point) do
    p.x += 1

fn main(): Int do
    move(Point { x = 1, y = 2 })
    ret 0
