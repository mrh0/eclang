// error: Can't take the address of bit-field 'a'
rec R as
    a: UInt : 3

fn main(): Int do
    var r = R { 1 }
    val p = addrof r.a
    ret 0
