// error: Can't return this: it points to the elements of an array literal (on the stack)
rec R as
    n: Int
    a: Int[] = [1, 2]
fn f(): R = R { 1 }
fn main(): Int do
    ret f().a[0]
