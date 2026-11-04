// error: is given twice: by an argument in order, and by name
fn add(a: Int, b: Int): Int = a + b

fn main(): Int do
    ret add(1, a = 2)
