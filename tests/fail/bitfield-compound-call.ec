// error: would run the calls in this target twice
mut rec R as
    a: UInt : 3

var r = R { 1 }

fn get(): @R = addrof r

fn main(): Int do
    val d: UInt = 2
    get().a /= d
    ret 0
