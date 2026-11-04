// error: `+` needs two numbers, but got Int | CString
use "core:lib.ec"

// The assignment in the inner block undoes `v is Int` for the rest of the outer block too.
fn main(): Int do
    var v: Int | CString = 1
    if v is Int do
        if true do
            v = "s"c
        log(v + 1)
    ret 0
