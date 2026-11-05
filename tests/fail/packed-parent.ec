// error: can't take part in `extends`
align(16) rec A as
    x: Int

rec B extends A as
    y: Int

fn main(): Int do
    ret 0
