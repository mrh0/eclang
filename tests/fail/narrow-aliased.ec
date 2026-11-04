// error: This pointer may be null
use "core:io.ec"

// A check doesn't narrow a variable whose address is taken: a write through the pointer could undo it.
fn main(): Int do
    var a: @Int? = new 5
    val pa = addrof a
    if a != null do
        @pa = null
        io.log(@a)
    ret 0
