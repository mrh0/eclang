// error: a fill character and centering
fn main(): Int do
    val n = 1
    val s = "${n:^8}"
    ret s.len as Int
