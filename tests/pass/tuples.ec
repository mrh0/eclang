use "core:io.ec"
use "core:string.ec"

rec Point as
    x: Int
    y: Int

rec Segment as
    ends: (Point, Point)
    label: String

type Range = (Int, Int)

fn divmod(a: Int, b: Int): (Int, Int) = (a / b, a % b)

fn wide(): (Long, Double) = (1, 2)

fn safeDiv(a: Int, b: Int): (Int, Int) throws String do
    throw "division by zero" if b == 0
    ret divmod(a, b)

fn tryDiv(a: Int, b: Int): Int throws String do
    val (q, r) = try safeDiv(a, b)
    ret q + r

fn swapped(t: (<A>, <B>)): (<B>, <A>) = (t.second, t.first)

fn pair(a: <T>): (<T>, <T>) = (a, a)

fn findUser(id: Int): (Int, String)? do
    ret (id, "ann") if id == 1
    ret null

type Spot = distinct (Int, Int)

fn firstOf(t: (Int, Int) = (3, 4)): Int = t.first

fn sum(r: Range): Int = r.first + r.second

fn sum(t: (Int, Int, Int)): Int = t.first + t.second + t.third

fn status(ok: Bool): (Atom, Int) = if ok do (:ok, 1) else (:error, 0)

fn apply(f: (Int) => (Int, Int), x: Int): Int do
    val (a, b) = f(x)
    ret a * b

val origin = (0, 0)
var cursor: (Int, Long) = (1, 2)

fn main(): Int do
    // Literals, parts and types
    val t = (1, "one")
    io.log(t.first)
    io.log(t.second)
    val typed: (Long, Double) = (1, 2)
    io.log(typed.first)
    val nested = (1, (2.5, "deep"))
    io.log(nested.second.second)
    val long = (
        10,
        20,
        30
    )
    io.log(long.third)
    io.log(sizeof (Int, Int))

    // Destructuring
    val (q, r) = divmod(17, 5)
    io.log(q)
    io.log(r)
    var (a, b) = (1, 2)
    a = a + 10
    io.log(a)
    val (x, (y, z)) = nested
    io.log(x)
    io.log(y)
    io.log(z)
    val (_, onlySecond) = t
    io.log(onlySecond)
    val (p1, p2): (Long, Long) = (5, 6)
    io.log(p1 + p2)
    val (w1, w2) = wide()
    io.log(w2)

    // Assignment
    (a, b) = (b, a)
    io.log(a)
    io.log(b)
    var lo: Short = 0
    var hi: Short = 0
    (lo, hi) = (3, 4)
    io.log(lo + hi)
    var pt = Point { 1, 2 }
    var values = [1, 2, 3]
    (pt.x, values[0], _) = (7, 8, 9)
    io.log(pt.x)
    io.log(values[0])
    var maybe: String? = "set"
    var n = 0
    (maybe, n) = (null, 1)
    io.log(maybe == null)

    // Parts are changed through `var`s and pointers
    var m = (1, 2)
    m.first = 5
    io.log(m.first)
    val ptr = addrof m
    ptr.second = 6
    io.log(m.second)
    val heap = new (1, "heap")
    io.log(heap.second)

    // Comparing
    io.log((1, 2) == (1, 2))
    io.log((1, "a") != (1, "b"))
    io.log(divmod(17, 5) == (3, 2))
    io.log(((1, 2), "x") == ((1, 3), "x"))

    // Arrays and loops
    val entries = [(1, "one"), (2, "two"), (3, "three")]
    for (k, v) in entries do
        io.log(k)
        io.log(v)
    for i, (k, v) in entries where k > 1 do
        io.log(i)
    val spots: (Int, (Int, Int))[] = [(1, (2, 3))]
    for (k, (u, v)) in spots do
        io.log(k + u + v)

    // Errors, null, generics, unions
    io.log(tryDiv(7, 2) ?? -1)
    io.log(tryDiv(7, 0) ?? -1)
    val (sv, sk) = swapped((1, "s"))
    io.log(sv)
    io.log(sk)
    io.log(pair(4).second)
    val user = findUser(1)
    if user != null do
        val (id, name) = user
        io.log(id)
        io.log(name)
    val (fid, fname) = findUser(2) ?? (0, "nobody")
    io.log(fname)
    io.log(sum((1, 2)))
    io.log(sum((1, 2, 3)))
    val (code, _) = status(true)
    io.log(code == :ok)
    val mixed: Int | (Int, Int) = (1, 2)
    if mixed is (Int, Int) do
        io.log(mixed.second)
    val whenPair = when q do
        3 do (1, 2)
        else do (3, 4)
    io.log(whenPair.first)

    // Records, globals, clone, functions
    val seg = Segment { (Point { 1, 2 }, Point { 3, 4 }), "s" }
    val (start, end) = seg.ends
    io.log(start.x + end.y)
    io.log(origin.first)
    io.log(cursor.second)
    val copy = clone(("x", [1, 2]))
    io.log(copy.second[1])
    io.log(apply(fn (v: Int) = (v, v + 1), 2))

    // Tuples that read alike in C stay apart
    val left = new (1, (2, 3))
    val right = new ((1, 2), 3)
    io.log(left.second.second)
    io.log(right.first.first)

    // Comparing what needs converting, `distinct` tuples, and a tuple as a default
    val big: (Long, Long) = (1, 2)
    io.log(big == (1, 2))
    val spot = (1, 2) as Spot
    val (sx, sy) = spot
    io.log(sx + sy)
    io.log(spot == spot)
    io.log(firstOf())
    io.log((1, 2) |> sum)
    ret 0
