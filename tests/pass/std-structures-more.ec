// RingBuffer, Counter, Grid and DisjointSet, and the additions to Heap, Deque and SortedMap.
use "core:io.ec"
use "core:structures.ec"
use "core:ringbuffer.ec"
use "core:counter.ec"
use "core:grid.ec"
use "core:disjointset.ec"
use "core:array.ec"

/** The elements in brackets, for the log. */
fn show(values: <T>[]): String do
    var text = "["
    for i, v in values do
        text = if i == 0 do "${text}${v}" else "${text}, ${v}"
    ret "${text}]"

fn main() do
    // RingBuffer
    val recent: RingBuffer<Int> = ringBuffer(3)
    io.log(recent.push(1) ?? -1)
    recent.push(2)
    recent.push(3)
    io.log(recent.isFull())
    io.log(recent.push(4) ?? -1)
    io.log(recent.push(5) ?? -1)
    var line = ""
    for v in recent do
        line = "${line}${v} "
    io.log(line)
    io.log("${recent.first() ?? 0} ${recent.last() ?? 0} ${recent.get(1)} ${recent.size()} ${recent.capacity()}")
    recent.set(0, 30)
    io.log(recent.popFront() ?? 0)
    io.log(recent.popBack() ?? 0)
    io.log(recent.fold(0, fn (a, b) = a + b))
    recent.clear()
    io.log(recent.isEmpty())
    io.log(recent.popFront() ?? -1)

    // Counter
    val letters: Counter<Char> = counter()
    for c in "mississippi" do
        letters.add(c)
    io.log("${letters.count('s')} ${letters.count('z')} ${letters.size()} ${letters.total()}")
    for (c, n) in letters.mostCommon(2) do
        io.log("${c}: ${n}")
    io.log(letters.subtract('i', 2))
    io.log(letters.subtract('m'))
    io.log(letters.has('m'))
    io.log(letters.total())
    io.log(letters.add('p', 10))
    io.log(letters.remove('p'))
    io.log(letters.total())
    val words = counterOf("a", "b", "a")
    words.addAll(["c", "b", "a"])
    for w, n in words do
        io.log("${w}=${n}")
    io.log(words.mostCommon().len)

    // Grid
    val board = grid(4, 3, '.')
    board.set(3, 2, '#')
    board.set(0, 0, '@')
    for y in 0..<board.height do
        var text = ""
        for c in board.row(y) do
            text = "${text}${c}"
        io.log(text)
    io.log(board.get(3, 2))
    io.log(board.contains(4, 0))
    io.log(board.contains(-1 as Long, 0 as Long))
    io.log(show(board.neighbors(0, 0)))
    io.log(show(board.neighbors(1, 1)))
    io.log(show(board.neighbors8(3, 2)))
    io.log("${board.find(fn (c) = c == '#') ?? (0 as Size, 0 as Size)}")
    io.log(board.count(fn (c) = c == '.'))
    val sums = gridOf(3, 2, fn (x, y) = x + y * 10)
    io.log(show(sums.column(1)))
    io.log(sums.fold(0 as Size, fn (a, b) = a + b))
    val doubled = sums.map(fn (v) = v * 2)
    io.log(doubled.get(2, 1))
    val copied = sums.copy()
    copied.fill(0)
    io.log("${sums.get(1, 1)} ${copied.get(1, 1)}")

    // DisjointSet
    val d = disjointSet(6)
    io.log(d.union(0, 1))
    io.log(d.union(1, 0))
    d.union(3, 4)
    d.union(4, 5)
    io.log("${d.groups()} ${d.connected(0, 1)} ${d.connected(1, 3)} ${d.groupSize(5)}")
    val seven = d.add()
    io.log("${seven} ${d.size()} ${d.groups()}")
    for g in d.toArray() do
        io.log(show(g))
    val many = disjointSet()
    for i in 0..<100 do
        many.add()
        if i > 0 do
            many.union((i - 1) as Size, i as Size)
    io.log("${many.groups()} ${many.groupSize(0)}")

    // Heap
    val h = heapOf(fn (a: Int, b: Int) = a < b, 5, 1, 4, 2, 3)
    io.log(show(h.sorted()))
    io.log(h.size())
    var sum = 0
    for v in h do
        sum += v
    io.log(sum)
    io.log(h.toArray().len)
    io.log(h.fold(0, fn (a, b) = a + b))
    val big = h.filter(fn (v) = v > 2)
    io.log(big.pop() ?? 0)
    io.log(h.map(fn (v) = v * 10).items().len)

    // Deque
    val q: Deque<Int> = deque()
    q.pushBack(2)
    q.pushFront(1)
    q.pushBack(3)
    q.set(2, 30)
    io.log("${q.get(0)} ${q.get(1)} ${q.get(2)}")

    // SortedMap
    val scores: SortedMap<String, Int> = sortedMap()
    scores.put("bob", 3)
    scores.put("ada", 5)
    scores.put("cy", 1)
    val joinedKeys = scores.map(fn (k, v) = "${k}${v}")
    io.log(show(joinedKeys.items()))
    val high = scores.filter(fn (k, v) = v > 1)
    io.log(show(high.keys()))
    val names = scores.mapValues(fn (v) = v * 100)
    for k, v in names do
        io.log("${k} ${v}")
    val backwards: SortedMap<Int, Int> = sortedMap(fn (a: Int, b: Int) = a > b)
    for i in 0..<5 do
        backwards.put(i, i)
    io.log(show(backwards.filter(fn (k, v) = k % 2 == 0).keys()))
