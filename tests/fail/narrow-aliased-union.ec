// error: `+` needs two numbers, but got Int | CString and 1.
use "core:io.ec"

fn main(): Int do
    var v: Int | CString = 5
    val pv = addrof v
    if v is Int do
        @pv = "hi"c
        io.log(v + 1)
    ret 0
