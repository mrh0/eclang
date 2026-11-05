// error: which field it holds isn't known
union U as
    a: Int
    b: Float

fn main(): Int do
    val u = U { a = 1 }
    val v = u == u
    ret 0
