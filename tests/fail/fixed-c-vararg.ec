// error: C can't take an array by value
declare fn printf as (f: CString, ...): Int

fn main(): Int do
    var a: Char[4]
    printf("x"c, a)
    ret 0
