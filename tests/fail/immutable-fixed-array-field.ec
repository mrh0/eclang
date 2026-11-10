// error: Grid isn't a `mut rec`: its fields are fixed
rec Grid as
    cells: Int[4]

fn main() do
    var g = Grid { [1, 2, 3, 4] }
    g.cells[0] = 9
