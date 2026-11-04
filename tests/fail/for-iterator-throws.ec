// error: because it can fail
use "core:errors.ec"
rec Thing as
    n: Int
fn next(t: @Thing): Int? throws Error do
    throw Error { 1, "no" } if t.n == 0
    ret null
fn main() do
    for x in Thing { 1 } do
        pass
