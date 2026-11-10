// error: Can't store this in 'kept': it points to memory from the pool of the `pool` block at line 5, which is freed when that block ends, before 'kept' is. Keep a copy instead: wrap the value in `keep(...)`
fn main(): Int do
    var kept: String = "start"
    val n = 1
    pool scratch do
        kept = clone("copy ${n}")
    ret 0
