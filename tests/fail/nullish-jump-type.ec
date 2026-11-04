// error: Function f returns nothing, but this returns a 1
fn find(): Int? = null

fn f() do
    val a = find() ?? ret 1

fn main(): Int do
    f()
    ret 0
