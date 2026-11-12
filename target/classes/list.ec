/**
 * List<T>, an array that grows: adding at the end is fast, as is reading by index.
 *
 *     use "core:list.ec"
 *     val names: List<String> = list()
 *     names.push("Ada")
 *     for name in names do
 *         io.log(name)
 *     val numbers = listOf(3, 1, 2)                   // a List<Int>
 *
 * The functions are top-level, so they can be called on a structure. Its memory is in the pool that was current when it
 * was made (or the pool given), and it grows there. What it holds isn't copied: Strings and pointers put in it must live at
 * least as long as it does, which the compiler checks. core:structures.ec has every structure at once.
 */
use "core:errors.ec"
use "core:string.ec"

/**
 * An array that grows: adding at the end is fast, as is reading by index.
 * @param <T> the type of the elements
 */
mut ref rec List<T> as
    /** The elements, and room for more. */
    buffer: T[]
    /** How many elements it holds. */
    len: Size
    /** The pool its memory is in. */
    mem: Pool

/**
 * A new empty {@link List}, in the current pool: `val names: List<String> = list()`.
 * @param capacity how many elements it has room for before it grows
 */
fn list(capacity: Size = 8): List<T> = new List<T> { unsafe new (<T>)[if capacity == 0 do 8 else capacity], 0, pool }

/**
 * A new empty {@link List}, in the pool `mem`.
 * @param mem the pool for its memory
 * @param capacity how many elements it has room for before it grows
 */
fn list(mem: Pool, capacity: Size): List<T> = new List<T> { unsafe new (<T>)[if capacity == 0 do 8 else capacity] in mem, 0, mem } in mem

/** A new {@link List} of the `values`, in the current pool: `listOf(1, 2, 3)`. */
fn listOf(...values: <T>): List<T> do
    val l: List<T> = list(values.len)
    for v in values do
        push(l, v)
    ret l

/** A new {@link List} with the elements of the array `values`, in the current pool. */
fn toList(values: <T>[]): List<T> do
    val l: List<T> = list(values.len)
    for v in values do
        push(l, v)
    ret l

/** Makes room for `n` more elements, so that adding them doesn't grow the list again. */
fn reserve(l: List<T>, n: Size) do
    if l.len + n > l.buffer.len do
        var capacity = l.buffer.len * 2
        while capacity < l.len + n do
            capacity *= 2
        val grown = unsafe new (<T>)[capacity] in l.mem
        for i in 0..<l.len do
            grown[i] = l.buffer[i]
        l.buffer = grown

/** Adds `value` at the end. */
fn push(l: List<T>, value: <T>) do
    reserve(l, 1)
    l.buffer[l.len] = value
    l.len += 1

/** Adds the elements of `values` at the end. */
fn pushAll(l: List<T>, values: <T>[]) do
    reserve(l, values.len)
    for v in values do
        l.buffer[l.len] = v
        l.len += 1

/** Takes the last element away. @return the element, or null when the list is empty */
fn pop(l: List<T>): <T>? do
    ret null if l.len == 0
    l.len -= 1
    ret l.buffer[l.len]

/** The element at index `i`. The program stops when there is none. */
fn get(l: List<T>, i: Size): <T> do
    assert i < l.len, "the index is past the end of the List"
    ret l.buffer[i]

/** Replaces the element at index `i`. The program stops when there is none. */
fn set(l: List<T>, i: Size, value: <T>) do
    assert i < l.len, "the index is past the end of the List"
    l.buffer[i] = value

/** The first element, or null when the list is empty. */
fn first(l: List<T>): <T>? do
    ret null if l.len == 0
    ret l.buffer[0]

/** The last element, or null when the list is empty. */
fn last(l: List<T>): <T>? do
    ret null if l.len == 0
    ret l.buffer[l.len - 1]

/**
 * Puts `value` before the element at index `i`, moving the later elements up.
 * @param i from 0 to `l.len`, which adds it at the end; the program stops otherwise
 */
fn insert(l: List<T>, i: Size, value: <T>) do
    assert i <= l.len, "the index is past the end of the List"
    reserve(l, 1)
    var at = l.len
    while at > i do
        l.buffer[at] = l.buffer[at - 1]
        at -= 1
    l.buffer[i] = value
    l.len += 1

/** Takes away the element at index `i`, moving the later elements down. The program stops when there is none. @return the element */
fn removeAt(l: List<T>, i: Size): <T> do
    assert i < l.len, "the index is past the end of the List"
    val value = l.buffer[i]
    for at in i + 1..<l.len do
        l.buffer[at - 1] = l.buffer[at]
    l.len -= 1
    ret value

/** Takes every element away, and keeps the room they took. */
fn clear(l: List<T>) do
    l.len = 0

/** Whether the list has no elements. */
fn isEmpty(l: List<T>): Bool = l.len == 0

/** How many elements it holds. */
fn size(l: List<T>): Size = l.len

/**
 * The elements as an array, for `for` loops and core:array.ec: `for x in l.items() do`. It shares their memory, so
 * changing an element changes the list; once the list grows, it no longer does. See {@link toArray}.
 */
fn items(l: List<T>): <T>[] = l.buffer[0..<l.len]

/** What `for x in l do` loops over: the elements, as {@link items} gives them. Elements added during the loop aren't part of it. */
fn iter(l: List<T>): <T>[] = items(l)

/** A new array of the elements, in the current pool. */
fn toArray(l: List<T>): <T>[] do
    val out = unsafe new (<T>)[l.len]
    for i in 0..<l.len do
        out[i] = l.buffer[i]
    ret out

/** Whether `value` is in the list, compared with `==`. */
fn contains(l: List<T>, value: <T>): Bool = indexOf(l, value) != null

/** The index of the first `value`, or null when there is none. */
fn indexOf(l: List<T>, value: <T>): Size? do
    for i in 0..<l.len where l.buffer[i] == value do
        ret i
    ret null

/** A new {@link List} of `f` of each element, in the current pool: `listOf(1, 2).map(fn (x) = x * 2)`. */
fn map(l: List<T>, f: (<T>) => <U>): List<U> do
    val out: List<U> = list(l.len)
    for v in items(l) do
        push(out, f(v))
    ret out

/** A new {@link List} of the elements `test` is true for, in the same order, in the current pool. */
fn filter(l: List<T>, test: (<T>) => Bool): List<T> do
    val out: List<T> = list()
    for v in items(l) where test(v) do
        push(out, v)
    ret out

/** Combines the elements into one value, from the first to the last, as `reduce` does for an array. */
fn fold(l: List<T>, initial: <U>, f: (<U>, <T>) => <U>): <U> do
    var result = initial
    for v in items(l) do
        result = f(result, v)
    ret result
