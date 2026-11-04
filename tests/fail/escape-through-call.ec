// error: Can't return this: it points to 'x', a local variable
use "core:io.ec"

// A function's result may point into its arguments.
fn id(p: @Int): @Int = p

fn leak(): @Int do
    var x = 42
    ret id(addrof x)

fn main(): Int do
    io.log(@leak())
    ret 0
