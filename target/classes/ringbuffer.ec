/**
 * RingBuffer<T>, a queue of fixed size that drops its oldest element to make room for a new one.
 *
 *     use "core:ringbuffer.ec"
 *     val recent: RingBuffer<Int> = ringBuffer(3)
 *     for i in 1..=5 do
 *         recent.push(i)
 *     io.log(recent.first() ?? 0)                     // 3
 *
 * The functions are top-level, so they can be called on a structure. Its memory is in the pool that was current when it
 * was made, and it never grows. What it holds isn't copied: Strings and pointers put in it must live at least as long as
 * it does, which the compiler checks. It isn't part of core:structures.ec.
 */
use "core:errors.ec"
use "core:string.ec"

/**
 * A queue with room for a fixed number of elements: when it is full, {@link push} drops the oldest. It allocates nothing
 * after it is made, which suits the last lines of a log, a moving average or recent input.
 *
 *     val last: RingBuffer<String> = ringBuffer(2)
 *     last.push("a")
 *     last.push("b")
 *     io.log(last.push("c") ?? "")                   // a, which it dropped
 *     for s in last do                                // b, then c
 *         io.log(s)
 *
 * @param <T> the type of the elements
 */
mut ref rec RingBuffer<T> as
    /** The ring of elements, as long as the capacity. */
    buffer: T[]
    /** The index in {@code buffer} of the oldest element. */
    head: Size
    /** How many elements it holds. */
    len: Size

/**
 * A new empty {@link RingBuffer}, in the current pool: `val recent: RingBuffer<Int> = ringBuffer(100)`.
 * @param capacity how many elements it holds at most, at least 1; the program stops otherwise
 */
fn ringBuffer(capacity: Size): RingBuffer<T> do
    assert capacity > 0, "a RingBuffer needs room for at least one element"
    ret new RingBuffer<T> { unsafe new (<T>)[capacity], 0, 0 }

/** Adds `value` as the newest element. @return the oldest element, which it dropped to make room, or null when there was room */
fn push(r: RingBuffer<T>, value: <T>): <T>? do
    if r.len < r.buffer.len do
        r.buffer[(r.head + r.len) % r.buffer.len] = value
        r.len += 1
        ret null
    val dropped = r.buffer[r.head]
    r.buffer[r.head] = value
    r.head = (r.head + 1) % r.buffer.len
    ret dropped

/** Takes the oldest element away. @return the element, or null when the buffer is empty */
fn popFront(r: RingBuffer<T>): <T>? do
    ret null if r.len == 0
    val value = r.buffer[r.head]
    r.head = (r.head + 1) % r.buffer.len
    r.len -= 1
    ret value

/** Takes the newest element away. @return the element, or null when the buffer is empty */
fn popBack(r: RingBuffer<T>): <T>? do
    ret null if r.len == 0
    r.len -= 1
    ret r.buffer[(r.head + r.len) % r.buffer.len]

/** The oldest element, or null when the buffer is empty. */
fn first(r: RingBuffer<T>): <T>? do
    ret null if r.len == 0
    ret r.buffer[r.head]

/** The newest element, or null when the buffer is empty. */
fn last(r: RingBuffer<T>): <T>? do
    ret null if r.len == 0
    ret r.buffer[(r.head + r.len - 1) % r.buffer.len]

/** The element at index `i`, counted from the oldest (0). The program stops when there is none. */
fn get(r: RingBuffer<T>, i: Size): <T> do
    assert i < r.len, "the index is past the end of the RingBuffer"
    ret r.buffer[(r.head + i) % r.buffer.len]

/** Replaces the element at index `i`, counted from the oldest (0). The program stops when there is none. */
fn set(r: RingBuffer<T>, i: Size, value: <T>) do
    assert i < r.len, "the index is past the end of the RingBuffer"
    r.buffer[(r.head + i) % r.buffer.len] = value

/** Takes every element away. */
fn clear(r: RingBuffer<T>) do
    r.head = 0
    r.len = 0

/** Whether the buffer has no elements. */
fn isEmpty(r: RingBuffer<T>): Bool = r.len == 0

/** Whether the buffer is full, so that the next {@link push} drops the oldest element. */
fn isFull(r: RingBuffer<T>): Bool = r.len == r.buffer.len

/** How many elements it holds. */
fn size(r: RingBuffer<T>): Size = r.len

/** How many elements it holds at most. */
fn capacity(r: RingBuffer<T>): Size = r.buffer.len

/** A new array of the elements, from the oldest, in the current pool. */
fn toArray(r: RingBuffer<T>): <T>[] do
    val out = unsafe new (<T>)[r.len]
    for i in 0..<r.len do
        out[i] = r.buffer[(r.head + i) % r.buffer.len]
    ret out

/** Goes through the elements of a {@link RingBuffer}, for `for`. */
mut rec RingBufferIter<T> as
    /** The buffer. */
    ring: RingBuffer<T>
    /** How many elements are behind. */
    at: Size

/** What `for x in r do` loops over: the elements, from the oldest. */
fn iter(r: RingBuffer<T>): RingBufferIter<T> = RingBufferIter<T> { r, 0 }

/** The next element, or null after the newest. */
fn next(mut i: RingBufferIter<T>): <T>? do
    ret null if i.at >= i.ring.len
    val value = i.ring.buffer[(i.ring.head + i.at) % i.ring.buffer.len]
    i.at += 1
    ret value

/** Combines the elements into one value, from the oldest. */
fn fold(r: RingBuffer<T>, initial: <U>, f: (<U>, <T>) => <U>): <U> do
    var result = initial
    for i in 0..<r.len do
        result = f(result, r.buffer[(r.head + i) % r.buffer.len])
    ret result
