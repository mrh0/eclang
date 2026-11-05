// error: Can't use 's' here: it points to memory of pool 'p'
use "core:io.ec"
fn main(): Int do
    val p = newPool()
    var s = ""
    val ps = addrof s
    @ps = format(p, "x${1}")
    clearPool(p)
    io.log(s)
    ret 0
