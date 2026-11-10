// `ref rec`: the name means a pointer to the record, which `new` makes in a pool.
use "core:io.ec"

mut ref rec Counter as
    name: String
    count: Int = 0

fn inc(c: Counter) do
    c.count += 1

// A `ref rec` can refer to itself: `next: Node?` is a pointer.
ref rec Node as
    value: Int
    next: Node?

fn push(list: Node?, value: Int): Node = new Node { value, list }

fn sum(list: Node?): Int do
    var total = 0
    var at = list
    while at != null do
        total += at.value
        at = at.next
    ret total

ref rec Animal as
    name: String

ref rec Cat extends Animal as
    lives: Int

fn describe(a: Animal): String = if a is Cat do "${a.name} the cat, ${a.lives} lives" else a.name

mut ref rec Box<T> as
    value: <T>

fn boxed(v: <T>): Box<T> = new Box<T> { v }

// `mut` on a reference changes which record the caller's variable refers to.
fn replace(mut c: Counter, name: String) do
    c = new Counter { name }

fn main() do
    val c = new Counter { "clicks" }
    c.inc()
    inc(c)
    // Two names, one record: `==` compares references.
    val same = c
    same.inc()
    io.log("${c.name}: ${c.count} ${c == same}")
    io.log("${c}")

    var list: Node? = null
    for i in 1..=4 do
        list = push(list, i)
    io.log("sum ${sum(list)}")

    val pet: Animal = new Cat { "Tom", 9 }
    io.log(describe(pet))
    io.log(describe(new Animal { "Rex" }))

    val b = boxed(42)
    b.value += 1
    io.log("${b.value}")

    var d = c
    replace(mut d, "other")
    io.log("${d.name} ${c.name}")

    val copy = clone(c)
    copy.inc()
    io.log("${copy.count} ${c.count} ${copy == c}")

    // `@c` is the record itself: this overwrites it, for everyone that refers to it.
    @c = Counter { "reset" }
    io.log("${same}")
    val empty = new Counter
    io.log("${empty.count}")
    val slots = new (Counter?)[2]
    slots[0] = c
    io.log("${slots[0] != null} ${slots[1] == null}")
