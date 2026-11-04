// error: `for i in` needs an array, a String or a range, but got the number Int. To count, write `for i in 0..<n do`.
fn main(): Int do
    val n = 3
    for i in n do
        pass
    ret 0
