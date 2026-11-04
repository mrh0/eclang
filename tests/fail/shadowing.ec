// error: 'a' is already defined in an enclosing block, as a `val`. eclang doesn't let a nested block hide (shadow) a name
fn main(): Int do
    val a = 1
    if a == 1 do
        val a = 2
    ret a
