// error: `mut x` passes a variable for the function to change, but this is a value, which has no place to change.

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
    move(mut Point { 1, 2 }, 1)
