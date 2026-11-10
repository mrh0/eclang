// error: Parameter 'p' of 'move' is `mut`: the function changes the caller's variable. Pass a variable with `mut`

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
    var p = Point { 1, 2 }
    move(p, 1)
