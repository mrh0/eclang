// error: This `when` doesn't handle every value
fn f(v: Int | CString): Int do
    when v do
        is Int if v > 0 do ret 1
        is CString do ret 2
    ret 3

fn main(): Int do
    ret f(1)
