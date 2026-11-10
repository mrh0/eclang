// error: `cstr(a)` is a copy of its text (up to its first 0) that does
declare fn puts as (s: CString): Int

fn main(): Int do
    var a: Char[2]
    puts(a)
    ret 0
