// error: Missing field(s) of P: x: Int. Give them a value, or give the field a default in the record (`x: Int = ...`).
rec P as
    x: Int
    y: Int = 1

fn main(): Int do
    val p = P { y = 2 }
    ret 0
