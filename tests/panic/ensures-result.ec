// panic: result = -6
fn get(a: Int[], i: Size): Int requires i < a.len ensures result >= 0 do
    ret a[i]

fn main(): Int do
    ret get([4, 5, -6], 2)
