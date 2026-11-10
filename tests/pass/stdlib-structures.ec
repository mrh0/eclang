use "core:io.ec"
use "core:structures.ec"
use "core:array.ec"

rec Point as
    x: Int
    y: Int

/** Records can be keys: they only need a hash. */
fn hash(p: Point): ULong = hash((p.x * 31 + p.y) as Long)

fn main(): Int do
    val l: List<Int> = list(1)
    for i in 0..<100 do
        l.push(i * i)
    io.log(l.len)
    io.log(l.get(99))
    io.log(l.pop() ?? -1)
    l.insert(0, -5)
    io.log(l.first() ?? 0)
    io.log(l.removeAt(0))
    io.log(l.contains(81))
    io.log(l.indexOf(82) == null)
    l.set(1, 1000)
    io.log(l.items()[1])
    val few = listOf("x", "y")
    few.pushAll(["z"])
    io.log(join(few.toArray(), ","))
    val empty: List<String> = list()
    io.log(empty.pop() ?? "none")

    val ages: Map<String, Int> = map()
    ages.put("Ada", 36)
    ages.put("Alan", 41)
    ages.put("Ada", 37)
    io.log(ages.get("Ada") ?? 0)
    io.log(ages.get("Bob") ?? -1)
    io.log(ages.has("Alan"))
    io.log(ages.len)
    io.log(ages.remove("Alan"))
    io.log(ages.remove("Alan"))
    io.log(ages.has("Alan"))
    ages.put("Grace", 85)
    io.log(join(ages.keys(), ","))
    io.log(ages.values()[1])

    // Growth, and removed keys left in the table.
    val big: Map<Int, Int> = map()
    for i in 0..<5000 do
        big.put(i, i * 2)
    for i in 0..<5000 where i % 2 == 0 do
        big.remove(i)
    for i in 5000..<6000 do
        big.put(i, i)
    var hits = 0
    for i in 0..<6000 where big.has(i) do
        hits += 1
    io.log(hits)
    io.log(big.len)
    io.log(big.get(4999) ?? 0)
    io.log(big.get(4998) ?? -1)
    // Adding and removing the same key must not fill the table.
    val churn: Map<Int, Int> = map()
    for i in 0..<100000 do
        churn.put(i, i)
        churn.remove(i)
    io.log(churn.len)

    val grid: Map<Point, String> = map()
    grid.put(Point { 1, 2 }, "a")
    io.log(grid.get(Point { 1, 2 }) ?? "?")
    io.log(grid.get(Point { 2, 1 }) ?? "?")

    val seen: Set<String> = set()
    io.log(seen.add("a"))
    io.log(seen.add("a"))
    seen.add("b")
    io.log(seen.size())
    val a = setOf(1, 2, 3)
    val b = setOf(2, 3, 4)
    io.log(union(a, b).size())
    io.log(intersection(a, b).toArray()[0])
    io.log(difference(a, b).toArray()[0])

    val d: Deque<Int> = deque()
    for i in 0..<20 do
        d.pushBack(i)
    d.pushFront(-1)
    io.log(d.popFront() ?? 0)
    io.log(d.popFront() ?? 0)
    io.log(d.popBack() ?? 0)
    io.log(d.last() ?? 0)
    io.log(d.toArray()[0])
    io.log(d.size())

    val h: Heap<Int> = heap(fn (x: Int, y: Int) = x < y)
    for v in [5, 1, 4, 2, 3] do
        h.push(v)
    io.log(h.peek() ?? 0)
    while not h.isEmpty() do
        io.print("${h.pop() ?? 0} ")
    io.log("")

    // A structure in a pool of its own, and elements that are pointers.
    pool kept do
        val own: List<@Point> = list(kept, 1)
        for i in 0..<10 do
            own.push(new Point { i, i } in kept)
        io.log(own.get(9).y)
    ret 0
