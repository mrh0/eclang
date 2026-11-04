// error: Can't return this: it points to 'n', a local variable
fn f(c: Int): @Int do
    val n = 1
    ret when c do
        1 do addrof n
        else do new 5

fn main(): Int do
    ret @f(1)
