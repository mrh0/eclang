module "geo"
use "core:io.ec"

rec Point as
    x: Int
    y: Int

val origin = Point { x = 0, y = 0 }
var created = 0

fn make(x: Int, y: Int): Point do
    created = created + 1
    ret Point { x = x, y = y }

fn square(v: Int): Int = v * v

// Private to geo: only this module's code can use them.
priv rec Delta as
    dx: Int
    dy: Int

priv val NONE = 0

priv fn delta(a: Point, b: Point): Delta = Delta { a.x - b.x + NONE, a.y - b.y }

fn distance2(a: Point, b: Point): Int do
    val d = delta(a, b)
    ret square(d.dx) + square(d.dy)

// geo's own `log`, beside io.log: `geo.log(p)`.
fn log(p: Point) do
    io.log(p.x)
    io.log(p.y)

const UNIT = 100
