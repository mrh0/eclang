use "core:io.ec"
use "core:array.ec"
use "core:math.ec"

rec Person as
    name: String
    age: Int

fn main(): Int do
    val numbers = clone([5, 3, 8, 1])
    io.log(numbers.contains(8))
    io.log(numbers.indexOf(1) ?? 99)
    io.log(numbers.count(3))
    io.log(numbers.find(fn (x) = x > 5) ?? 0)
    io.log(numbers.findIndex(fn (x) = x > 100) == null)
    io.log(numbers.any(fn (x) = x == 3))
    io.log(numbers.all(fn (x) = x > 1))

    val squares = numbers.map(fn (x) = x * x)
    io.log(squares[0])
    val texts = numbers.map(fn (x) = "#${x}")
    io.log(texts[3])
    val odd = numbers.filter(fn (x) = x % 2 == 1)
    io.log(odd.len)
    io.log(numbers.reduce(0, fn (sum, x) = sum + x))
    io.log(numbers.reduce(0.5, fn (sum, x) = sum + (x as Double)))

    numbers.sort()
    io.log(numbers.isSorted())
    io.log(numbers[0])
    io.log(numbers.binarySearch(5) ?? 99)
    io.log(numbers.binarySearch(6) == null)
    numbers.reverse()
    io.log(numbers[0])

    val words = clone(["pear", "apple", "fig"])
    words.sort()
    io.log(words[0])
    io.log(words.binarySearch("pear") ?? 99)

    // A long array, against a check of the order; the same seed gives the same array.
    var rng = math.random(42)
    val many = new Int[10000]
    for i in 0..<many.len do
        many[i] = math.nextInt(addrof(rng), 0, 1000)
    many.sort()
    io.log(many.isSorted())

    // The sort is stable: people of the same age stay in order.
    val people = clone([Person { "a", 30 }, Person { "b", 20 }, Person { "c", 30 }, Person { "d", 20 }])
    people.sortBy(fn (x, y) = x.age < y.age)
    for p in people do
        io.print(p.name)
    io.print("\n")

    val pair = joined(clone([1, 2]), clone([3]))
    io.log(pair.len)
    io.log(clone([1, 2, 1, 3, 2]).distinct().len)
    io.log(pair.take(2).len)
    io.log(pair.drop(2)[0])
    io.log(pair.drop(9).len)
    val copy = copyOf(pair)
    copy.fill(7)
    io.log(pair[0])
    io.log(copy[2])
    copy[0] = 1
    copy.swap(0, 2)
    io.log(copy[2])
    ret 0
