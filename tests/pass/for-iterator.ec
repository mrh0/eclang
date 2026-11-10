use "core:io.ec"
use "core:structures.ec"
use "lib/countdown.ec"

mut rec Count as
    at: Int
    end: Int

fn next(c: @Count): Int? do
    ret null if c.at >= c.end
    c.at += 1
    ret c.at - 1

rec Words as
    list: String[]

fn iter(w: @Words): String[] = w.list

rec Letters as
    text: String

fn iter(l: Letters): String = l.text

rec Grid as
    size: Int

mut rec Cells as
    at: Int
    size: Int

fn iter(g: Grid): Cells = Cells { 0, g.size }
fn pairs(g: Grid): Cells = Cells { 0, g.size }

fn next(c: @Cells): (Int, Int)? do
    ret null if c.at >= c.size
    c.at += 1
    ret (c.at, c.at * c.at)

mut rec Box<T> as
    value: T
    full: Bool

fn next(b: @Box<T>): <T>? do
    ret null if not b.full
    b.full = false
    ret b.value

/** Runs per element type, through the generic copies. */
fn total(values: <T>): Int do
    var sum = 0
    for v in values do
        sum += v
    ret sum

fn firstEven(c: @Count): Int? do
    for x in c where x % 2 == 0 do
        ret x
    ret null

fn deferred() do
    for i, x in Count { 0, 2 } do
        defer io.log("end ${i}")
        continue if x == 0
        io.log("body ${x}")

fn main() do
    for x in Count { 1, 4 } do
        io.log(x)

    // A value is copied, so the variable stays where it was; a pointer advances it.
    var c = Count { 0, 5 }
    for x in c do
        break if x == 1
    io.log(c.at)
    for x in addrof c do
        break if x == 1
    io.log(c.at)
    for x in addrof c do
        io.log(x)

    for i, x in Count { 10, 13 } do
        continue if i == 1
        io.log("${i}: ${x}")

    for x in Count { 0, 10 } where x % 4 == 0 do
        io.log(x)

    for x in Count { 0, 3 } do
        break if x == 7
    else do
        io.log("else after the end")
    for x in Count { 0, 3 } do
        break if x == 1
    else do
        io.log("not printed")

    outer: for a in Count { 0, 3 } do
        for b in Count { 0, 3 } do
            continue outer if b == 1
            break outer if a == 2
            io.log("${a} ${b}")

    deferred()

    io.log(firstEven(addrof Count { 1, 9 }) ?? -1)
    val empty = Count { 3, 3 }
    for x in empty do
        io.log("never")
    else do
        io.log("empty")

    // `iter`: an array, a String, a pointer parameter on a value.
    for w in Words { ["a", "b"] } do
        io.log(w)
    for i, ch in Letters { "hi" } do
        io.log("${i} ${ch}")

    // `pairs` names both values, a pattern always counts.
    for a, b in Grid { 2 } do
        io.log("${a} ${b}")
    for (a, b) in Grid { 2 } do
        io.log("${a} ${b}")
    for i, (a, _) in Grid { 2 } do
        io.log("${i} ${a}")
    for _, b in Grid { 2 } do
        io.log(b)

    // Generic iterators and generic functions.
    for s in Box<String> { "boxed", true } do
        io.log(s)
    io.log(total(Count { 1, 4 }))
    io.log(total(listOf(4, 5)))
    io.log(total([7, 8]))

    // From another module.
    for n in countdown.from(2) do
        io.log(n)
    for n in countdown.shelf(1) do
        io.log(n)

    // The structures.
    val names = listOf("Ada", "Bob")
    for name in names do
        io.log(name)
    for i, name in names do
        io.log("${i} ${name}")
    val maybe: List<Int?> = list()
    maybe.push(1)
    maybe.push(null)
    maybe.push(3)
    for m in maybe where m != null do
        io.log(m + 1)
    for i, pair in listOf((1, 2), (3, 4)) do
        io.log("${i} ${pair}")

    val ages: Map<String, Int> = map()
    ages.put("Ada", 36)
    ages.put("Bob", 41)
    ages.put("Cid", 7)
    ages.remove("Bob")
    for name, age in ages do
        io.log("${name}: ${age}")
    for (name, age) in ages do
        io.log("${name} is ${age}")
    for i, (name, _) in ages do
        io.log("${i} ${name}")
    for entry in ages do
        io.log("${entry}")
    for name, age in ages do
        ages.remove(name)
    io.log(ages.size())

    val seen = setOf(3, 1, 3, 2)
    seen.remove(1)
    for v in seen do
        io.log(v)
    for i, v in seen do
        io.log("${i} ${v}")

    val queue: Deque<Int> = deque()
    queue.pushBack(2)
    queue.pushBack(3)
    queue.pushFront(1)
    for x in queue do
        io.log(x)
