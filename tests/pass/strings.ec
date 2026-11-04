use "core:lib.ec"
use "core:apr/pools.ec"

// Slicing, `\u` escapes, and building Strings in a pool with `format` and `concat`.
rec Point as
    x: Int
    y: Int

fn main(): Int do
    var pool: @apr_pool_t? = null
    ret 1 if apr_pool_create_core(addrof(pool!)) != APR_SUCCESS
    val mem = pool!
    defer apr_pool_destroy(mem)

    val s = "hello world"
    log(s[0..<5])
    log(s[6..=10])
    log(s[3..<3].len)
    log(s[4])
    val nums = [1, 2, 3, 4, 5]
    val mid = nums[1..<4]
    log(mid.len)
    log(mid[0])
    mid[0] = 20
    log(nums[1])

    log("café \u{1F600}")
    log("é".len)
    log("cost: \$5")

    val name = "world"
    val p = Point { x = 3, y = 4 }
    val big: Long = 5000000000
    log(format(mem, "hello ${name}! ${p.x + p.y} ${p.x * 2 == 6} ${'c'} ${:ok} ${big} ${0.5} 100%"))
    log(mem.format("[${s[0..<4]}]"))
    val joined = concat(mem, "ab", name, "cd")
    log(joined)
    log(joined.len)
    ret 0
