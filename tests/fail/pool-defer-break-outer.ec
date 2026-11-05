// error: Can't use 's' here
use "core:io.ec"

fn main() do
    val p = newPool()
    var s: String = "a"
    outer: for i in 0..<3 do
        for j in 0..<3 do
            defer clearPool(p)
            s = format(p, "x${i}")
            break outer
    io.log(s)
