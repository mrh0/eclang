use "core:io.ec"
use "core:structures.ec"

// core:structures.ec: SortedMap, SortedSet, BitSet and LruCache.

rec Point as
    x: Int
    y: Int

var seed: ULong = 12345

fn random(n: Size): Size do
    seed = seed * 6364136223846793005ul + 1442695040888963407ul
    ret ((seed >> 33) as Size) % n

/** Checks the map against `present` (which keys it should have, each with the value key * 10). */
fn check(m: SortedMap<Int, Int>, present: Bool[]): Bool do
    var count: Size = 0
    var previous = -1
    for k, v in m do
        if k <= previous or v != k * 10 or not present[k as Size] do
            io.log("bad entry ${k}=${v} after ${previous}")
            ret false
        previous = k
        count += 1
    var expected: Size = 0
    for p in present where p do
        expected += 1
    if count != expected or m.size() != expected do
        io.log("bad count ${count} ${m.size()} ${expected}")
        ret false
    // floor and ceiling of every key, against a scan of `present`
    for q in 0..<present.len do
        var below = -1
        for i in 0..<q + 1 where present[i] do
            below = i as Int
        var above = -1
        var i = q
        while i < present.len do
            if present[i] do
                above = i as Int
                break
            i += 1
        var f = -1
        if val e = m.floor(q as Int) do
            f = e.first
        var c = -1
        if val e = m.ceiling(q as Int) do
            c = e.first
        if f != below or c != above do
            io.log("bad floor/ceiling of ${q}: ${f} ${c}, expected ${below} ${above}")
            ret false
    ret true

fn main(): Int do
    // Random puts and removes, checked against an array of what is there.
    val m: SortedMap<Int, Int> = sortedMap()
    val present = new Bool[300]
    var ok = true
    for round in 0..<4000 do
        val k = random(present.len)
        if random(3) == 0 do
            if m.remove(k as Int) != present[k] do
                ok = false
            present[k] = false
        else do
            m.put(k as Int, k as Int * 10)
            present[k] = true
        if round % 400 == 0 and not check(m, present) do
            ok = false
    io.log(ok and check(m, present))
    while m.size() > 0 do
        m.popLast()
    io.log(m.isEmpty())
    io.log(m.first() == null)

    val words: SortedMap<String, Int> = sortedMap()
    for w in ["pear", "apple", "fig", "banana", "cherry"] do
        words.put(w, w.len as Int)
    for w, n in words do
        io.log("${w} ${n}")
    io.log(words.get("fig") ?? 0)
    io.log(words.get("kiwi") ?? -1)
    for (w, _) in words.range("b", "d") do
        io.log("range ${w}")
    for (w, _) in words.rangeFrom("d") do
        io.log("from ${w}")
    for (w, _) in words.rangeUntil("c") do
        io.log("until ${w}")
    if val e = words.lower("banana") do
        io.log("lower ${e.first}")
    if val e = words.higher("banana") do
        io.log("higher ${e.first}")
    io.log(words.higher("pear") == null)
    if val e = words.popFirst() do
        io.log("popped ${e.first}")
    io.log(words.keys().len)
    io.log(words.fold(0, fn (sum, _, n) = sum + n))
    // removing during the loop
    for w, _ in words do
        words.remove(w)
    io.log(words.size())

    // A custom order: by x, then y, largest first.
    val points: SortedMap<Point, String> = sortedMap(fn (a: Point, b: Point) = a.x > b.x or (a.x == b.x and a.y > b.y))
    points.put(Point { 1, 2 }, "a")
    points.put(Point { 3, 0 }, "b")
    points.put(Point { 1, 5 }, "c")
    points.put(Point { 1, 2 }, "d")
    for p, name in points do
        io.log("${p.x},${p.y} ${name}")

    val s: SortedSet<Int> = sortedSetOf(5, 1, 9, 3, 7, 3)
    io.log(s.size())
    io.log(s.first() ?? 0)
    io.log(s.last() ?? 0)
    io.log(s.floor(6) ?? 0)
    io.log(s.ceiling(6) ?? 0)
    io.log(s.lower(1) == null)
    io.log(s.add(4))
    io.log(s.add(4))
    io.log(s.remove(9))
    for x in s.range(3, 7) do
        io.log("set ${x}")
    io.log(s.popFirst() ?? 0)
    var total = 0
    for x in s do
        total += x
    io.log(total)
    io.log(s.toArray().len)

    val b = bitSet()
    io.log(b.add(3))
    io.log(b.add(3))
    b.add(64)
    b.add(1000)
    io.log(b.has(64))
    io.log(b.has(65))
    io.log(b.has(100000))
    io.log(b.size())
    for i in b do
        io.log("bit ${i}")
    io.log(b.remove(64))
    io.log(b.remove(64))
    io.log(b.nextSet(4) ?? 0)
    val c = bitSetOf(1, 3, 2000)
    io.log(union(b, c).toArray().len)
    io.log(intersection(b, c).toArray()[0])
    io.log(difference(b, c).toArray()[0])
    b.clear()
    io.log(b.isEmpty())
    io.log(bitSet(0).add(0))

    val cache: LruCache<String, Int> = lruCache(2)
    io.log(cache.put("a", 1) == null)
    cache.put("b", 2)
    io.log(cache.get("a") ?? 0)
    if val dropped = cache.put("c", 3) do
        io.log("dropped ${dropped.first}=${dropped.second}")
    io.log(cache.has("b"))
    io.log(cache.peek("a") ?? 0)
    cache.put("d", 4)                         // drops a: peek isn't a use
    io.log(cache.has("a"))
    for k, v in cache do
        io.log("cache ${k}=${v}")
    io.log(cache.remove("d"))
    cache.put("e", 5)
    io.log(cache.size())
    cache.put("c", 30)
    cache.put("f", 6)                         // drops e
    for k, v in cache do
        io.log("cache ${k}=${v}")
    cache.clear()
    io.log(cache.isEmpty())
    cache.put("g", 7)
    io.log(cache.get("g") ?? 0)
    ret 0
