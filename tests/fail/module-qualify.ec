// error: Function 'make' is not defined. It is defined in module 'geo'; write `geo.make`.
use "core:lib.ec"
use "../pass/modules/geo.ec"

fn main(): Int do
    val p = make(1, 2)
    ret 0
