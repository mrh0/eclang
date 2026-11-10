// error: Point isn't a `mut rec`: its fields are fixed
rec Point as
    x: Int
    y: Int

fn main() do
    val points = [Point { 1, 2 }, Point { 3, 4 }]
    points[0].x = 5
