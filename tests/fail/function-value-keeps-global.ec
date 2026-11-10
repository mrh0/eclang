// error: `fn keep` can't be used as a function value: it keeps 's' in a global
var saved: String = "none"

fn keep(s: String) do
    saved = s

fn main(): Int do
    val f = fn keep
    f("x")
    ret 0
