// error: `ref` goes before `rec`

ref union U as
    a: Int
    b: Float

fn main() do
    val u = U { 1 }
