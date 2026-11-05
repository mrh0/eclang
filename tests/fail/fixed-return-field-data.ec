// error: it points to 'r', a local variable
rec R as
    a: Char[2]

fn f(): CString do
    var r = R { ['a', 0 as Char] }
    ret r.a.data

fn main(): Int do
    ret 0
