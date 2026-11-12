use "core:io.ec"

type Dir = :north | :south | :east | :west
type Vertical = :north | :south
type Horizontal = Omit<Dir, Vertical>
type NotWest = Omit<Dir, :west>

// Exhaustive without :north and :south.
fn turn(d: Horizontal): Horizontal do
    when d do
        :east do ret :west
        :west do ret :east

fn name(d: NotWest): CString do
    when d do
        :north do ret "north"c
        :south do ret "south"c
        :east do ret "east"c

fn widen(d: Omit<Dir, :north, :south, :east>): Dir = d

fn main(): Int do
    io.log(turn(:east))
    io.log(name(:south))
    val d: Dir = widen(:west)
    io.log(d)
    val h: Horizontal = :west
    io.log(turn(h))
    ret 0
