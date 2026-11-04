use "core:io.ec"
use "core:apr/pools.ec"
use "core:apr/strings.ec"
use "core:apr/tables.ec"
use "core:apr/hash.ec"
use "core:apr/time.ec"

// The APR bindings: a memory pool, and strings, tables and hash tables allocated in it.

fn main(): Int do
    var pool: @apr_pool_t? = null
    ret 1 if apr_pool_create_core(addrof(pool!)) != APR_SUCCESS
    val p = pool!
    defer apr_pool_destroy(p)

    io.log(apr_pstrcat(p, "pool"c, "-"c, "allocated"c, null))
    io.log(apr_psprintf(p, "%d + %d = %d"c, 2, 3, 2 + 3))
    io.log(apr_itoa(p, 42))

    val table = apr_table_make(p, 4)
    apr_table_set(table, "lang"c, "eclang"c)
    io.log(apr_table_get(table, "lang"c) ?? "missing"c)
    io.log(apr_table_get(table, "other"c) ?? "missing"c)

    val hash = apr_hash_make(p)
    var answer = 42
    apr_hash_set(hash, "answer"c, APR_HASH_KEY_STRING, addrof(answer))
    io.log(apr_hash_count(hash))
    val found = apr_hash_get(hash, "answer"c, APR_HASH_KEY_STRING)
    if found != null do
        io.log(@(found !as @Int))
    io.log(apr_hash_get(hash, "nothing"c, APR_HASH_KEY_STRING) == null)

    var parts = 0
    var last: CString = ""c
    var token = apr_strtok(apr_pstrdup(p, "a,b,c"c), ","c, addrof(last))
    while token != null do
        parts = parts + 1
        token = apr_strtok(null, ","c, addrof(last))
    io.log(parts)

    var t: apr_time_exp_t = apr_time_exp_t { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }
    ret 1 if apr_time_exp_gmt(addrof(t), 86400 * 1000000) != APR_SUCCESS
    io.log(t.tm_mday)
    io.log(t.tm_year + 1900)
    ret 0
