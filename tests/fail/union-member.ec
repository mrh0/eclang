// error: the value may be CString. Narrow it first, for example with `if x is Int`
fn main(): Int do
    val u: Int | CString = 5
    val i: Int = u
    ret 0
