// error: Can't pass this as `mut p.x`: Point isn't a `mut rec`
rec Point as
    x: Int
    y: Int

fn bump(mut n: Int) do
    n += 1

fn main() do
    var p = Point { 1, 2 }
    bump(mut p.x)
