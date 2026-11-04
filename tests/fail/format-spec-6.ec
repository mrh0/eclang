// error: `0` pads on the left
fn main(): Int do
    val n = 1
    val s = "${n:<05}"
    ret s.len as Int
