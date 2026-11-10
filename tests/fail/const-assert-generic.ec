// error: greet needs a name: `const assert <T> fits Named` failed for <T> = Pt (the call at const-assert-generic.ec:13)
use "core:io.ec"
inter Named as
    name: String
rec Pt as
    x: Int
rec Person as
    name: String
fn greet(x: <T>): String do
    const assert <T> fits Named, "greet needs a name"
    ret "hello"
fn main(): Int do
    io.log(greet(Pt { 1 }))
    ret 0
