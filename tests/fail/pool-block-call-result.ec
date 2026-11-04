// error: Can't store this in 'name': it points to memory from the pool of the `pool` block at line 6, which is freed when that block ends
fn describe(n: Int): String = "number ${n}"

fn main(): Int do
    var name = "none"
    pool do
        name = describe(1)
    ret 0
