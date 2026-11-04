// error: Argument 1 should be Int: Long → Int may lose data
fn twice(x: Int): Int = x * 2

fn main(): Int do
    val big: Long = 5
    ret twice(big)
