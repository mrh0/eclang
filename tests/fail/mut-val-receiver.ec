// error: `p.move(...)`, whose first parameter is `mut`, lets the function change 'p', but 'p' is a `val`

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
    p.move(1)
