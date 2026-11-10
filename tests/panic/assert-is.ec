// panic: v holds a String
fn main(): Int do
    val v: Int | String = "text"
    assert v is Int
    ret 0
