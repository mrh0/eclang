use "core:io.ec"

// Literals, the calls in an assignment and the initialisers of globals run left to right, whatever C would do with them.

var n: Int = 0

fn bump(): Int do
    n += 1
    ret n

fn add(a: Int, b: Int): Int = a * 10 + b

fn fails(v: Int): Int throws String do
    n += 1
    ret n

val g1: Int = add(bump(), bump())
val g2 = [bump(), bump()]
val g3 = (bump(), bump())

rec Point as
    x: Int
    y: Int

rec Later as
    a: Int
    b: Int = bump()

rec Holder as
    p: Point
    arr: Int[2]

val g4 = Point { y = bump(), x = bump() }
val g5: Int[2] = [bump(), bump()]

fn makePoint(): Point = Point { bump(), bump() }

fn ptr(p: @Int): @Int do
    n += 1
    ret p

fn tryArray(): Int[] throws String do
    val a = [bump(), try fails(1), bump()]
    ret clone(a)

fn trySet(a: Int[]): Int throws String do
    a[bump()] = try fails(1)
    ret 0

fn main() do
    io.log("${g1} ${g2[0]} ${g2[1]} ${g3.first} ${g3.second} ${g4.y} ${g4.x} ${g5[0]} ${g5[1]}")
    n = 0
    val a = [bump(), bump(), bump()]
    io.log("${a[0]} ${a[1]} ${a[2]}")
    val t = (bump(), bump())
    io.log("${t.first} ${t.second}")
    val p = Point { y = bump(), x = bump() }
    io.log("${p.y} ${p.x}")
    val l = Later { bump() }
    io.log("${l.a} ${l.b}")
    val q = Point { ...makePoint(), y = bump() }
    io.log("${q.x} ${q.y}")
    var r = Point { 1, 2 }
    val u = Point { ...r, y = bump() }
    io.log("${u.x} ${u.y}")
    val f: Int[2] = [bump(), bump()]
    io.log("${f[0]} ${f[1]}")
    val h = Holder { Point { bump(), bump() }, [bump(), bump()] }
    io.log("${h.p.x} ${h.p.y} ${h.arr[0]} ${h.arr[1]}")
    val nest = [[bump(), bump()], [bump()]]
    io.log("${nest[0][0]} ${nest[0][1]} ${nest[1][0]}")
    // After something else ran in the statement.
    val pp = add(bump(), (addrof Point { bump(), bump() }).y)
    io.log("${pp}")
    val np = new Point { bump(), bump() }
    io.log("${np.x} ${np.y}")
    io.log("${Point { bump(), bump() }.y}")
    if n > 0 and [bump(), bump()][1] > 0 do
        io.log("cond ${n}")
    for v in [bump(), bump()] do
        io.log("${v}")

    n = 0
    var arr: Int[] = new Int[8]
    arr[bump()] = bump()
    io.log("${arr[1]} ${arr[2]}")
    var x: Int = 5
    @ptr(addrof x) = bump()
    io.log("${x} ${n}")
    arr[bump()] += bump()
    io.log("${arr[4]} ${arr[5]}")
    match1(arr)
    n = 0
    val e = tryArray() ?? [0, 0, 0]
    io.log("${e[0]} ${e[1]} ${e[2]}")

fn match1(a: Int[]) do
    n = 2
    val r = trySet(a) ?? -1
    io.log("${a[3]} ${n} ${r}")
