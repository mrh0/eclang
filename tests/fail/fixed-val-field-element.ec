// error: so its fields can't change either
mut rec R as
    a: Int[2]

fn main(): Int do
    val r = R { [1, 2] }
    r.a[0] = 3
    ret 0
