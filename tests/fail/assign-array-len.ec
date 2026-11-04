// error: `.len` can't be assigned: it describes the memory this Int[] points to.
fn main(): Int do
    var a = [1, 2]
    a.len = 5
    ret 0
