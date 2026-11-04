// error: `==` can't compare (Int?, Int) tuples
fn main(): Int do
    val a: (Int?, Int) = (1, 2)
    val b = a == a
    ret 0
