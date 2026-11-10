// error: `clone` is built in, and takes values: none of its parameters is `mut`. Remove `mut`.

fn main() do
    var s = "a"
    val t = clone(mut s)
