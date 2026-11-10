// error: Can't use 's' here
use "core:io.ec"

fn main() do
    val p = new Pool
    var s: String = "a"
    for i in 0..<3 do
        defer destroy p
        s = format(p, "x${i}")
        break
    io.log(s)
