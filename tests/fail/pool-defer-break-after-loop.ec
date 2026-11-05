// error: Can't use 's' here
use "core:io.ec"

fn main() do
    val p = newPool()
    var s: String = "a"
    for i in 0..<3 do
        defer freePool(p)
        s = format(p, "x${i}")
        break
    io.log(s)
