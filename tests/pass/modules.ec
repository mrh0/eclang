use "core:lib.ec"
use "modules/geo.ec"
use "modules/shapes.ec"

fn square(v: Int): Int = -v

fn main(): Int do
    val a = geo.make(3, 4)
    val b: geo.Point = geo.origin
    log(geo.distance2(a, b))
    log(geo.square(5))
    log(shapes.square(2))
    log(square(7))
    geo.log(a)
    log(shapes.named().label)
    log(shapes.corner().x)
    log(geo.created)
    val p = geo.Point { x = 9, y = 8 }
    log(p.y)
    val f: (Int) => Int = fn geo.square
    log(f(3))
    ret 0
