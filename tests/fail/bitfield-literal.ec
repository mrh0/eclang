// error: 8 doesn't fit bit-field 'a', which has 3 bit(s): it holds 0 to 7
mut rec R as
    a: UInt : 3

fn main(): Int do
    var r = R { 1 }
    r.a = 8
    ret 0
