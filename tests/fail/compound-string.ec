// error: Joining Strings needs memory, so `+=` can't do it. Write `s = concat(s, other)`
fn main(): Int do
    var s = "a"
    s += "b"
    ret 0
