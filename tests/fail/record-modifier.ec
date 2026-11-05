// error: `sealed` can't go before `rec` or `union`
sealed rec A as
    x: Int

fn main(): Int do
    ret 0
