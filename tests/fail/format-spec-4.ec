// error: `:abc` isn't a format
fn main(): Int do
    val n = 1
    val s = "${n:abc}"
    ret s.len as Int
