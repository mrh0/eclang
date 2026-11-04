// panic: is out of bounds for length 3
fn main(): Int do
    val s = "abc"
    val n = 4
    val t = s[..<n]
    ret t.len as Int
