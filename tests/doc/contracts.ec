/**
 * The square root of [x], rounded down.
 *
 * @param x what to take the root of
 * @return the root
 */
fn isqrt(x: Int): Int requires x >= 0, "x must not be negative"  ensures result * result <= x do
    var r = 0
    while (r + 1) * (r + 1) <= x do
        r += 1
    ret r

/** Halves an even number. */
fn half(n: Int): Int requires n % 2 == 0 debug requires n - 1 > 0 debug ensures result > 0 = n / 2

/** No contracts here. */
fn twice(n: Int): Int = n * 2
