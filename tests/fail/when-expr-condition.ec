// error: A `when` used as a value can't be used here
fn main(): Int do
    var x = 1
    while (when x do
        1 do true
        else do false
    ) do
        x = 2
    ret 0
