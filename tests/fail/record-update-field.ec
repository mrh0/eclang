// error: Point has no field 'w'. Did you mean 'x'?
rec Point as
    x: Int

fn main(): Int do
    val p = Point { 1 }
    val q = Point { ...p, w = 2 }
    ret q.x
