// error: here calls this same `str` with the same value
rec Point as
    x: Int

fn str(p: Point): String = "P ${p}"

fn main(): Int do
    val p = Point { 1 }
    val s = "${p}"
    ret 0
