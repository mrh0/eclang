// `T[N]`: the elements are part of the value, so it copies like a number, and a record holds them like a C struct.
use "core:io.ec"
use "core:structures.ec"

const SIDE = 2
const AREA = SIDE * SIDE
const TYPED: Size = 3

rec Grid as
    id: Int
    cells: Int[AREA]
    tag: Char[4] = ['a', 'b', 0 as Char, 'z']

rec Base as
    ids: Int[2]

rec Child extends Base as
    extra: Int

rec Box<T> as
    items: T[2]

rec Named as
    names: String[2]
    next: @Named?

type Pair = distinct Int[2]

var counts: Int[TYPED] = [1, 2, 3]
var grid = Grid { 1, [1, 2, 3, 4] }

fn sum(a: Int[]): Int do
    var t = 0
    for x in a do
        t += x
    ret t

fn doubled(a: Int[3]): Int[3] do
    var b = a
    for i in 0..<b.len do
        b[i] = b[i] * 2
    ret b

fn firstOf(a: <T>[2]): <T> = a[0]

fn fill(a: @(Int[3])) do
    a.data[0] = 70
    (@a)[1] = 80

fn main(): Int do
    // Empty, literal, copies
    var a: Int[3]
    io.log("${a} ${a.len} ${sizeof Int[3]} ${alignof Int[3]}")
    val b: Int[3] = [1, 2, 3]
    a = b
    a[0] = 10
    io.log("${a} ${b} ${a == b}")
    io.log("${doubled(b)} ${doubled(b)[2]}")

    // As an array: slices share the memory
    io.log(sum(b[..]))
    var s = a[1..]
    s[0] = 20
    io.log("${a} ${s.len}")
    for i, x in a do
        io.log("${i}: ${x}")

    // Records
    var g = Grid { 1, [1, 2, 3, 4] }
    g.cells[1] = 20
    var h = g
    h.cells[1] = 21
    val u = Grid { ...g, id = 2 }
    io.log("${g}")
    io.log("${h.cells} ${u.cells} ${u.id} ${g == h} ${g == Grid { 1, [1, 20, 3, 4] }}")
    io.log(hash(g) == hash(Grid { 1, [1, 20, 3, 4] }))
    g.cells = [5, 6, 7, 8]
    val cells = g.cells
    g.cells[0] = 0
    io.log("${cells} ${g.cells}")
    io.log(g.tag.data)
    val p = addrof g
    p.cells[3] = 80
    io.log("${p.cells} ${@p}")
    val maybe: @Grid? = p
    val some = maybe?.cells
    if some != null do
        io.log("${some}")

    // Globals
    counts[1] = 20
    grid.cells[0] = 9
    io.log("${counts} ${grid.cells}")

    // Records that extend, generic records and functions
    val c = Child { [1, 2], 3 }
    val base = c as Base
    io.log("${base} ${c}")
    val box = Box<Int> { [1, 2] }
    val strings = Box<String> { ["a", "b"] }
    io.log("${box} ${firstOf(box.items)} ${firstOf(strings.items)}")

    // Inside other values
    val nested: Int[2][3] = [[1, 2], [3, 4], [5, 6]]
    io.log("${nested} ${nested[2][1]} ${sizeof Int[2][3]}")
    val tuple = (b, 1)
    io.log("${tuple.first}")
    var optional: Int[2]? = null
    io.log(optional == null)
    optional = [1, 2]
    if optional != null do
        io.log("${optional[1]}")
    var mixed: Int[2] | CString = "text"
    mixed = [3, 4]
    if mixed is Int[2] do
        io.log("${mixed}")
    val list = List<Int[2]> { 0 }
    val distinct_ = [1, 2] as Pair
    io.log("${distinct_ as Int[2]}")

    // Values that own memory: `clone` copies what the elements point to
    val named = new Named { ["x", "y"], null }
    val copy = clone(named)
    io.log("${@copy}")
    io.log(copy.names[1] == named.names[1])
    io.log(copy.names.data != named.names.data)

    // Pointers
    var filled: Int[3]
    fill(addrof filled)
    val second = addrof filled[1]
    @second = 81
    io.log("${filled}")
    ret 0
