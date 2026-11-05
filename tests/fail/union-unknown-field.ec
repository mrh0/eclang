// error: U has no field 'c'
union U as
    a: Int
    b: Float

fn main(): Int do
    val u = U { c = 1 }
    ret 0
