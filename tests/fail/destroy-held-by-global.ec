// error: Can't `destroy p` here: the global 'g' still holds this pool
var g: Pool? = null

fn main(): Int do
    val p = new Pool
    g = p
    destroy p
    ret 0
