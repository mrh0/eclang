// error: Reading '.s' of union Text isn't checked
union Text as
    n: Long
    s: String

fn main() do
    val t = Text { s = "hi" }
    val c = t.s[0]
