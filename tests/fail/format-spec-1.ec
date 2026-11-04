// error: `:.2f` can't format a Int: a whole number takes
fn main(): Int do
    val n = 1
    val s = "${n:.2f}"
    ret s.len as Int
