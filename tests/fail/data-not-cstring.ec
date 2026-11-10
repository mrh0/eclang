// error: a CString must end with a 0, and `.data` of a String or Char[N] need not (a slice doesn't). Use `cstr(s)`
use "core:io.ec"

fn main(): Int do
    val s = "hello"
    io.log(s[1..<3].data)
    ret 0
