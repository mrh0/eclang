// error: `+` needs two numbers, but got Int?
use "core:lib.ec"

// The loop sets v to null, so in its next round v may be null: the narrowing from the `if` no longer holds.
fn main(): Int do
    var v: Int? = 1
    if v != null do
        while true do
            log(v + 1)
            v = null
    ret 0
