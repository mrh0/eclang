// error: 'both' is given 'p' twice to change
mut rec Point as
    x: Int
    y: Int

fn both(mut p: Point, mut x: Int) do
    p.x = x

fn main() do
    var p = Point { 1, 2 }
    both(mut p, mut p.x)
