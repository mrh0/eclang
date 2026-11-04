module "geo"
use "core:lib.ec"

rec Point as
    x: Int
    y: Int

val origin = Point { x = 0, y = 0 }
var created = 0

fn make(x: Int, y: Int): Point do
    created = created + 1
    ret Point { x = x, y = y }

fn square(v: Int): Int = v * v

fn distance2(a: Point, b: Point): Int = square(a.x - b.x) + square(a.y - b.y)

// Adds an overload to the top level's `log`; inside this module both are visible.
fn log(p: Point) do
    log(p.x)
    log(p.y)

const UNIT = 100
