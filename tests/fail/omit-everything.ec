// error: leaves out every atom
type Dir = :up | :down
type Bad = Omit<Dir, :up, :down>
fn main() do
    pass
