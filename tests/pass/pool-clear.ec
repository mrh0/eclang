use "core:io.ec"

// A pool that is cleared every round: what is allocated after the clear can be used.

fn main(): Int do
    val p = new Pool
    defer destroy p
    var last = "none"
    for i in 0..<3 do
        clearPool(p)
        val s = format(p, "round ${i}")
        io.log(s)
        last = s
    io.log(last)
    if last.len > 100 do
        ret 1
    io.log(format(p, "after"))
    ret 0
