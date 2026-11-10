// error: 'keep' keeps 'p' in a global, but here 'p' points to 'x', a local variable
use "core:io.ec"
use "core:structures.ec"

var g: @Int? = null
fn keep(p: @Int) do
    g = p
fn main() do
    val x = 5
    keep(addrof x)
