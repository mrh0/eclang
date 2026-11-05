// A field's default is compiled in the record's module, also when the record is made in another one.
use "core:io.ec"
use "modules/shade.ec"

fn main(): Int do
    val p = shade.Paint { coats = 2 }
    io.log(p.tint.level)
    io.log(p.other.level)
    io.log(p.coats)
    val q = shade.Primer { coats = 4 }
    io.log(q.tint.level)
    io.log(q.other.level)
    ret 0
