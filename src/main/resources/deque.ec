/**
 * Deque<T>, a queue with two ends.
 *
 *     use "core:deque.ec"
 *     val jobs: Deque<Int> = deque()
 *     jobs.pushBack(1)
 *     io.log(jobs.popFront() ?? 0)                    // 1
 *
 * The functions are top-level, so they can be called on a structure. Its memory is in the pool that was current when it
 * was made (or the pool given), and it grows there. What it holds isn't copied: Strings and pointers put in it must live at
 * least as long as it does, which the compiler checks. core:structures.ec has every structure at once.
 */
use "core:errors.ec"
use "core:string.ec"

/**
 * A queue with two ends: adding and taking at either end is fast. As a first-in first-out queue, use
 * {@link pushBack} and {@link popFront}.
 * @param <T> the type of the elements
 */
mut ref rec Deque<T> as
    /** The ring of elements. */
    buffer: T[]
    /** The index in {@code buffer} of the first element. */
    head: Size
    /** How many elements it holds. */
    len: Size
    /** The pool its memory is in. */
    mem: Pool

/** A new empty {@link Deque}, in the current pool: `val jobs: Deque<Int> = deque()`. */
fn deque(): Deque<T> = new Deque<T> { unsafe new (<T>)[8], 0, 0, pool }

/** Makes room for one more element. */
priv fn growIfFull(d: Deque<T>) do
    if d.len == d.buffer.len do
        val grown = unsafe new (<T>)[d.buffer.len * 2] in d.mem
        for i in 0..<d.len do
            grown[i] = d.buffer[(d.head + i) % d.buffer.len]
        d.buffer = grown
        d.head = 0

/** Adds `value` at the end. */
fn pushBack(d: Deque<T>, value: <T>) do
    growIfFull(d)
    d.buffer[(d.head + d.len) % d.buffer.len] = value
    d.len += 1

/** Adds `value` at the start. */
fn pushFront(d: Deque<T>, value: <T>) do
    growIfFull(d)
    d.head = (d.head + d.buffer.len - 1) % d.buffer.len
    d.buffer[d.head] = value
    d.len += 1

/** Takes the first element away. @return the element, or null when the deque is empty */
fn popFront(d: Deque<T>): <T>? do
    ret null if d.len == 0
    val value = d.buffer[d.head]
    d.head = (d.head + 1) % d.buffer.len
    d.len -= 1
    ret value

/** Takes the last element away. @return the element, or null when the deque is empty */
fn popBack(d: Deque<T>): <T>? do
    ret null if d.len == 0
    d.len -= 1
    ret d.buffer[(d.head + d.len) % d.buffer.len]

/** The first element, or null when the deque is empty. */
fn first(d: Deque<T>): <T>? do
    ret null if d.len == 0
    ret d.buffer[d.head]

/** The last element, or null when the deque is empty. */
fn last(d: Deque<T>): <T>? do
    ret null if d.len == 0
    ret d.buffer[(d.head + d.len - 1) % d.buffer.len]

/** The element at index `i`, counted from the first (0). The program stops when there is none. */
fn get(d: Deque<T>, i: Size): <T> do
    assert i < d.len, "the index is past the end of the Deque"
    ret d.buffer[(d.head + i) % d.buffer.len]

/** Replaces the element at index `i`, counted from the first (0). The program stops when there is none. */
fn set(d: Deque<T>, i: Size, value: <T>) do
    assert i < d.len, "the index is past the end of the Deque"
    d.buffer[(d.head + i) % d.buffer.len] = value

/** Takes every element away, and keeps the room they took. */
fn clear(d: Deque<T>) do
    d.head = 0
    d.len = 0

/** Whether the deque has no elements. */
fn isEmpty(d: Deque<T>): Bool = d.len == 0

/** How many elements it holds. */
fn size(d: Deque<T>): Size = d.len

/** A new array of the elements, from first to last, in the current pool. */
fn toArray(d: Deque<T>): <T>[] do
    val out = unsafe new (<T>)[d.len]
    for i in 0..<d.len do
        out[i] = d.buffer[(d.head + i) % d.buffer.len]
    ret out

/** Goes through the elements of a {@link Deque}, for `for`. */
mut rec DequeIter<T> as
    /** The deque. */
    queue: Deque<T>
    /** How many elements are behind. */
    at: Size

/** What `for x in d do` loops over: the elements, from first to last. Adding or taking at the start during the loop moves it along with them. */
fn iter(d: Deque<T>): DequeIter<T> = DequeIter<T> { d, 0 }

/** The next element, or null after the last. */
fn next(mut i: DequeIter<T>): <T>? do
    ret null if i.at >= i.queue.len
    val value = i.queue.buffer[(i.queue.head + i.at) % i.queue.buffer.len]
    i.at += 1
    ret value

/** A new {@link Deque} of `f` of each element, from first to last, in the current pool. */
fn map(d: Deque<T>, f: (<T>) => <U>): Deque<U> do
    val out: Deque<U> = deque()
    for i in 0..<d.len do
        pushBack(out, f(d.buffer[(d.head + i) % d.buffer.len]))
    ret out

/** A new {@link Deque} of the elements `test` is true for, in the same order, in the current pool. */
fn filter(d: Deque<T>, test: (<T>) => Bool): Deque<T> do
    val out: Deque<T> = deque()
    for i in 0..<d.len do
        val v = d.buffer[(d.head + i) % d.buffer.len]
        if test(v) do
            pushBack(out, v)
    ret out

/** Combines the elements into one value, from first to last. */
fn fold(d: Deque<T>, initial: <U>, f: (<U>, <T>) => <U>): <U> do
    var result = initial
    for i in 0..<d.len do
        result = f(result, d.buffer[(d.head + i) % d.buffer.len])
    ret result
