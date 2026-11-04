// error: the value may be null. Check it first with `if x != null`
fn name(s: CString?): CString = s

fn main(): Int do
    ret 0
