use "core:io.ec"
use "modules/shapes-base.ec"

// A record can extend a record of another module, by its qualified name, also when written before it is known.
rec Square extends shapesbase.Named as
    size: Int

rec Plain extends shapesbase.Shape

fn sides(s: @shapesbase.Shape): Int = s.sides

fn main() do
    val q = new Square { sides = 4, name = "square", size = 2 }
    io.log("${q.name} ${q.size} ${sides(q)}")
    val p = new Plain { sides = 3 }
    io.log(sides(p))
