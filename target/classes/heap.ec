/**
 * Heap<T>, a priority queue.
 *
 *     use "core:heap.ec"
 *     val tasks: Heap<Int> = heap(fn (a: Int, b: Int) = a < b)
 *     tasks.push(5)
 *     io.log(tasks.pop() ?? 0)                        // 5
 *
 * The functions are top-level, so they can be called on a structure. Its memory is in the pool that was current when it
 * was made (or the pool given), and it grows there. What it holds isn't copied: Strings and pointers put in it must live at
 * least as long as it does, which the compiler checks. core:structures.ec has every structure at once.
 */
use "core:errors.ec"
use "core:string.ec"
use "core:list.ec"
use "core:array.ec"

/**
 * A priority queue: {@link pop} always gives the element that comes first, and adding and taking are fast.
 *
 *     val tasks: Heap<Int> = heap(fn (a: Int, b: Int) = a < b)
 *     tasks.push(5)
 *     tasks.push(1)
 *     io.log(tasks.pop() ?? 0)                        // 1
 *
 * @param <T> the type of the elements
 */
mut ref rec Heap<T> as
    /** The binary heap, and room for more. */
    buffer: T[]
    /** How many elements it holds. */
    len: Size
    /** Whether its first argument comes before its second. */
    less: (T, T) => Bool
    /** The pool its memory is in. */
    mem: Pool

/**
 * A new empty {@link Heap}, in the current pool.
 * @param less whether its first argument comes before its second
 */
fn heap(less: (<T>, <T>) => Bool): Heap<T> = new Heap<T> { unsafe new (<T>)[8], 0, less, pool }

/**
 * A new {@link Heap} of the `values`, in the current pool: `heapOf(fn (a: Int, b: Int) = a < b, 5, 1, 3)`.
 * @param less whether its first argument comes before its second
 */
fn heapOf(less: (<T>, <T>) => Bool, ...values: <T>): Heap<T> do
    val h = heap(less)
    for v in values do
        push(h, v)
    ret h

/** Adds `value`. */
fn push(h: Heap<T>, value: <T>) do
    if h.len == h.buffer.len do
        val grown = unsafe new (<T>)[h.buffer.len * 2] in h.mem
        for i in 0..<h.len do
            grown[i] = h.buffer[i]
        h.buffer = grown
    var i = h.len
    h.len += 1
    while i > 0 do
        val parent = (i - 1) / 2
        break if not h.less(value, h.buffer[parent])
        h.buffer[i] = h.buffer[parent]
        i = parent
    h.buffer[i] = value

/** The element that comes first, without taking it away, or null when the heap is empty. */
fn peek(h: Heap<T>): <T>? do
    ret null if h.len == 0
    ret h.buffer[0]

/** Takes away the element that comes first. @return the element, or null when the heap is empty */
fn pop(h: Heap<T>): <T>? do
    ret null if h.len == 0
    val top = h.buffer[0]
    h.len -= 1
    val moved = h.buffer[h.len]
    var i: Size = 0
    while true do
        var child = i * 2 + 1
        break if child >= h.len
        if child + 1 < h.len and h.less(h.buffer[child + 1], h.buffer[child]) do
            child += 1
        break if not h.less(h.buffer[child], moved)
        h.buffer[i] = h.buffer[child]
        i = child
    h.buffer[i] = moved
    ret top

/** Takes every element away, and keeps the room they took. */
fn clear(h: Heap<T>) do
    h.len = 0

/** Whether the heap has no elements. */
fn isEmpty(h: Heap<T>): Bool = h.len == 0

/** How many elements it holds. */
fn size(h: Heap<T>): Size = h.len

/**
 * What `for x in h do` loops over: the elements, in no particular order (only the first is the one {@link peek} gives).
 * Adding or taking during the loop moves them. See {@link sorted} for the order {@link pop} takes them in.
 */
fn iter(h: Heap<T>): <T>[] = h.buffer[0..<h.len]

/** A new array of the elements, in no particular order, in the current pool. See {@link sorted}. */
fn toArray(h: Heap<T>): <T>[] do
    val out = unsafe new (<T>)[h.len]
    for i in 0..<h.len do
        out[i] = h.buffer[i]
    ret out

/** A new array of the elements in the order {@link pop} would take them, in the current pool. The heap keeps them. */
fn sorted(h: Heap<T>): <T>[] do
    val out = toArray(h)
    sortBy(out, h.less)
    ret out

/** A new {@link List} of `f` of each element, in no particular order, in the current pool. */
fn map(h: Heap<T>, f: (<T>) => <U>): List<U> do
    val out: List<U> = list(h.len)
    for i in 0..<h.len do
        push(out, f(h.buffer[i]))
    ret out

/** A new {@link Heap} of the elements `test` is true for, with the same order, in the current pool. */
fn filter(h: Heap<T>, test: (<T>) => Bool): Heap<T> do
    val out = heap(h.less)
    for i in 0..<h.len where test(h.buffer[i]) do
        push(out, h.buffer[i])
    ret out

/** Combines the elements into one value, in no particular order. */
fn fold(h: Heap<T>, initial: <U>, f: (<U>, <T>) => <U>): <U> do
    var result = initial
    for i in 0..<h.len do
        result = f(result, h.buffer[i])
    ret result
