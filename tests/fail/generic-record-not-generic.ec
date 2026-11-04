// error: Point has no type parameters
rec Point as
    x: Int

fn main(): Int do
    val p: Point<Int> = Point { 1 }
    ret 0
