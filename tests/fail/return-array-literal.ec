// error: it points to the elements of an array literal (on the stack), which is freed when the function returns, so it would point to freed memory. Copy it into a pool with `clone(...)`
fn numbers(): Int[] = [1, 2, 3]

fn main(): Int = 0
