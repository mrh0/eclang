// error: doesn't fit bit-field 'a'
rec R as
    a: UInt : 1 = 2
    b: Int

fn main(): Int do
    val r = R { b = 1 }
    ret 0
