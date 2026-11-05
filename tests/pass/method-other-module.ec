use "core:io.ec"
use "modules/closable.ec"

inter Closer as
    fn close()
    fn label(): String

fn shut(c: @Closer): String do
    c.close()
    ret c.label()

// What this code sees comes first.
fn weight(d: @closable.Door): Int = 99

fn main() do
    val d = closable.door("oak")
    // A function of the module that declares the type, as a method.
    d.close()
    io.log("${d.open} ${d.label()}")
    // With arguments, a default, a named argument and a lambda that gets its type from the function.
    val g = closable.gate("iron", 3)
    // A function of the parent's module.
    io.log("${g.label()}")
    io.log("${d.heavy()} ${d.heavy(3)} ${d.heavy(scale = 4)}")
    d.iterate(fn (s) = io.log(s))
    // An interface asks for what the type's module has.
    io.log(shut(closable.door("ash")))
    io.log(shut(g))
    // `?.` and `try`.
    val m = closable.maybe(true)
    io.log(m?.label() ?? "none")
    io.log(closable.maybe(false)?.label() ?? "none")
    val open = d.opened() ?? false
    io.log("${open}")
    io.log("${d.weight()}")
    // The module's function beats a builtin.
    io.log(d.clone())
