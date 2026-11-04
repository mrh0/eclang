// error: `+` and `0` are for numbers
fn main(): Int do
    val n = "a"
    val s = "${n:05}"
    ret s.len as Int
