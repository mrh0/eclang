// error: Can't return this: it points to the elements of an array literal (on the stack)
fn f(a: Int[] = [1, 2, 3]): Int[] = a
fn main(): Int do
    ret f()[0]
