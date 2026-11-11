// error: A slice shares the memory of what it slices, so it can't run backwards
fn main() do
    val s = "hello"
    val t = s[3..>0]
