// error: Argument 1 should be :south | :east | :west
type Dir = :north | :south | :east | :west
fn f(d: Omit<Dir, :north>) do
    pass
fn main() do
    f(:north)
