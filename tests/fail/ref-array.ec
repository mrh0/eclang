// error: `new Counter[n]` fills the array with empty values, but a Counter can't be null and has no empty value.

ref rec Counter as
    count: Int = 0

fn main() do
    val cs = new Counter[3]
