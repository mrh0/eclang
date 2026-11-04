// error: `\u` in a string needs a Unicode code point
fn main(): Int do
    val s = "\uD800"
    ret 0
