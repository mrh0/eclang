// error: needs a `next` that takes a pointer
rec Thing as
    n: Int
fn next(t: Thing): Int? = t.n
fn main() do
    for x in Thing { 1 } do
        pass
