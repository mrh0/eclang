// error: Value isn't a `mut union`: its fields are fixed once it is made. Make a new one with the change instead, `Value { i = value }`, or declare `mut union Value`
union Value as
    i: Int
    f: Float

fn main() do
    var v = Value { i = 1 }
    v.i = 2
