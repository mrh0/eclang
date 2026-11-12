/**
 * Set<T>, a hash set that keeps its values in the order they were added, with union, intersection and difference.
 *
 *     use "core:set.ec"
 *     val seen: Set<Int> = set()
 *     seen.add(7)
 *     io.log(seen.has(7))                             // true
 *
 * Values are found with `hash(value)` and `==`, as the keys of a Map (core:map.ec).
 *
 * The functions are top-level, so they can be called on a structure. Its memory is in the pool that was current when it
 * was made (or the pool given), and it grows there. What it holds isn't copied: Strings and pointers put in it must live at
 * least as long as it does, which the compiler checks. core:structures.ec has every structure at once.
 */
use "core:errors.ec"
use "core:string.ec"
use "core:map.ec"

/**
 * Holds each value at most once, and tells quickly whether it holds one. Its values stay in the order they were added.
 * @param <T> the type of the values, which need `hash(value)` and `==`
 */
ref rec Set<T> as
    /** The values, as keys. */
    table: Map<T, Bool>

/** A new empty {@link Set}, in the current pool: `val seen: Set<Int> = set()`. */
fn set(): Set<T> = new Set<T> { map() }

/** A new {@link Set} of the `values`, in the current pool: `setOf(1, 2, 3)`. */
fn setOf(...values: <T>): Set<T> do
    val s: Set<T> = set()
    for v in values do
        add(s, v)
    ret s

/** Adds `value`. @return whether it was new */
fn add(s: Set<T>, value: <T>): Bool do
    ret false if has(s.table, value)
    put(s.table, value, true)
    ret true

/** Whether the set has `value`. */
fn has(s: Set<T>, value: <T>): Bool = has(s.table, value)

/** Takes `value` away. @return whether the set had it */
fn remove(s: Set<T>, value: <T>): Bool = remove(s.table, value)

/** Takes every value away. */
fn clear(s: Set<T>) do
    clear(s.table)

/** Whether the set has no values. */
fn isEmpty(s: Set<T>): Bool = s.table.len == 0

/** How many values it holds. */
fn size(s: Set<T>): Size = s.table.len

/** A new array of the values, in the order they were added, in the current pool. */
fn toArray(s: Set<T>): <T>[] = keys(s.table)

/** Goes through the values of a {@link Set}, for `for`. */
mut rec SetIter<T> as
    /** The keys are the values. */
    keys: MapIter<T, Bool>

/** What `for x in s do` loops over: the values, in the order they were added. A value added during the loop may make it skip others. */
fn iter(s: Set<T>): SetIter<T> = SetIter<T> { pairs(s.table) }

/** The next value, or null after the last. */
fn next(mut i: SetIter<T>): <T>? do
    if val entry = next(mut i.keys) do
        ret entry.first
    ret null

/** A new {@link Set} of the values in `a` or `b`, in the current pool. */
fn union(a: Set<T>, b: Set<T>): Set<T> do
    val out: Set<T> = set()
    for v in toArray(a) do
        add(out, v)
    for v in toArray(b) do
        add(out, v)
    ret out

/** A new {@link Set} of the values in both `a` and `b`, in the current pool. */
fn intersection(a: Set<T>, b: Set<T>): Set<T> do
    val out: Set<T> = set()
    for v in toArray(a) where has(b, v) do
        add(out, v)
    ret out

/** A new {@link Set} of the values in `a` that aren't in `b`, in the current pool. */
fn difference(a: Set<T>, b: Set<T>): Set<T> do
    val out: Set<T> = set()
    for v in toArray(a) where not has(b, v) do
        add(out, v)
    ret out


/** A new {@link Set} of `f` of each value, in the current pool. Values `f` makes equal become one. */
fn map(s: Set<T>, f: (<T>) => <U>): Set<U> do
    val out: Set<U> = set()
    for v in toArray(s) do
        add(out, f(v))
    ret out

/** A new {@link Set} of the values `test` is true for, in the same order, in the current pool. */
fn filter(s: Set<T>, test: (<T>) => Bool): Set<T> do
    val out: Set<T> = set()
    for v in toArray(s) where test(v) do
        add(out, v)
    ret out

/** Combines the values into one value, in the order they were added. */
fn fold(s: Set<T>, initial: <U>, f: (<U>, <T>) => <U>): <U> do
    var result = initial
    for v in toArray(s) do
        result = f(result, v)
    ret result
