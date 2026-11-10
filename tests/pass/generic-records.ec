use "core:io.ec"
use "core:string.ec"

rec Box<T> as
    value: T

rec Pair<A, B> as
    first: A
    second: B

rec Node<T> as
    value: T
    next: @Node<T>?

mut rec Stack<T> as
    items: T[]
    count: Size = 0

rec Point as
    x: Int
    y: Int

/** Reads the box, whatever it holds. */
fn unbox(b: Box<T>): <T> = b.value

fn swapped(p: Pair<A, B>): Pair<B, A> = Pair<B, A> { p.second, p.first }

fn push(list: @Node<T>?, value: <T>): @Node<T> = new Node<T> { value, list }

fn length(list: @Node<T>?): Int do
    var n = 0
    var at = list
    while at != null do
        n += 1
        at = at.next
    ret n

/** `T` is only in the result: the expected type decides it. */
fn stack(capacity: Size): @Stack<T> = new Stack<T> { unsafe new (<T>)[capacity] }

fn add(s: @Stack<T>, value: <T>) do
    s.items[s.count] = value
    s.count += 1

fn top(s: @Stack<T>): <T>? do
    ret null if s.count == 0
    ret s.items[s.count - 1]

fn stackOf(...values: <T>): @Stack<T> do
    val s: @Stack<T> = stack(values.len)
    for v in values do
        add(s, v)
    ret s

fn mapped(values: <T>[], f: (<T>) => <U>): <U>[] do
    val out = unsafe new (<U>)[values.len]
    for i, v in values do
        out[i] = f(v)
    ret out

fn orElse(value: <T>?, other: <T>): <T> = value ?? other

fn main(): Int do
    val b = Box<Int> { 42 }
    io.log(b.value)
    io.log(unbox(Box<String> { "text" }))
    val typed: Box<Double> = Box { 2.5 }
    io.log(typed.value)

    val p = Pair<Int, String> { 1, "one" }
    val q = swapped(p)
    io.log(q.first)
    io.log(q.second)

    // Nested type arguments end in `>>`, which is also a shift.
    val nested = Box<Box<Int>> { Box<Int> { 7 } }
    io.log(nested.value.value)
    io.log(256 >> 2)
    var bits = 64
    bits >>= 1
    io.log(bits)
    io.log(3 > 2)

    var list: @Node<Int>? = null
    list = push(list, 1)
    list = push(list, 2)
    io.log(length(list))

    val ints: @Stack<Int> = stack(4)
    ints.add(5)
    ints.add(6)
    io.log(ints.top() ?? 0)
    val words: @Stack<String> = stack(2)
    io.log(words.top() ?? "empty")

    // A pointer has no empty value, but `new (<T>)[n]` is only storage.
    val points: @Stack<@Point> = stack(1)
    points.add(new Point { 3, 4 })
    io.log(points.items[0].y)

    val made = stackOf("a", "b", "c")
    io.log(made.count)
    io.log(made.top() ?? "")

    val numbers = [1, 2, 3]
    val doubled = mapped(numbers, fn (x) = x * 2)
    io.log(doubled[2])
    val names = numbers.mapped(fn (x) = "n${x}")
    io.log(names[0])

    var missing: Int? = null
    io.log(orElse(missing, 9))
    io.log(orElse(3 as Int?, 9))

    val copy = clone(Box<String> { "cloned" })
    io.log(copy.value)
    io.log(Box<Int> { 1 } == Box<Int> { 1 })
    ret 0
