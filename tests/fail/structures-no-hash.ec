// error: `hash` can't be generated for a Row, because `==` can't compare it
use "core:structures.ec"

rec Row as
    cells: Int[]

fn main(): Int do
    val m: @Map<Row, Int> = map()
    m.put(Row { clone([1]) }, 2)
    ret 0
