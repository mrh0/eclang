// error: This `when` doesn't handle every value a Null | Int | CString can be: CString is missing. Add `is CString do`
use "core:io.ec"

fn show(v: Int | CString | Null) do
    when v do
        is Int do io.log(v)
        null do io.log("null"c)

fn main(): Int do
    show(1)
    ret 0
