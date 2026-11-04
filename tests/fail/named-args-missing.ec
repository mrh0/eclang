// error: also needs its parameter 'b'
fn add(a: Int, b: Int, c: Int = 0): Int = a + b + c

fn main(): Int do
    ret add(1, c = 2)
