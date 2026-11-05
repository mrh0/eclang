// error: is a temporary value, so it can't be used through `.data`
fn f(): Int[2] = [1, 2]

fn main(): Int do
    val s = f().data
    ret 0
