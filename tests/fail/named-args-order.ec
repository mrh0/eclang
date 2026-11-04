// error: Arguments in order come first, then the named ones
fn add(a: Int, b: Int): Int = a + b

fn main(): Int do
    ret add(a = 1, 2)
