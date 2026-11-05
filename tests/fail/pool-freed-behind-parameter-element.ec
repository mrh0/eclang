// error: `names[...]` is the caller's memory and still points to memory of pool 'p'
fn fill(names: String[], p: Pool) do
    names[0] = format(p, "hello ${1}")
    clearPool(p)

fn main(): Int do
    ret 0
