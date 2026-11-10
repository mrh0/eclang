// error: an `out` parameter also takes a nullable variable, but only of a pointer type

use "math.h"
declare fn split extern "modf" as (x: Double, out whole: Double): Double

fn main() do
    var whole: Double? = null
    val f = split(1.5, mut whole)
