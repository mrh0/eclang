// error: 'apr_hash_set' keeps 'value' in what 'ht' points to, but here 'value' points to 'x', a local variable
use "core:apr/hash.ec"

fn main(): Int do
    val h = apr_hash_make(pool)
    var x = 1
    apr_hash_set(h, "x"c, APR_HASH_KEY_STRING, addrof x)
    ret 0
