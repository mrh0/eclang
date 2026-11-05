// error: `a.data` is a pointer to them
declare fn puts as (s: CString): Int

fn main(): Int do
    var a: Char[2]
    puts(a)
    ret 0
