use "core:io.ec"
use "modules/geo.ec"
use "modules/shapes.ec"

fn square(v: Int): Int = -v

fn main(): Int do
    val a = geo.make(3, 4)
    val b: geo.Point = geo.origin
    io.log(geo.distance2(a, b))
    io.log(geo.square(5))
    io.log(shapes.square(2))
    io.log(square(7))
    geo.log(a)
    io.log(shapes.named().label)
    io.log(shapes.corner().x)
    io.log(geo.created)
    val p = geo.Point { x = 9, y = 8 }
    io.log(p.y)
    val f: (Int) => Int = fn geo.square
    io.log(f(3))
    ret 0
