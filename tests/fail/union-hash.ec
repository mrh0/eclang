// error: which field it holds isn't known
union U as
    a: Int
    b: Float

fn main(): Int do
    val h = hash(U { a = 1 })
    ret 0
