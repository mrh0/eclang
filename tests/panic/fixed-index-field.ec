// panic: index 4 is out of bounds for length 2
rec R as
    a: Int[2]

fn main(): Int do
    var r = R { [1, 2] }
    var i = 4
    r.a[i] = 1
    ret 0
