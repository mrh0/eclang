// error: must be an atom or a union of atoms
type Bad = Omit<Int, :a>
fn main() do
    val b: Bad = 1
