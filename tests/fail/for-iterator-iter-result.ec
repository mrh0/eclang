// error: loops over what `iter` or `pairs` gives, but this is a Int
rec Thing as
    n: Int
fn iter(t: Thing): Int = t.n
fn main() do
    for x in Thing { 1 } do
        pass
