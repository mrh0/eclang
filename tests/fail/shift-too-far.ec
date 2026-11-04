// error: Can't shift a Int by 32 bits: it only has 32. Convert it to a wider type first, e.g. `(x as Long) << 32`.
fn main(): Int do
    val x = 1
    ret x << 32
