use "core:io.ec"

/** Printed by the compiler. */
rec Point as
    x: Int
    y: Int

rec Person as
    name: String
    nick: CString
    initial: Char
    age: Int
    height: Double
    active: Bool
    status: Atom
    home: Point
    scores: Int[]
    pair: (Int, String)
    best: Int?
    next: @Person?

rec Pair2<T> as
    first: T
    second: T

// A `str` of its own, also inside other records.
rec Money as
    cents: Int

fn str(m: Money): String = "\$${m.cents / 100}.${m.cents % 100:02}"

rec Wallet as
    owner: String
    balance: Money
    history: Money[]

// A `str` that takes a pointer also prints the record itself.
rec Tag as
    label: String

fn str(t: @Tag): String = "#${t.label}"

// Generic `str`.
rec Box<T> as
    value: T

fn str(b: Box<T>): String = "[box ${b.value}]"

// Printing through pointers, and a pointer that comes back.
rec Node as
    value: Int
    next: @Node?

rec Animal as
    name: String

rec Cat extends Animal as
    lives: Int

rec Dog extends Animal as
    good: Bool

fn str(d: @Dog): String = "the dog ${d.name}"

type Id = distinct Point

// This `str` is only reached through a field, from another function: it must stay in the C file.
rec Inner as
    n: Int

fn str(i: Inner): String = "inner ${i.n}"

rec Outer as
    inner: Inner

fn describe(o: Outer): String = "${o}"

fn main(): Int do
    val p = Point { 1, 2 }
    io.log("${p}")
    io.log("p is ${p}, and again ${p}!")
    io.log("${p=}")
    io.log("[${p:>24}]")
    val t = (1, "two", 'c')
    io.log("${t}")

    val q = new Point { 3, 4 }
    io.log("${q}")

    val ann = new Person { "Ann", "annie"c, 'a', 30, 1.5, true, :ok, p, clone([1, 2]), (1, "x"), null, null }
    val bob = new Person { "Bob", "b"c, 'b', 31, 2.0, false, :no, Point { 3, 4 }, clone([3]), (2, "y"), 7, ann }
    io.log("${bob}")

    val pair = Pair2<Int> { 1, 2 }
    io.log("${pair}")
    val money = Money { 1205 }
    io.log("${money}")
    val wallet = Wallet { "w", Money { 150 }, clone([Money { 1 }, Money { 200 }]) }
    io.log("${wallet}")
    val tag = Tag { "red" }
    io.log("${tag}")
    val tagged = new Tag { "blue" }
    io.log("${tagged}")
    val box = Box<Int> { 5 }
    io.log("${box}")
    val text = Box<String> { "s" }
    io.log("${text}")

    val a = new Node { 1, null }
    val b = new Node { 2, a }
    a.next = b
    io.log("${a}")

    val animal: @Animal = new Cat { "Tom", 9 }
    io.log("${animal}")
    val dog: @Animal = new Dog { "Rex", true }
    io.log("${dog}")
    val thing = new Animal { "Thing" }
    io.log("${thing}")

    val id = p as Id
    io.log("${id}")
    io.log(describe(Outer { Inner { 1 } }))

    // Printing stops at a depth, so a long chain can't exhaust the stack.
    var chain = new Node { 0, null }
    for i in 1..<40 do
        chain = new Node { i, chain }
    val long = "${chain}"
    io.log(long.len)
    ret 0
