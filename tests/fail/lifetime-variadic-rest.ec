// error: Can't return this: it points to 'r', the extra arguments of the call
fn f(...r: Int): Int[] = r
fn main(): Int do
    ret f(1, 2)[0]
