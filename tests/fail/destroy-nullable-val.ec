// error: `destroy p` sets the `Pool?` 'p' to null once it is freed, so it must be a `var`
fn main(): Int do
    val p: Pool? = if 1 > 0 do new Pool else null
    destroy p
    ret 0
