use "core:io.ec"
use "core:array.ec"
rec Person as
    name: String
    age: Int
fn main(): Int do
    val xs = clone([1, 2, 3, 4, 5])
    val z = xs.zip(["a", "b", "c"])
    io.log(z.len)
    for (n, s) in z do
        pool do
            io.log("${n}${s}")
    for (i, v) in ["x", "y"].enumerate() do
        pool do
            io.log("${i}=${v}")
    io.log(xs.flatMap(fn (x) = filled(x as Size, x)).len)
    val nested = [[1, 2], [3]]
    io.log(nested.flatten().len)
    val (ev, od) = xs.partition(fn (x) = x % 2 == 0)
    io.log("${ev.len} ${od.len}")
    for c in xs.chunked(2) do
        io.log(c.len)
    for w in xs.windowed(3) do
        io.log(w[0])
    io.log(xs.windowed(2, 2).len)
    io.log(xs.windowed(9).len)
    val s = xs.scan(0, fn (sum, x) = sum + x)
    io.log("${s.len} ${s[5]}")
    io.log(xs.takeWhile(fn (x) = x < 3).len)
    io.log(xs.dropWhile(fn (x) = x < 3)[0])
    io.log(xs.dropWhile(fn (x) = true).len)
    val people = clone([Person { "Ada", 36 }, Person { "Bo", 20 }, Person { "Cy", 50 }])
    io.log(people.sumOf(fn (p) = p.age))
    io.log((people.minBy(fn (p) = p.age) ?? people[0]).name)
    io.log((people.maxBy(fn (p) = p.name) ?? people[0]).name)
    io.log(xs.fold(0, fn (a, x) = a + x))
    ret 0
