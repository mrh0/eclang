// error: `mut` argument 1 of 'swap' is a variable of type Int?, but parameter 'a' is a `mut Int`. A `mut` parameter takes a variable of exactly its type

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
    var a: Int? = 1
    var b = 2
    swap(mut a, mut b)
