// error: Cannot store the result of a call that can fail
fn f(): (Int, Int) throws String = (1, 2)

fn main(): Int do
    val (a, b) = f()
    ret a
