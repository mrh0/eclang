// error: `new Holder` makes an empty Holder, but its field `target` has no empty value
rec Holder as
    target: @Int

fn main(): Int do
    val h = new Holder
    ret 0
