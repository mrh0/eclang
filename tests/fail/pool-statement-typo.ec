// error: `whlie x do` isn't a statement. A block that starts with a name is a pool block
fn main(): Int do
    val x = true
    whlie x do
        ret 1
    ret 0
