module "shapes"
use "geo.ec"

// The same names as in geo, without clashing.
rec Point as
    label: CString

fn square(side: Int): Int = side * side * 100

fn named(): Point = Point { label = "p"c }

fn corner(): geo.Point = geo.make(1, 1)
