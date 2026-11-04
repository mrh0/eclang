// error: This pointer may be null
fn main(): Int do
    var one = 1
    var x: @Int? = addrof one
    var y = 0
    if x != null do
        (x, y) = (null, 1)
        ret @x
    ret 0
