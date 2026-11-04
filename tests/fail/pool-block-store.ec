// error: Can't store this in 'kept': it points to memory from the pool of the `pool` block at line 5, which is freed when that block ends, but 'kept' lives longer
fn main(): Int do
    var kept: String = "start"
    val n = 1
    pool scratch do
        kept = clone("copy ${n}")
    ret 0
