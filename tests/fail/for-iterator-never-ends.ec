// error: so the loop could never end
rec Thing as
    n: Int
fn next(t: @Thing): Int = t.n
fn main() do
    for x in Thing { 1 } do
        pass
