// error: the global 'g' still points to memory of pool 'p'
var g = "none"
fn main(): Int do
    val p = new Pool
    g = format(p, "x${1}")
    destroy p
    ret 0
