// error: This arm of a `when` used as a value has no value
fn main(): Int do
    val x = 1
    val y = when x do
        1 do 2
        else do
            val z = 3
    ret y
