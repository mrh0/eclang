// error: A `when` used as a value must give a value in every case
fn main(): Int do
    val x = 1
    val y = when x do
        1 do 2
    ret y
