// error: `destroy` frees a whole pool (a `Pool`), but this is a @Int: one value can't be freed on its own
fn main(): Int do
    val x = new 5
    destroy x
    ret 0
