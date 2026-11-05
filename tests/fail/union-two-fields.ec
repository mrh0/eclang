// error: so a literal gives exactly one of them (2 given)
union U as
    a: Int
    b: Float

fn main(): Int do
    val u = U { a = 1, b = 2.0 }
    ret 0
