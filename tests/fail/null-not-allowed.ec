// error: null is not allowed here. To allow it, make the type nullable: `CString?`
fn main(): Int do
    val s: CString = null
    ret 0
