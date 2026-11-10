// error: `destroy p` frees a pool this function didn't make with `new Pool`: 'p' is a parameter, which the caller may still use
fn drop(p: Pool) do
    destroy p

fn main(): Int do
    drop(new Pool)
    ret 0
