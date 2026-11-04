// error: Can't put a Point into a String with `${...}`. Put its fields in instead, e.g. `${p.x}`.
use "core:apr/pools.ec"

rec Point as
    x: Int
    y: Int

fn main(): Int do
    var pool: @apr_pool_t? = null
    ret 1 if apr_pool_create_core(addrof(pool!)) != APR_SUCCESS
    val p = Point { x = 1, y = 2 }
    val s = format(pool!, "p is ${p}")
    ret 0
