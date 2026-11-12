// `copy(x)`: a copy one level deep. What x points to is copied; what that points to is shared.
use "core:io.ec"
use "core:grid.ec"

mut rec Inner as
    a: Int

mut rec Pair as
    n: Int
    inner: @Inner

mut ref rec Counter as
    name: String
    count: Int = 0

mut rec Animal as
    legs: Int

mut rec Dog extends Animal as
    name: String

// Only for Doors: other values still get the builtin `copy`.
mut rec Door as
    open: Bool

fn copy(d: Door): String = "not a copy"

fn main() do
    // A pointer: a new record, whose pointer fields point where the original's do.
    val p = new Pair { 1, new Inner { 2 } }
    val q = copy(p)
    q.n = 10
    q.inner.a = 20
    io.log("${p.n} ${q.n} ${p.inner.a} ${p.inner == q.inner} ${p == q}")

    // A `ref rec`, as a method.
    val c = new Counter { "c" }
    c.count = 1
    val d = c.copy()
    d.count = 5
    io.log("${c.count} ${d.count} ${c == d}")

    // A pointer to a child record copies the whole child.
    val a: @Animal = new Dog { legs = 4, name = "Rex" }
    val b = copy(a)
    b.legs = 3
    io.log("${a.legs} ${b.legs} ${b is @Dog}")
    if b is @Dog do
        io.log(b.name)

    // An array: new elements, which are the same pointers.
    val xs = [new Inner { 1 }, new Inner { 2 }]
    val ys = copy(xs)
    ys[0] = new Inner { 9 }
    ys[1].a = 7
    io.log("${xs[0].a} ${ys[0].a} ${xs[1].a}")

    // A String, and a copy into another pool.
    val other = new Pool
    val s = copy("text", other)
    io.log(s)

    // A nullable pointer.
    val none: @Inner? = null
    io.log("${copy(none) == null}")

    // A value: the value itself.
    val pair = Pair { 3, new Inner { 4 } }
    val pair2 = pair.copy()
    io.log("${pair2.n} ${pair2.inner == pair.inner}")

    // A function of the same name takes over only for what it takes.
    io.log(copy(Door { true }))
    val g = grid(2, 2, 1)
    val g2 = g.copy()
    io.log("${g2.width}")

    // clone and hash too, with such functions around.
    io.log("${clone(p).inner == p.inner} ${hash(3) == hash(3)}")
