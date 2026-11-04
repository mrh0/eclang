// error: It may be null
fn find(): (Int, Int)? = null

fn main(): Int do
    val (a, b) = find()
    ret a
