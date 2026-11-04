// error: There is no module called 'geom' (in the type geom.Point).
use "../pass/modules/geo.ec"

fn main(): Int do
    val p: geom.Point = geo.make(1, 2)
    ret 0
