// error: Missing field(s) of R: x: Int
rec R as
    x: Int
    y: Int = 1
fn main() do
    val r = R {}
