// error: 'swap' is given 'a' twice to change (`mut a` and `mut a`).

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
    var a = 1
    swap(mut a, mut a)
