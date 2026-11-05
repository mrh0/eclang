// error: 4 doesn't fit bit-field 'a', which has 3 bit(s): it holds -4 to 3
rec R as
    a: Int : 3

fn main(): Int do
    val r = R { 4 }
    ret 0
