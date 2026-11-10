// error: 'drop' takes over the pool 'p' (`own`) and may free it, so it must be a pool this function made with `new Pool`, but 'q' is a parameter
fn drop(own p: Pool) do
    destroy p

fn hand(q: Pool) do
    drop(q)

fn main(): Int = 0
