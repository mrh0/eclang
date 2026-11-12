/**
 * Counter<T>, which counts how many times each value was added (a multiset).
 *
 *     use "core:counter.ec"
 *     val words: Counter<String> = counter()
 *     for w in ["a", "b", "a"] do
 *         words.add(w)
 *     io.log(words.count("a"))                        // 2
 *
 * The functions are top-level, so they can be called on a structure. Its memory is in the pool that was current when it
 * was made, and it grows there. What it holds isn't copied: Strings and pointers put in it must live at least as long as
 * it does, which the compiler checks. It isn't part of core:structures.ec.
 */
use "core:errors.ec"
use "core:string.ec"
use "core:map.ec"
use "core:array.ec"

/**
 * Counts how many times each value was added. Values are found like the keys of a {@link Map}: with `hash(value)` and `==`.
 * A value whose count drops to 0 is taken away.
 *
 *     val letters: Counter<Char> = counter()
 *     for c in "banana" do
 *         letters.add(c)
 *     for (c, n) in letters.mostCommon(2) do         // a: 3, then n: 2
 *         io.log("${c}: ${n}")
 *
 * @param <T> the type of the values, which need `hash(value)` and `==`
 */
mut ref rec Counter<T> as
    /** The count of each value, in the order they were first added. */
    counts: Map<T, Size>
    /** The sum of the counts. */
    total: Size

/** A new empty {@link Counter}, in the current pool: `val words: Counter<String> = counter()`. */
fn counter(): Counter<T> = new Counter<T> { map(), 0 }

/** A new {@link Counter} of the `values`, each counted as often as it appears: `counterOf("a", "b", "a")`. */
fn counterOf(...values: <T>): Counter<T> do
    val c: Counter<T> = counter()
    for v in values do
        add(c, v)
    ret c

/** Adds `value` once. @return its count now */
fn add(c: Counter<T>, value: <T>): Size = add(c, value, 1)

/** Adds `value` `n` times. @return its count now */
fn add(c: Counter<T>, value: <T>, n: Size): Size do
    ret count(c, value) if n == 0
    val now = (get(c.counts, value) ?? 0) + n
    put(c.counts, value, now)
    c.total += n
    ret now

/** Adds each of the `values` once. */
fn addAll(c: Counter<T>, values: <T>[]) do
    for v in values do
        add(c, v)

/** Takes `value` away once, if the counter has it. @return its count now */
fn subtract(c: Counter<T>, value: <T>): Size = subtract(c, value, 1)

/** Takes `value` away `n` times, down to 0, which removes it. @return its count now */
fn subtract(c: Counter<T>, value: <T>, n: Size): Size do
    val had = get(c.counts, value) ?? ret 0
    if n >= had do
        remove(c.counts, value)
        c.total -= had
        ret 0
    put(c.counts, value, had - n)
    c.total -= n
    ret had - n

/** How many times `value` was added, 0 when it never was. */
fn count(c: Counter<T>, value: <T>): Size = get(c.counts, value) ?? 0

/** Whether `value` has a count above 0. */
fn has(c: Counter<T>, value: <T>): Bool = has(c.counts, value)

/** Takes `value` away, whatever its count. @return whether the counter had it */
fn remove(c: Counter<T>, value: <T>): Bool do
    val had = get(c.counts, value) ?? ret false
    remove(c.counts, value)
    c.total -= had
    ret true

/** Takes every value away. */
fn clear(c: Counter<T>) do
    clear(c.counts)
    c.total = 0

/** Whether the counter has no values. */
fn isEmpty(c: Counter<T>): Bool = c.counts.len == 0

/** How many different values it holds. See {@link total} for the sum of their counts. */
fn size(c: Counter<T>): Size = c.counts.len

/** The sum of the counts: how many times anything was added and not taken away. */
fn total(c: Counter<T>): Size = c.total

/** A new array of the different values, in the order they were first added, in the current pool. */
fn toArray(c: Counter<T>): <T>[] = keys(c.counts)

/**
 * A new array of the values and their counts, from the largest count, in the current pool. Equal counts keep the order
 * in which their values were first added.
 * @param n how many to give at most
 */
fn mostCommon(c: Counter<T>, n: Size): (<T>, Size)[] do
    val all = mostCommon(c)
    ret if n >= all.len do all else all[0..<n]

/** A new array of every value and its count, from the largest count, in the current pool. */
fn mostCommon(c: Counter<T>): (<T>, Size)[] do
    val out = unsafe new ((<T>, Size))[c.counts.len]
    var i: Size = 0
    for v, n in c.counts do
        out[i] = (v, n)
        i += 1
    sortBy(out, fn (a, b) = a.second > b.second)
    ret out

/** What `for v, n in c do` loops over: each value and its count, in the order they were first added. */
fn pairs(c: Counter<T>): MapIter<T, Size> = pairs(c.counts)

/** What `for v in c do` loops over: each value and its count, in the order they were first added. */
fn iter(c: Counter<T>): MapIter<T, Size> = pairs(c.counts)
