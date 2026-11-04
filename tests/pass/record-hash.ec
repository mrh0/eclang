use "core:io.ec"
use "core:structures.ec"

rec Point as
    x: Int
    y: Int

/** The Char leaves padding before the Long, which must not count. */
rec Padded as
    c: Char
    n: Long

rec Label as
    name: String
    code: CString
    at: Point

rec Measure as
    value: Double

rec Node as
    value: Int

/** A `hash` of its own wins. */
rec Fixed as
    x: Int

fn hash(f: Fixed): ULong = 42ul

fn main(): Int do
    // Equal values have equal hashes, like `==` says.
    io.log(hash(Point { 1, 2 }) == hash(Point { 1, 2 }))
    io.log(hash(Point { 1, 2 }) == hash(Point { 2, 1 }))
    io.log(Point { 1, 2 }.hash() == hash(Point { 1, 2 }))
    io.log(hash(Padded { 'a', 5 }) == hash(Padded { 'a', 5 }))

    val first = concat("na", "me")
    val second = concat("nam", "e")
    val one = Label { first, cstr(first), Point { 1, 2 } }
    val two = Label { second, cstr(second), Point { 1, 2 } }
    io.log(one == two)
    io.log(hash(one) == hash(two))

    val zero = Measure { 0.0 }
    val negative = Measure { -0.0 }
    io.log(zero == negative)
    io.log(hash(zero) == hash(negative))

    // Pointers compare by address.
    val a = new Node { 1 }
    val b = new Node { 1 }
    io.log(hash(a) == hash(a))
    io.log(hash(a) == hash(b))

    val pair = (1, "x")
    io.log(hash(pair) == hash((1, "x")))
    io.log(hash(Fixed { 1 }))

    // Keys of a Map and a Set need nothing more.
    val names: @Map<Point, String> = map()
    for i in 0..<100 do
        names.put(Point { i, i * 2 }, "p${i}")
    io.log(names.len)
    io.log(names.get(Point { 7, 14 }) ?? "none")
    io.log(names.get(Point { 7, 15 }) ?? "none")

    val seen: @Set<Label> = set()
    seen.add(one)
    seen.add(two)
    io.log(seen.size())

    val cells: @Set<(Int, Int)> = set()
    cells.add((1, 2))
    cells.add((1, 2))
    cells.add((2, 1))
    io.log(cells.size())

    val nodes: @Set<@Node> = set()
    nodes.add(a)
    nodes.add(a)
    nodes.add(b)
    io.log(nodes.size())
    ret 0
