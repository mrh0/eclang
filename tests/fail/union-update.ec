// error: share their memory
union U as
    a: Int
    b: Float

fn main(): Int do
    val u = U { a = 1 }
    val v = U { ...u, a = 2 }
    ret 0
