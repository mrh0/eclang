// error: Point isn't a `mut rec`: its fields are fixed
rec Point as
    x: Int
    y: Int

fn main() do
    var p = Point { 1, 2 }
    val pp = addrof p
    pp.x = 3
