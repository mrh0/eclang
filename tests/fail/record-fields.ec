// error: Point has 2 field(s) (x: Int, y: Int), but 3 value(s) were given
rec Point as
    x: Int
    y: Int

fn main(): Int do
    val p = Point { 1, 2, 3 }
    ret 0
