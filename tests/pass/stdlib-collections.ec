use "core:io.ec"
use "core:array.ec"
use "core:structures.ec"
rec Person as
    name: String
    age: Int
fn main(): Int do
    val l = listOf(1, 2, 3, 4)
    val doubled = l.map(fn (x) = x * 2)
    io.log(doubled.get(3))
    io.log(l.filter(fn (x) = x > 2).len)
    io.log(l.fold(0, fn (a, x) = a + x))
    io.log(l.map(fn (x) = "#${x}").get(0))

    val ages: Map<String, Int> = map()
    ages.put("Ada", 36)
    ages.put("Bo", 20)
    io.log(ages.update("Ada", fn (a) = a + 1))
    io.log(ages.update("Zed", fn (a) = a + 1))
    io.log(ages.get("Ada") ?? 0)
    io.log(ages.getOrPut("Cy", fn () = 50))
    io.log(ages.getOrPut("Cy", fn () = 99))
    val counts: Map<String, Int> = map()
    for w in ["a", "b", "a"] do
        counts.update(w, 0, fn (n) = n + 1)
    val groups: Map<String, List<Int>> = map()
    groups.getOrPut("a", fn () = list()).push(1)
    groups.getOrPut("a", fn () = list()).push(2)
    io.log((groups.get("a") ?? list()).size())
    io.log(counts.get("a") ?? 0)
    io.log(ages.mapValues(fn (a) = a * 2).get("Bo") ?? 0)
    io.log(ages.mapValues(fn (a) = "${a}").get("Bo") ?? "")
    io.log(ages.map(fn (k, v) = "${k}=${v}").get(1))
    io.log(ages.filter(fn (k, v) = v > 30).size())
    io.log(ages.fold(0, fn (acc, k, v) = acc + v))

    val s = setOf(1, 2, 3, 4)
    io.log(s.map(fn (x) = x % 2).size())
    io.log(s.filter(fn (x) = x > 1).size())
    io.log(s.fold(0, fn (a, x) = a + x))

    val d: Deque<Int> = deque()
    d.pushBack(2)
    d.pushFront(1)
    d.pushBack(3)
    io.log(d.map(fn (x) = x * 10).popBack() ?? 0)
    io.log(d.filter(fn (x) = x != 2).size())
    io.log(d.fold(0, fn (a, x) = a * 10 + x))

    val words = clone(["fig", "pear", "kiwi", "apple"])
    val byLen = words.groupBy(fn (w) = w.len)
    io.log((byLen.get(4) ?? list()).size())
    for n, group in byLen do
        pool do
            io.log("${n}: ${group.size()}")
    val people = clone([Person { "Ada", 36 }, Person { "Bo", 20 }])
    io.log(people.associate(fn (p) = (p.name, p.age)).get("Bo") ?? 0)
    io.log((people.associateBy(fn (p) = p.name).get("Ada") ?? people[1]).age)
    io.log(toMap([("a", 1), ("b", 2)]).get("b") ?? 0)
    ret 0
