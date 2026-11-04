// error: Can't return this: it points to 'n', a local variable
rec Holder as
    p: @Int
    v: Int

fn f(h: Holder): Holder do
    val n = 1
    ret Holder { ...h, p = addrof n }

fn main(): Int do
    val h = Holder { new 1, 2 }
    ret f(h).v
