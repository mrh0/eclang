// panic: the position is outside the Grid
use "core:io.ec"
use "core:grid.ec"

fn main(): Int do
    val g = grid(3, 2, 0)
    io.log(g.get(1, 2))
    ret 0
