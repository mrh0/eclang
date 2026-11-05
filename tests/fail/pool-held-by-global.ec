// error: the global 'g' still points to memory of pool 'p'
var g = "none"
fn main(): Int do
    val p = newPool()
    g = format(p, "x${1}")
    freePool(p)
    ret 0
