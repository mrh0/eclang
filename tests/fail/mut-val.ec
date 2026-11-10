// error: `mut p` lets the function change 'p', but 'p' is a `val`, which can't change. Declare it with `var`

rec Point as
    x: Int
    y: Int

fn move(mut p: Point, dx: Int) do
    p.x += dx

fn swap(mut a: Int, mut b: Int) do
    val t = a
    a = b
    b = t

fn main() do
    val p = Point { 1, 2 }
    move(mut p, 1)
