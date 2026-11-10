// error: Point isn't a `mut rec`: its fields are fixed once it is made. Make a new one with the change instead, `{ ...old, x = value }`, or declare `mut rec Point`
rec Point as
    x: Int
    y: Int

fn main() do
    var p = Point { 1, 2 }
    p.x = 3
