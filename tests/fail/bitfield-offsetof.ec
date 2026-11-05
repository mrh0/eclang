// error: `offsetof` can't give where bit-field 'a' starts
rec R as
    a: UInt : 3

fn main(): Int do
    val n = offsetof(R, a)
    ret 0
