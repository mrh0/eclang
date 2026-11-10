// error: `clearPool(p)` frees the memory in a pool this function didn't make with `new Pool`: 'p' is a parameter
fn reset(p: Pool) do
    clearPool(p)

fn main(): Int do
    reset(new Pool)
    ret 0
