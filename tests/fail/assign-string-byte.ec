// error: A String's bytes can't be changed in place
fn main(): Int do
    val s = "abc"
    s[0] = 'x'
    ret 0
