use "core:io.ec"

mut rec R as
    x: Int

fn main() do
    val r = new R { x = 0 }
    var p = r
    val q = addrof p
    (@q).x = 1
    (@q).x += 2
    io.log(r.x)
