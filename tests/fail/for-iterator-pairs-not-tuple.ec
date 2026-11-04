// error: takes a tuple apart
rec Thing as
    n: Int
fn pairs(t: Thing): Int[] = [t.n]
fn main() do
    for a, b in Thing { 1 } do
        pass
