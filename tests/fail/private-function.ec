// error: 'delta' is private to module 'geo' (`priv fn`), so only code in it can call it.
use "../pass/modules/geo.ec"

fn main(): Int do
    val d = geo.delta(geo.origin, geo.origin)
    ret 0
