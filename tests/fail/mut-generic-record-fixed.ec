// error: Box<...>'s type parameter `mut T` must be a type whose fields can change, but Point isn't a `mut rec`: its fields are fixed once it is made. Declare `mut rec Point`
rec Point as
    x: Int

rec Box<mut T> as
    item: T

fn main() do
    val b: Box<Point> = Box<Point> { Point { 1 } }
