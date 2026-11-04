// error: Pair takes 2 type arguments (A, B), but 1 were given
rec Pair<A, B> as
    a: A
    b: B

fn main(): Int do
    val p = Pair<Int> { 1, 2 }
    ret 0
