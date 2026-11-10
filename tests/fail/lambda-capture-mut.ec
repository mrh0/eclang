// error: A lambda can't use 'total', a `mut` parameter
fn add(mut total: Int): () => Int = fn () = total + 1
fn main(): Int do
    var t = 1
    val f = add(mut t)
    ret f()
