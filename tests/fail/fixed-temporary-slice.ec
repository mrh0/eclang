// error: is a temporary value, so it can't be sliced
fn f(): Int[2] = [1, 2]

fn main(): Int do
    val s = f()[..]
    ret 0
