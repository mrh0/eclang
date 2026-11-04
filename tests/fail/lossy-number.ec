// error: Long → Int may lose data (Int is smaller); convert it explicitly with `as Int`
fn main(): Int do
    val l: Long = 5
    val i: Int = l
    ret 0
