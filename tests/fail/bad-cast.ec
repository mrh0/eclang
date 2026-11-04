// error: Use `!as` for an unchecked C cast
fn main(): Int do
    val p: @Int? = null
    val s = p as CString
    ret 0
