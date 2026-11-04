// error: `str` must return a String
rec Point as
    x: Int

fn str(p: Point): Int = p.x

fn main(): Int do
    val p = Point { 1 }
    val s = "${p}"
    ret 0
