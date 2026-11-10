// error: Counter is a `ref rec`, so its values live in a pool and are used through pointers. Make one with `new Counter { ... }`

ref rec Counter as
    count: Int = 0

fn main() do
    val c = Counter { 1 }
