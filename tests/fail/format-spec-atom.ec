// error: an atom is written with its `:`
fn main(): Int do
    val a = :ok
    val s = "${a:>5}"
    ret s.len as Int
