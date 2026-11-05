// error: Can't return this: it points to a temporary value (on the stack)
rec P as
    x: Int
fn f(): @P = addrof P { x = 1 }
fn main(): Int do
    ret f().x
