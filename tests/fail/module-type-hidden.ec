// error: Unknown type named 'Point'. It is defined in module 'shapes' and module 'geo'; write `shapes.Point`.
use "../pass/modules/shapes.ec"

fn main(): Int do
    val p: Point = geo.make(1, 2)
    ret 0
