// error: :up isn't one of the atoms of :north | :south | :east | :west
type Dir = :north | :south | :east | :west
type Bad = Omit<Dir, :up>
fn main() do
    val b: Bad = :north
