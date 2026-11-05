use "core:io.ec"
use "core:csv.ec"

rec Options as
    width: Int = 80
    name: String = "x"

rec Box<T> as
    value: Int = 1
    other: T? = null

fn main() do
    val o = Options {}
    io.log("${o.width} ${o.name}")
    val b = Box<Int> {}
    io.log(b.value)
    val p = new Options {}
    io.log(p.width)
    val c = csv.Options {}
    io.log(c.separator)
