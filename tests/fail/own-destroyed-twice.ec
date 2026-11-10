// error: Can't use the pool 'p' here: `drop(...)` at line 7, which took the pool over (`own`), freed it
fn drop(own p: Pool) do
    destroy p

fn main(): Int do
    val p = new Pool
    drop(p)
    destroy p
    ret 0
