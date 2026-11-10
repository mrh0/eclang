// error: `new (<T>)[n]` leaves its elements unset, and T may be a pointer
fn storage(n: Size, x: <T>): <T>[] do
    val out = new (<T>)[n]
    ret out

fn main(): Int do
    val s = storage(2, 1)
    ret 0
