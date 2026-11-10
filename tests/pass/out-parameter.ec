use "core:io.ec"
use "stdlib.h"
use "math.h"

// C fills these in: `out` takes a variable that may still be null (here a CString?), and `mut` one C reads first.
declare fn parse extern "strtol" as (s: CString, out end: CString, base: Int): Long
declare fn split extern "modf" as (x: Double, out whole: Double): Double
declare fn create extern "apr_pool_create_ex" as (out made: Pool, parent: Pool, abort: @Any?, allocator: @Any?): Int

fn main() do
    var end: CString? = null
    val n = parse("42 rest"c, mut end, 10)
    if val rest = end do
        io.log("${n} '${rest}'")
    var whole = 0.0
    val fraction = split(3.25, mut whole)
    io.log("${whole} ${fraction}")
    var p: Pool? = null
    io.log("${create(mut p, pool, null, null)} ${p != null}")
