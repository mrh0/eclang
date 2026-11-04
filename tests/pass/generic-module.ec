use "core:io.ec"
use "lib/crate.ec"

// The same name as the module's, and the same C-looking instance name, don't clash.
rec Crate as
    id: Int

fn weight(c: crate.Crate<Int>): Int = c.item * 2

fn main(): Int do
    val c = crate.pack(21, "answer")
    io.log(weight(c))
    io.log(crate.labelOf(c))
    val typed: crate.Crate<String> = crate.pack("s", "text")
    io.log(typed.item)
    io.log(crate.hiddenValue(5))
    val literal = crate.Crate<Double> { 2.5, crate.pack(0, "x").label }
    io.log(literal.item)
    io.log(Crate { 3 }.id)
    ret 0
