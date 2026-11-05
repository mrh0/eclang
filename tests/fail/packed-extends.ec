// error: can't take part in `extends`
rec A as
    x: Int

packed rec B extends A as
    y: Int

fn main(): Int do
    ret 0
