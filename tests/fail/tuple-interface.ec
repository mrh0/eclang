// error: A tuple can't hold this: interface Named
inter Named as
    name: String

fn f(t: (Named, Int)): Int = t.second

fn main(): Int = 0
