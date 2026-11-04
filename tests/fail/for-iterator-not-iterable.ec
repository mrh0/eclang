// error: To loop over a type of your own, define `fn next(x: @Thing): Item?`
rec Thing as
    n: Int
fn main() do
    for x in Thing { 1 } do
        pass
