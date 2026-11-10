// error: `declare union` is C's `union x`, whose fields can always change, like a `declare struct`, so it takes no `mut`
declare mut union sigval as
    sival_int: Int

fn main() do
    var v: sigval
