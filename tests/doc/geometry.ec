/**
 * Points and shapes on a plane.
 *
 * All lengths are in the same unit; nothing here converts between units.
 *
 * @author Ada
 * @since 0.1
 */
module "geo"

/** How close two lengths must be to count as equal. */
const EPSILON: Double = 0.000001

/** A point on the plane. */
rec Point as
    /** Distance from the y axis. */
    x: Double
    /** Distance from the x axis. */
    y: Double = 0.0

/** Something with an area. See {@link area}. */
inter Shape as
    /** How wide it is. */
    width: Double
    fn area(): Double

/**
 * The distance between [a] and [b], always zero or more:
 *
 *     val d = distance(Point{0.0, 0.0}, Point{3.0, 4.0})   // 5.0
 *
 * @param a where to start
 * @param b where to end; the same as {@code a} gives 0
 * @return the straight-line distance
 * @see Point#x
 * @see area the area of a shape
 */
fn distance(a: Point, b: Point): Double = 0.0

/**
 * Parses "x,y".
 * @param text the text to parse,
 *     over two lines
 * @throws when `text` has no comma
 */
fn parse(text: String): Point throws Error do
    ret Point{0.0, 0.0}

/**
 * The area of a square.
 * @deprecated use {@link distance} instead
 */
fn area(side: Double): Double = side * side

priv fn hidden(): Int = 1

fn undocumented(n: Int): Int = n
