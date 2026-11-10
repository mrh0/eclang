/**
 * Data structures: List, Map, Set, Deque, Heap, SlotPool, SortedMap, SortedSet, BitSet and LruCache. They are generic, so each
 * holds one type of element.
 *
 *     use "core:structures.ec"
 *     val names: List<String> = list()
 *     names.push("Ada")
 *     for name in names do
 *         io.log(name)
 *
 *     val ages: Map<String, Int> = map()
 *     ages.put("Ada", 36)
 *     io.log(ages.get("Ada") ?? 0)                    // 36
 *     for name, age in ages do
 *         io.log("${name}: ${age}")
 *
 *     val seen: Set<Int> = set()
 *     seen.add(7)
 *     val numbers = listOf(3, 1, 2)                   // a List<Int>
 *
 *     val scores: SortedMap<Int, String> = sortedMap()  // keys in order, and ranges of them
 *     for score, name in scores.range(50, 100) do
 *         io.log("${name}: ${score}")
 *
 * The type on the left says what a new structure holds, as `list()` itself has no element to tell. The functions
 * are top-level, so they can be called on a structure. Its memory is in the pool that was current when it was made (or
 * the pool given), and it grows there. What it holds isn't copied: Strings and pointers put in it must live at least
 * as long as it does, which the compiler checks: `l.push("${i}")` in a `pool do` block is an error when l is from outside it.
 *
 * Map and Set find keys with `hash(key)` and `==`. Anything `==` can compare works as it is: the compiler generates a
 * `hash` for records and tuples. For one of your own, define `fn hash(p: Point): ULong` at the top level of a file. SortedMap and
 * SortedSet compare keys with `<` (Strings by their bytes), or with a `less` function given to {@link sortedMap}. See core:array.ec
 * for sorting and searching: `numbers.items().sort()`.
 */
use "core:errors.ec"
use "core:string.ec"
use "core:math.ec"

// ---------------------------------------------------------------------------------------------------
// List

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

// ---------------------------------------------------------------------------------------------------
// Hashes

/** A hash of `x`, for {@link Map} and {@link Set}: the bits are well mixed (the finalizer of SplitMix64). */
fn hash(x: ULong): ULong do
    var h = x
    h = math.wrappingMul(h ^ (h >> 30), (0xBF58476Dul << 32) | 0x1CE4E5B9ul)
    h = math.wrappingMul(h ^ (h >> 27), (0x94D049BBul << 32) | 0x133111EBul)
    ret h ^ (h >> 31)

/** A hash of `x`. */
fn hash(x: Long): ULong = hash(x as ULong)

/** A hash of `x`. */
fn hash(x: Size): ULong = hash(x as ULong)

// ---------------------------------------------------------------------------------------------------
// Map

/** A key and its value in a {@link Map}. */
mut rec MapEntry<K, V> as
    /** The key. */
    key: K
    /** Its value. */
    value: V
    /** False once it is removed. */
    live: Bool

/** In {@code slots}: this slot held a key that was removed, so looking further is still needed. */
priv const REMOVED: Size = 1

/**
 * Finds values by their keys, in about the same time however many it holds. Its keys and values stay in the order
 * they were added.
 * @param <K> the type of the keys, which need `hash(key)` and `==`
 * @param <V> the type of the values
 */
mut ref rec Map<K, V> as
    /** The keys and values, in the order they were added. */
    entries: MapEntry<K, V>[]
    /** How much of {@code entries} is in use, removed entries included. */
    used: Size
    /** The hash table: 0 for a free slot, {@code REMOVED}, or 2 + an index in {@code entries}. Twice as long as entries, a power of two. */
    slots: Size[]
    /** How many keys it holds. */
    len: Size
    /** The pool its memory is in. */
    mem: Pool

/** A new empty {@link Map}, in the current pool: `val ages: Map<String, Int> = map()`. */
fn map(): Map<K, V> = new Map<K, V> { unsafe new (MapEntry<K, V>)[8], 0, new Size[16], 0, pool }

/** A new empty {@link Map}, in the pool `mem`. */
fn map(mem: Pool): Map<K, V> = new Map<K, V> { unsafe new (MapEntry<K, V>)[8] in mem, 0, new Size[16] in mem, 0, mem } in mem

/** The slot holding `key`, or null when the map doesn't have it. */
priv fn findSlot(m: Map<K, V>, key: <K>): Size? do
    val mask = m.slots.len - 1
    var i = (hash(key) as Size) & mask
    while m.slots[i] != 0 do
        val slot = m.slots[i]
        if slot != REMOVED and m.entries[slot - 2].live and m.entries[slot - 2].key == key do
            ret i
        i = (i + 1) & mask
    ret null

/** Remembers that entry `index` holds `key`, which isn't in the table yet. */
priv fn claimSlot(m: Map<K, V>, key: <K>, index: Size) do
    val mask = m.slots.len - 1
    var i = (hash(key) as Size) & mask
    while m.slots[i] != 0 and m.slots[i] != REMOVED do
        i = (i + 1) & mask
    m.slots[i] = index + 2

/** Makes room for another entry: moves the live entries to new arrays, twice as long when more than half are live. */
priv fn rebuild(m: Map<K, V>) do
    val capacity = if m.len * 2 > m.entries.len do m.entries.len * 2 else m.entries.len
    val old = m.entries
    val count = m.used
    m.entries = unsafe new (MapEntry<K, V>)[capacity] in m.mem
    m.slots = new Size[capacity * 2] in m.mem
    m.used = 0
    for i in 0..<count where old[i].live do
        m.entries[m.used] = old[i]
        claimSlot(m, old[i].key, m.used)
        m.used += 1

/** Sets the value of `key`, adding the key when the map doesn't have it. */
fn put(m: Map<K, V>, key: <K>, value: <V>) do
    if val slot = findSlot(m, key) do
        m.entries[m.slots[slot] - 2].value = value
    else do
        if m.used == m.entries.len do
            rebuild(m)
        m.entries[m.used] = MapEntry<K, V> { key, value, true }
        claimSlot(m, key, m.used)
        m.used += 1
        m.len += 1

/** The value of `key`, or null when the map doesn't have it. */
fn get(m: Map<K, V>, key: <K>): <V>? do
    if val slot = findSlot(m, key) do
        ret m.entries[m.slots[slot] - 2].value
    ret null

/** Whether the map has `key`. */
fn has(m: Map<K, V>, key: <K>): Bool = findSlot(m, key) != null

/** Takes `key` and its value away. @return whether the map had it */
fn remove(m: Map<K, V>, key: <K>): Bool do
    if val slot = findSlot(m, key) do
        m.entries[m.slots[slot] - 2].live = false
        m.slots[slot] = REMOVED
        m.len -= 1
        ret true
    ret false

/** Takes every key away, and keeps the room they took. */
fn clear(m: Map<K, V>) do
    for i in 0..<m.slots.len do
        m.slots[i] = 0
    m.used = 0
    m.len = 0

/** Whether the map has no keys. */
fn isEmpty(m: Map<K, V>): Bool = m.len == 0

/** How many keys it holds. */
fn size(m: Map<K, V>): Size = m.len

/** A new array of the keys, in the order they were added, in the current pool. */
fn keys(m: Map<K, V>): <K>[] do
    val out = unsafe new (<K>)[m.len]
    var n: Size = 0
    for i in 0..<m.used where m.entries[i].live do
        out[n] = m.entries[i].key
        n += 1
    ret out

/** A new array of the values, in the order their keys were added, in the current pool. */
fn values(m: Map<K, V>): <V>[] do
    val out = unsafe new (<V>)[m.len]
    var n: Size = 0
    for i in 0..<m.used where m.entries[i].live do
        out[n] = m.entries[i].value
        n += 1
    ret out

/** Goes through the keys and values of a {@link Map}, for `for`. */
mut rec MapIter<K, V> as
    /** The map. */
    table: Map<K, V>
    /** The index in its {@code entries} to look at next. */
    at: Size

/**
 * What `for key, value in m do` loops over: each key and its value, in the order they were added. Keys and values can
 * be changed and removed during the loop, but a key added during the loop may make it skip others.
 */
fn pairs(m: Map<K, V>): MapIter<K, V> = MapIter<K, V> { m, 0 }

/** What `for (key, value) in m do` loops over: the same as {@link pairs}. */
fn iter(m: Map<K, V>): MapIter<K, V> = pairs(m)

/** The next key and its value, or null after the last. */
fn next(mut i: MapIter<K, V>): (<K>, <V>)? do
    while i.at < i.table.used do
        val entry = i.table.entries[i.at]
        i.at += 1
        ret (entry.key, entry.value) if entry.live
    ret null

/** The value of `key`; when the map doesn't have it, puts `make()` and gives that: `m.getOrPut(k, fn () = list())`. */
fn getOrPut(m: Map<K, V>, key: <K>, make: () => <V>): <V> do
    if val slot = findSlot(m, key) do
        ret m.entries[m.slots[slot] - 2].value
    val value = make()
    put(m, key, value)
    ret value

/** Replaces the value of `key` with `f` of it. @return whether the map had `key`; it is left out otherwise */
fn update(m: Map<K, V>, key: <K>, f: (<V>) => <V>): Bool do
    if val slot = findSlot(m, key) do
        val entry = m.slots[slot] - 2
        m.entries[entry].value = f(m.entries[entry].value)
        ret true
    ret false

/** Sets the value of `key` to `f` of its value, or of `initial` when the map doesn't have it: `counts.update(word, 0, fn (n) = n + 1)`. */
fn update(m: Map<K, V>, key: <K>, initial: <V>, f: (<V>) => <V>) do
    if val slot = findSlot(m, key) do
        val entry = m.slots[slot] - 2
        m.entries[entry].value = f(m.entries[entry].value)
    else do
        put(m, key, f(initial))

/** A new {@link Map} of the same keys, each with `f` of its value, in the current pool. */
fn mapValues(m: Map<K, V>, f: (<V>) => <U>): Map<K, U> do
    val out: Map<K, U> = map()
    for i in 0..<m.used where m.entries[i].live do
        put(out, m.entries[i].key, f(m.entries[i].value))
    ret out

/** A new {@link List} of `f` of each key and its value, in the order they were added, in the current pool. */
fn map(m: Map<K, V>, f: (<K>, <V>) => <U>): List<U> do
    val out: List<U> = list(m.len)
    for i in 0..<m.used where m.entries[i].live do
        push(out, f(m.entries[i].key, m.entries[i].value))
    ret out

/** A new {@link Map} of the keys and values `test` is true for, in the same order, in the current pool. */
fn filter(m: Map<K, V>, test: (<K>, <V>) => Bool): Map<K, V> do
    val out: Map<K, V> = map()
    for i in 0..<m.used where m.entries[i].live and test(m.entries[i].key, m.entries[i].value) do
        put(out, m.entries[i].key, m.entries[i].value)
    ret out

/** Combines the keys and values into one value, in the order they were added. */
fn fold(m: Map<K, V>, initial: <U>, f: (<U>, <K>, <V>) => <U>): <U> do
    var result = initial
    for i in 0..<m.used where m.entries[i].live do
        result = f(result, m.entries[i].key, m.entries[i].value)
    ret result

/** A new {@link Map} of the elements of `values` by `key` of each: `words.groupBy(fn (w) = w.len)`. Each list keeps their order. */
fn groupBy(values: <T>[], key: (<T>) => <K>): Map<K, List<T>> do
    val out: Map<K, List<T>> = map()
    for v in values do
        push(getOrPut(out, key(v), fn () = list()), v)
    ret out

/** A new {@link Map} of the key and value `f` gives for each element: `people.associate(fn (p) = (p.name, p.age))`. A later key replaces the value of an earlier one. */
fn associate(values: <T>[], f: (<T>) => (<K>, <V>)): Map<K, V> do
    val out: Map<K, V> = map()
    for v in values do
        val (k, value) = f(v)
        put(out, k, value)
    ret out

/** A new {@link Map} of the elements by `key` of each: `people.associateBy(fn (p) = p.name)`. A later key replaces the element of an earlier one. */
fn associateBy(values: <T>[], key: (<T>) => <K>): Map<K, T> do
    val out: Map<K, T> = map()
    for v in values do
        put(out, key(v), v)
    ret out

/** A new {@link Map} of key and value pairs: `toMap([("a", 1), ("b", 2)])`. A later key replaces the value of an earlier one. */
fn toMap(pairs: (<K>, <V>)[]): Map<K, V> do
    val out: Map<K, V> = map()
    for (k, v) in pairs do
        put(out, k, v)
    ret out

// ---------------------------------------------------------------------------------------------------
// Set

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
// ---------------------------------------------------------------------------------------------------
// Deque

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

// ---------------------------------------------------------------------------------------------------
// Heap

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

// ---------------------------------------------------------------------------------------------------
// SlotPool

/**
 * Where a value is in a {@link SlotPool}: an index, and which value has been there. It is a plain value (it holds no
 * pointer), so it can be kept anywhere, copied, compared, and even sent to another thread in a `thread.Queue`.
 */
rec SlotKey as
    /** The slot. */
    index: Size
    /** How often the slot was used before: a key of an earlier value there no longer matches. */
    generation: UInt

/**
 * Holds values whose lifetimes don't nest, such as the nodes of a graph or the entities of a game, and hands out a
 * {@link SlotKey} for each instead of a pointer. A value can be taken away at any time ({@link remove}), and its slot is used
 * again by a later {@link add}; nothing is freed until the pool the SlotPool is in is. A key of a value that was taken away
 * no longer finds anything ({@link get} gives null), even when another value now has its slot, as the slot counts how often
 * it was used. So a key that outlives its value is a mistake the program can see, never a pointer to freed or reused memory.
 * (Goose's slot pools reuse slots the same way; the count of uses is added here.)
 *
 *     val nodes: SlotPool<Node> = slotPool()
 *     val a = nodes.add(Node { "a" })
 *     nodes.remove(a)
 *     io.log(nodes.get(a) == null)                // true: a is gone, though its slot may be used again
 *
 * @param <T> the type of the values
 */
mut ref rec SlotPool<T> as
    /** The value in each slot; that of a slot taken away stays until the slot is used again. */
    values: List<T>
    /** How often each slot was used: odd while it holds a value, even while it is free. */
    generations: List<UInt>
    /** The slots taken away, to use again. */
    free: List<Size>
    /** How many values it holds. */
    live: Size

/** A new empty {@link SlotPool}, in the current pool: `val nodes: SlotPool<Node> = slotPool()`. */
fn slotPool(): SlotPool<T> = new SlotPool<T> { list(), list(), list(), 0 }

/** Whether `key` is that of a value `p` holds now. */
fn has(p: SlotPool<T>, key: SlotKey): Bool = key.index < p.generations.len and p.generations.buffer[key.index] == key.generation

/** Adds `value`, in a slot taken away earlier when there is one. @return its key */
fn add(p: SlotPool<T>, value: <T>): SlotKey do
    p.live += 1
    if val index = pop(p.free) do
        p.values.buffer[index] = value
        p.generations.buffer[index] += 1
        ret SlotKey { index, p.generations.buffer[index] }
    push(p.values, value)
    push(p.generations, 1)
    ret SlotKey { p.values.len - 1, 1 }

/** The value of `key`, or null when `p` no longer holds it (it was taken away). */
fn get(p: SlotPool<T>, key: SlotKey): <T>? do
    ret null if not has(p, key)
    ret p.values.buffer[key.index]

/** Replaces the value of `key`. @return whether `p` holds it; nothing changes when it doesn't */
fn set(p: SlotPool<T>, key: SlotKey, value: <T>): Bool do
    ret false if not has(p, key)
    p.values.buffer[key.index] = value
    ret true

/** Takes the value of `key` away; its slot is used again by a later {@link add}. @return whether `p` held it */
fn remove(p: SlotPool<T>, key: SlotKey): Bool do
    ret false if not has(p, key)
    p.generations.buffer[key.index] += 1
    push(p.free, key.index)
    p.live -= 1
    ret true

/** Whether it holds no values. */
fn isEmpty(p: SlotPool<T>): Bool = p.live == 0

/** How many values it holds. */
fn size(p: SlotPool<T>): Size = p.live

/** Goes through the values of a {@link SlotPool}, for `for`. */
mut rec SlotPoolIter<T> as
    /** The pool. */
    slots: SlotPool<T>
    /** The slot to look at next. */
    at: Size

/** What `for (key, value) in p do` loops over: each value it holds, with its key, in the order of the slots. */
fn pairs(p: SlotPool<T>): SlotPoolIter<T> = SlotPoolIter<T> { p, 0 }

/** What `for (key, value) in p do` loops over: the same as {@link pairs}. */
fn iter(p: SlotPool<T>): SlotPoolIter<T> = pairs(p)

/** The next key and its value, or null after the last. */
fn next(mut i: SlotPoolIter<T>): (SlotKey, <T>)? do
    while i.at < i.slots.values.len do
        val index = i.at
        i.at += 1
        val generation = i.slots.generations.buffer[index]
        ret (SlotKey { index, generation }, i.slots.values.buffer[index]) if generation % 2 == 1
    ret null

// ---------------------------------------------------------------------------------------------------
// SortedMap

/** Whether `a` comes before `b`, for {@link sortedMap} and {@link sortedSet}: `<`. */
priv fn comesBefore(a: <T>, b: <T>): Bool = a < b

/** Whether `a` comes before `b`, for {@link sortedMap} and {@link sortedSet}: Strings in the order of {@code compare}. */
priv fn comesBefore(a: String, b: String): Bool = compare(a, b) < 0

/** A key and its value in a {@link SortedMap}, and where it is in the tree. */
mut rec TreeNode<K, V> as
    /** The key. */
    key: K
    /** Its value. */
    value: V
    /** The node with the smaller keys (an index in {@code nodes}), 0 for none; the next free node in a free one. */
    left: Size
    /** The node with the larger keys, 0 for none. */
    right: Size
    /** Its level in the AA tree: 1 for a leaf, 0 for the empty node 0 and free nodes. */
    level: Size

/**
 * Finds values by their keys like a {@link Map}, but keeps the keys sorted: `for` goes through them from the smallest,
 * {@link first} and {@link last} give the ends, {@link floor} and {@link ceiling} the nearest keys, and {@link range}
 * the keys between two. Adding, finding and removing take about log2(n) steps.
 *
 *     val scores: SortedMap<String, Int> = sortedMap()
 *     scores.put("bob", 3)
 *     scores.put("ada", 5)
 *     for name, score in scores do                     // ada, then bob
 *         io.log("${name}: ${score}")
 *     if val e = scores.ceiling("b") do                // the first name from "b" on
 *         io.log("${e.first}: ${e.second}")            // bob: 3
 *
 * Numbers are sorted with `<` and Strings by their bytes ({@code compare}); give {@link sortedMap} a `less` function for
 * any other order, or for keys `<` can't compare (records).
 * @param <K> the type of the keys
 * @param <V> the type of the values
 */
mut ref rec SortedMap<K, V> as
    /** The nodes of the tree (an AA tree). Node 0 is the empty node: every missing child is 0. */
    nodes: TreeNode<K, V>[]
    /** How many of {@code nodes} were ever used, node 0 included. */
    used: Size
    /** The first node that was freed, to use again (0 for none); free nodes are chained through {@code left}. */
    free: Size
    /** The node at the root, 0 when it is empty. */
    root: Size
    /** How many keys it holds. */
    len: Size
    /** Whether its first key comes before its second. */
    less: (K, K) => Bool
    /** The pool its memory is in. */
    mem: Pool

/** The nodes of an empty tree, node 0 set to the empty node. */
priv fn treeNodes(capacity: Size, mem: Pool): TreeNode<K, V>[] do
    val nodes = unsafe new (TreeNode<K, V>)[capacity] in mem     // node 0's key and value are never read
    nodes[0].left = 0
    nodes[0].right = 0
    nodes[0].level = 0
    ret nodes

/** A new empty {@link SortedMap} whose keys are sorted with `<` (Strings by their bytes), in the current pool: `val m: SortedMap<Int, String> = sortedMap()`. */
fn sortedMap(): SortedMap<K, V> do
    val nodes: TreeNode<K, V>[] = treeNodes(8, pool)
    ret new SortedMap<K, V> { nodes, 1, 0, 0, 0, fn (a, b) = comesBefore(a, b), pool }

/**
 * A new empty {@link SortedMap} with the order `less` gives, in the current pool: `sortedMap(fn (a: Point, b: Point) = a.x < b.x)`.
 * @param less whether its first key comes before its second. Two keys neither of which comes before the other are the same key
 */
fn sortedMap(less: (<K>, <K>) => Bool): SortedMap<K, V> do
    val nodes: TreeNode<K, V>[] = treeNodes(8, pool)
    ret new SortedMap<K, V> { nodes, 1, 0, 0, 0, less, pool }

/** A new empty {@link SortedMap} whose keys are sorted with `<` (Strings by their bytes), in the pool `mem`. */
fn sortedMap(mem: Pool): SortedMap<K, V> do
    val nodes: TreeNode<K, V>[] = treeNodes(8, mem)
    ret new SortedMap<K, V> { nodes, 1, 0, 0, 0, fn (a, b) = comesBefore(a, b), mem } in mem

/** The node holding `key`, or 0 when the map doesn't have it. */
priv fn findNode(m: SortedMap<K, V>, key: <K>): Size do
    var t = m.root
    while t != 0 do
        if m.less(key, m.nodes[t].key) do
            t = m.nodes[t].left
        else if m.less(m.nodes[t].key, key) do
            t = m.nodes[t].right
        else do
            ret t
    ret 0

/** A node for `key` and `value`: a freed one, or one more of {@code nodes}, which grows when it is full. */
priv fn treeNewNode(m: SortedMap<K, V>, key: <K>, value: <V>): Size do
    var t = m.free
    if t != 0 do
        m.free = m.nodes[t].left
    else do
        if m.used == m.nodes.len do
            val grown: TreeNode<K, V>[] = treeNodes(m.nodes.len * 2, m.mem)
            for i in 1..<m.used do
                grown[i] = m.nodes[i]
            m.nodes = grown
        t = m.used
        m.used += 1
    m.nodes[t] = TreeNode<K, V> { key, value, 0, 0, 1 }
    m.len += 1
    ret t

/** Gives node `t` back, for {@link treeNewNode} to use again. */
priv fn treeFreeNode(m: SortedMap<K, V>, t: Size) do
    m.nodes[t].level = 0
    m.nodes[t].right = 0
    m.nodes[t].left = m.free
    m.free = t
    m.len -= 1

/** AA tree: turns a left child on the same level into the parent. @return the subtree's new root */
priv fn treeSkew(m: SortedMap<K, V>, t: Size): Size do
    ret t if t == 0
    val l = m.nodes[t].left
    ret t if l == 0 or m.nodes[l].level != m.nodes[t].level
    m.nodes[t].left = m.nodes[l].right
    m.nodes[l].right = t
    ret l

/** AA tree: lifts the middle of three nodes in a row on one level. @return the subtree's new root */
priv fn treeSplit(m: SortedMap<K, V>, t: Size): Size do
    ret t if t == 0
    val r = m.nodes[t].right
    ret t if r == 0 or m.nodes[m.nodes[r].right].level != m.nodes[t].level
    m.nodes[t].right = m.nodes[r].left
    m.nodes[r].left = t
    m.nodes[r].level += 1
    ret r

/** Puts `key` and `value` into the subtree at `t`. @return the subtree's new root */
priv fn treeInsert(m: SortedMap<K, V>, t: Size, key: <K>, value: <V>): Size do
    ret treeNewNode(m, key, value) if t == 0
    if m.less(key, m.nodes[t].key) do
        val l = treeInsert(m, m.nodes[t].left, key, value)
        m.nodes[t].left = l
    else if m.less(m.nodes[t].key, key) do
        val r = treeInsert(m, m.nodes[t].right, key, value)
        m.nodes[t].right = r
    else do
        m.nodes[t].value = value
        ret t
    ret treeSplit(m, treeSkew(m, t))

/** Takes `key` out of the subtree at `t`. @return the subtree's new root */
priv fn treeRemove(m: SortedMap<K, V>, t: Size, key: <K>): Size do
    ret 0 if t == 0
    if m.less(m.nodes[t].key, key) do
        val r = treeRemove(m, m.nodes[t].right, key)
        m.nodes[t].right = r
    else if m.less(key, m.nodes[t].key) do
        val l = treeRemove(m, m.nodes[t].left, key)
        m.nodes[t].left = l
    else if m.nodes[t].left == 0 and m.nodes[t].right == 0 do
        treeFreeNode(m, t)
        ret 0
    else if m.nodes[t].left == 0 do
        // The next key moves here, and its node goes.
        var s = m.nodes[t].right
        while m.nodes[s].left != 0 do
            s = m.nodes[s].left
        val nextKey = m.nodes[s].key
        val nextValue = m.nodes[s].value
        val r = treeRemove(m, m.nodes[t].right, nextKey)
        m.nodes[t].right = r
        m.nodes[t].key = nextKey
        m.nodes[t].value = nextValue
    else do
        var p = m.nodes[t].left
        while m.nodes[p].right != 0 do
            p = m.nodes[p].right
        val previousKey = m.nodes[p].key
        val previousValue = m.nodes[p].value
        val l = treeRemove(m, m.nodes[t].left, previousKey)
        m.nodes[t].left = l
        m.nodes[t].key = previousKey
        m.nodes[t].value = previousValue
    // Lower t's level when its children are too low, then rebalance.
    val l = m.nodes[t].left
    val r = m.nodes[t].right
    val lowest = math.min(m.nodes[l].level, m.nodes[r].level) + 1
    if lowest < m.nodes[t].level do
        m.nodes[t].level = lowest
        if lowest < m.nodes[r].level do
            m.nodes[r].level = lowest
    var top = treeSkew(m, t)
    val right = treeSkew(m, m.nodes[top].right)
    m.nodes[top].right = right
    if right != 0 do
        val further = treeSkew(m, m.nodes[right].right)
        m.nodes[right].right = further
    top = treeSplit(m, top)
    val splitRight = treeSplit(m, m.nodes[top].right)
    m.nodes[top].right = splitRight
    ret top

/** Sets the value of `key`, adding the key when the map doesn't have it. */
fn put(m: SortedMap<K, V>, key: <K>, value: <V>) do
    m.root = treeInsert(m, m.root, key, value)

/** The value of `key`, or null when the map doesn't have it. */
fn get(m: SortedMap<K, V>, key: <K>): <V>? do
    val t = findNode(m, key)
    ret null if t == 0
    ret m.nodes[t].value

/** Whether the map has `key`. */
fn has(m: SortedMap<K, V>, key: <K>): Bool = findNode(m, key) != 0

/** Takes `key` and its value away. @return whether the map had it */
fn remove(m: SortedMap<K, V>, key: <K>): Bool do
    ret false if findNode(m, key) == 0
    m.root = treeRemove(m, m.root, key)
    ret true

/** Takes every key away, and keeps the room they took. */
fn clear(m: SortedMap<K, V>) do
    m.root = 0
    m.used = 1
    m.free = 0
    m.len = 0

/** Whether the map has no keys. */
fn isEmpty(m: SortedMap<K, V>): Bool = m.len == 0

/** How many keys it holds. */
fn size(m: SortedMap<K, V>): Size = m.len

/** The node with the smallest key, 0 when the map is empty. */
priv fn lowestNode(m: SortedMap<K, V>): Size do
    var t = m.root
    while t != 0 and m.nodes[t].left != 0 do
        t = m.nodes[t].left
    ret t

/** The node with the largest key, 0 when the map is empty. */
priv fn highestNode(m: SortedMap<K, V>): Size do
    var t = m.root
    while t != 0 and m.nodes[t].right != 0 do
        t = m.nodes[t].right
    ret t

/** The node with the smallest key after `key` (or equal to it, when `orEqual`), 0 when there is none. */
priv fn nodeAbove(m: SortedMap<K, V>, key: <K>, orEqual: Bool): Size do
    var t = m.root
    var best: Size = 0
    while t != 0 do
        if m.less(key, m.nodes[t].key) or (orEqual and not m.less(m.nodes[t].key, key)) do
            best = t
            t = m.nodes[t].left
        else do
            t = m.nodes[t].right
    ret best

/** The node with the largest key before `key` (or equal to it, when `orEqual`), 0 when there is none. */
priv fn nodeBelow(m: SortedMap<K, V>, key: <K>, orEqual: Bool): Size do
    var t = m.root
    var best: Size = 0
    while t != 0 do
        if m.less(m.nodes[t].key, key) or (orEqual and not m.less(key, m.nodes[t].key)) do
            best = t
            t = m.nodes[t].right
        else do
            t = m.nodes[t].left
    ret best

/** The key and value of node `t`, or null for 0. */
priv fn entryOf(m: SortedMap<K, V>, t: Size): (<K>, <V>)? do
    ret null if t == 0
    ret (m.nodes[t].key, m.nodes[t].value)

/** The smallest key and its value, or null when the map is empty. */
fn first(m: SortedMap<K, V>): (<K>, <V>)? = entryOf(m, lowestNode(m))

/** The largest key and its value, or null when the map is empty. */
fn last(m: SortedMap<K, V>): (<K>, <V>)? = entryOf(m, highestNode(m))

/** The largest key that is `key` or before it, and its value, or null when there is none. */
fn floor(m: SortedMap<K, V>, key: <K>): (<K>, <V>)? = entryOf(m, nodeBelow(m, key, true))

/** The smallest key that is `key` or after it, and its value, or null when there is none. */
fn ceiling(m: SortedMap<K, V>, key: <K>): (<K>, <V>)? = entryOf(m, nodeAbove(m, key, true))

/** The largest key before `key`, and its value, or null when there is none. */
fn lower(m: SortedMap<K, V>, key: <K>): (<K>, <V>)? = entryOf(m, nodeBelow(m, key, false))

/** The smallest key after `key`, and its value, or null when there is none. */
fn higher(m: SortedMap<K, V>, key: <K>): (<K>, <V>)? = entryOf(m, nodeAbove(m, key, false))

/** Takes away the smallest key. @return it and its value, or null when the map is empty */
fn popFirst(m: SortedMap<K, V>): (<K>, <V>)? do
    val t = lowestNode(m)
    ret null if t == 0
    val key = m.nodes[t].key
    val value = m.nodes[t].value
    m.root = treeRemove(m, m.root, key)
    ret (key, value)

/** Takes away the largest key. @return it and its value, or null when the map is empty */
fn popLast(m: SortedMap<K, V>): (<K>, <V>)? do
    val t = highestNode(m)
    ret null if t == 0
    val key = m.nodes[t].key
    val value = m.nodes[t].value
    m.root = treeRemove(m, m.root, key)
    ret (key, value)

/** Goes through the keys and values of a {@link SortedMap} in order, for `for`. */
mut rec SortedMapIter<K, V> as
    /** The map. */
    tree: SortedMap<K, V>
    /** The key given last, null before the first. */
    after: K?
    /** The first key to give, or one after it; null for the smallest. */
    from: K?
    /** The key to stop before; null to go to the end. */
    until: K?

/**
 * What `for key, value in m do` loops over: each key and its value, from the smallest key. Each step looks for the key after
 * the one before, so keys can be added, changed and removed during the loop: a key added after the current one is part of it.
 */
fn pairs(m: SortedMap<K, V>): SortedMapIter<K, V> = SortedMapIter<K, V> { m, null, null, null }

/** What `for (key, value) in m do` loops over: the same as {@link pairs}. */
fn iter(m: SortedMap<K, V>): SortedMapIter<K, V> = pairs(m)

/** The keys from `from` up to `until`, but not `until`, and their values, for `for (key, value) in m.range(10, 20) do`. */
fn range(m: SortedMap<K, V>, from: <K>, until: <K>): SortedMapIter<K, V> = SortedMapIter<K, V> { m, null, from, until }

/** The keys from `from` to the end and their values, for `for (key, value) in m.rangeFrom(10) do`. */
fn rangeFrom(m: SortedMap<K, V>, from: <K>): SortedMapIter<K, V> = SortedMapIter<K, V> { m, null, from, null }

/** The keys before `until` and their values, for `for (key, value) in m.rangeUntil(10) do`. */
fn rangeUntil(m: SortedMap<K, V>, until: <K>): SortedMapIter<K, V> = SortedMapIter<K, V> { m, null, null, until }

/** The next key and its value, or null after the last. */
fn next(mut i: SortedMapIter<K, V>): (<K>, <V>)? do
    var t: Size = 0
    if val after = i.after do
        t = nodeAbove(i.tree, after, false)
    else if val from = i.from do
        t = nodeAbove(i.tree, from, true)
    else do
        t = lowestNode(i.tree)
    ret null if t == 0
    val key = i.tree.nodes[t].key
    if val until = i.until do
        ret null if not i.tree.less(key, until)
    i.after = key
    ret (key, i.tree.nodes[t].value)

/** A new array of the keys, from the smallest, in the current pool. */
fn keys(m: SortedMap<K, V>): <K>[] do
    val out = unsafe new (<K>)[m.len]
    var n: Size = 0
    for (k, _) in pairs(m) do
        out[n] = k
        n += 1
    ret out

/** A new array of the values, in the order of their keys, in the current pool. */
fn values(m: SortedMap<K, V>): <V>[] do
    val out = unsafe new (<V>)[m.len]
    var n: Size = 0
    for (_, v) in pairs(m) do
        out[n] = v
        n += 1
    ret out

/** Combines the keys and values into one value, from the smallest key. */
fn fold(m: SortedMap<K, V>, initial: <U>, f: (<U>, <K>, <V>) => <U>): <U> do
    var result = initial
    for (k, v) in pairs(m) do
        result = f(result, k, v)
    ret result

// ---------------------------------------------------------------------------------------------------
// SortedSet

/**
 * Holds each value at most once, like a {@link Set}, but keeps them sorted: `for` goes through them from the smallest, and
 * {@link floor}, {@link ceiling} and {@link range} find the values near one. Adding, finding and removing take about log2(n) steps.
 *
 *     val s: SortedSet<Int> = sortedSetOf(5, 1, 3)
 *     io.log(s.first() ?? 0)                           // 1
 *     io.log(s.ceiling(2) ?? 0)                        // 3
 *
 * @param <T> the type of the values, sorted with `<` (Strings by their bytes) or the `less` given to {@link sortedSet}
 */
ref rec SortedSet<T> as
    /** The values, as keys. */
    tree: SortedMap<T, Bool>

/** A new empty {@link SortedSet} whose values are sorted with `<` (Strings by their bytes), in the current pool. */
fn sortedSet(): SortedSet<T> = new SortedSet<T> { sortedMap() }

/** A new empty {@link SortedSet} with the order `less` gives, in the current pool. */
fn sortedSet(less: (<T>, <T>) => Bool): SortedSet<T> = new SortedSet<T> { sortedMap(less) }

/** A new {@link SortedSet} of the `values`, sorted with `<` (Strings by their bytes), in the current pool: `sortedSetOf(3, 1, 2)`. */
fn sortedSetOf(...values: <T>): SortedSet<T> do
    val s: SortedSet<T> = sortedSet()
    for v in values do
        add(s, v)
    ret s

/** Adds `value`. @return whether it was new */
fn add(s: SortedSet<T>, value: <T>): Bool do
    ret false if has(s.tree, value)
    put(s.tree, value, true)
    ret true

/** Whether the set has `value`. */
fn has(s: SortedSet<T>, value: <T>): Bool = has(s.tree, value)

/** Takes `value` away. @return whether the set had it */
fn remove(s: SortedSet<T>, value: <T>): Bool = remove(s.tree, value)

/** Takes every value away. */
fn clear(s: SortedSet<T>) do
    clear(s.tree)

/** Whether the set has no values. */
fn isEmpty(s: SortedSet<T>): Bool = s.tree.len == 0

/** How many values it holds. */
fn size(s: SortedSet<T>): Size = s.tree.len

/** The key of an entry of a SortedMap, or null. */
priv fn keyOf(entry: (<T>, Bool)?): <T>? do
    if val e = entry do
        ret e.first
    ret null

/** The smallest value, or null when the set is empty. */
fn first(s: SortedSet<T>): <T>? = keyOf(first(s.tree))

/** The largest value, or null when the set is empty. */
fn last(s: SortedSet<T>): <T>? = keyOf(last(s.tree))

/** The largest value that is `value` or before it, or null when there is none. */
fn floor(s: SortedSet<T>, value: <T>): <T>? = keyOf(floor(s.tree, value))

/** The smallest value that is `value` or after it, or null when there is none. */
fn ceiling(s: SortedSet<T>, value: <T>): <T>? = keyOf(ceiling(s.tree, value))

/** The largest value before `value`, or null when there is none. */
fn lower(s: SortedSet<T>, value: <T>): <T>? = keyOf(lower(s.tree, value))

/** The smallest value after `value`, or null when there is none. */
fn higher(s: SortedSet<T>, value: <T>): <T>? = keyOf(higher(s.tree, value))

/** Takes away the smallest value. @return it, or null when the set is empty */
fn popFirst(s: SortedSet<T>): <T>? = keyOf(popFirst(s.tree))

/** Takes away the largest value. @return it, or null when the set is empty */
fn popLast(s: SortedSet<T>): <T>? = keyOf(popLast(s.tree))

/** A new array of the values, from the smallest, in the current pool. */
fn toArray(s: SortedSet<T>): <T>[] = keys(s.tree)

/** Goes through the values of a {@link SortedSet} in order, for `for`. */
mut rec SortedSetIter<T> as
    /** The keys are the values. */
    keys: SortedMapIter<T, Bool>

/** What `for x in s do` loops over: the values, from the smallest. Values can be added and removed during the loop, as in a {@link SortedMap}. */
fn iter(s: SortedSet<T>): SortedSetIter<T> = SortedSetIter<T> { pairs(s.tree) }

/** The values from `from` up to `until`, but not `until`, for `for x in s.range(10, 20) do`. */
fn range(s: SortedSet<T>, from: <T>, until: <T>): SortedSetIter<T> = SortedSetIter<T> { range(s.tree, from, until) }

/** The values from `from` to the end, for `for x in s.rangeFrom(10) do`. */
fn rangeFrom(s: SortedSet<T>, from: <T>): SortedSetIter<T> = SortedSetIter<T> { rangeFrom(s.tree, from) }

/** The values before `until`, for `for x in s.rangeUntil(10) do`. */
fn rangeUntil(s: SortedSet<T>, until: <T>): SortedSetIter<T> = SortedSetIter<T> { rangeUntil(s.tree, until) }

/** The next value, or null after the last. */
fn next(mut i: SortedSetIter<T>): <T>? = keyOf(next(mut i.keys))

// ---------------------------------------------------------------------------------------------------
// BitSet

/**
 * A set of small numbers (from 0), one bit each: a set of a million numbers takes 125 KB. It grows to hold the largest number added.
 *
 *     val seen = bitSet()
 *     seen.add(3)
 *     seen.add(64)
 *     io.log(seen.has(3))                             // true
 *     for i in seen do                                // 3, then 64
 *         io.log(i)
 */
mut ref rec BitSet as
    /** The bits, 64 to a word: number i is bit i % 64 of word i / 64. */
    words: ULong[]
    /** The pool its memory is in. */
    mem: Pool

/**
 * A new empty {@link BitSet}, in the current pool.
 * @param capacity the numbers it has room for (from 0) before it grows
 */
fn bitSet(capacity: Size = 64): BitSet do
    val words: Size = if capacity == 0 do 1 else (capacity + 63) / 64
    ret new BitSet { new ULong[words], pool }

/** A new {@link BitSet} of the `values`, in the current pool: `bitSetOf(1, 5, 9)`. */
fn bitSetOf(...values: Size): BitSet do
    val b = bitSet()
    for v in values do
        add(b, v)
    ret b

/** The bit of number `i` in its word. */
priv fn bitOf(i: Size): ULong = 1ul << (i % 64)

/** Adds the number `i`. @return whether it was new */
fn add(b: BitSet, i: Size): Bool do
    val word = i / 64
    if word >= b.words.len do
        var capacity = b.words.len * 2
        while capacity <= word do
            capacity *= 2
        val grown = new ULong[capacity] in b.mem
        for w in 0..<b.words.len do
            grown[w] = b.words[w]
        b.words = grown
    ret false if b.words[word] & bitOf(i) != 0
    b.words[word] |= bitOf(i)
    ret true

/** Whether the set has the number `i`. */
fn has(b: BitSet, i: Size): Bool = i / 64 < b.words.len and b.words[i / 64] & bitOf(i) != 0

/** Takes the number `i` away. @return whether the set had it */
fn remove(b: BitSet, i: Size): Bool do
    ret false if not has(b, i)
    b.words[i / 64] &= ~bitOf(i)
    ret true

/** Takes every number away, and keeps the room they took. */
fn clear(b: BitSet) do
    for w in 0..<b.words.len do
        b.words[w] = 0

/** How many numbers it holds. */
fn size(b: BitSet): Size do
    var n: Size = 0
    for w in b.words do
        var bits = w
        while bits != 0 do
            bits &= bits - 1
            n += 1
    ret n

/** Whether the set has no numbers. */
fn isEmpty(b: BitSet): Bool do
    for w in b.words where w != 0 do
        ret false
    ret true

/** The smallest number from `from` on that the set has, or null when there is none. */
fn nextSet(b: BitSet, from: Size): Size? do
    var word = from / 64
    ret null if word >= b.words.len
    var bits = b.words[word] & ~(bitOf(from) - 1)
    while true do
        if bits != 0 do
            var i = word * 64
            while bits & 1 == 0 do
                bits >>= 1
                i += 1
            ret i
        word += 1
        ret null if word >= b.words.len
        bits = b.words[word]

/** Goes through the numbers of a {@link BitSet}, for `for`. */
mut rec BitSetIter as
    /** The set. */
    bits: BitSet
    /** The number to look at next. */
    at: Size

/** What `for i in b do` loops over: the numbers, from the smallest. */
fn iter(b: BitSet): BitSetIter = BitSetIter { b, 0 }

/** The next number, or null after the last. */
fn next(mut i: BitSetIter): Size? do
    val found = nextSet(i.bits, i.at) ?? ret null
    i.at = found + 1
    ret found

/** A new array of the numbers, from the smallest, in the current pool. */
fn toArray(b: BitSet): Size[] do
    val out = new Size[size(b)]
    var n: Size = 0
    for i in b do
        out[n] = i
        n += 1
    ret out

/** A new {@link BitSet} holding the words `f` makes of those of `a` and `b`, in the current pool. */
priv fn combineBits(a: BitSet, b: BitSet, f: (ULong, ULong) => ULong): BitSet do
    val n = math.max(a.words.len, b.words.len)
    val out = new BitSet { new ULong[n], pool }
    for w in 0..<n do
        val x = if w < a.words.len do a.words[w] else 0
        val y = if w < b.words.len do b.words[w] else 0
        out.words[w] = f(x, y)
    ret out

/** A new {@link BitSet} of the numbers in `a` or `b`, in the current pool. */
fn union(a: BitSet, b: BitSet): BitSet = combineBits(a, b, fn (x, y) = x | y)

/** A new {@link BitSet} of the numbers in both `a` and `b`, in the current pool. */
fn intersection(a: BitSet, b: BitSet): BitSet = combineBits(a, b, fn (x, y) = x & y)

/** A new {@link BitSet} of the numbers in `a` that aren't in `b`, in the current pool. */
fn difference(a: BitSet, b: BitSet): BitSet = combineBits(a, b, fn (x, y) = x & ~y)

// ---------------------------------------------------------------------------------------------------
// LruCache

/** A key and its value in an {@link LruCache}, and its neighbours in the order of use. */
mut rec LruEntry<K, V> as
    /** The key. */
    key: K
    /** Its value. */
    value: V
    /** The entry used just after this one (an index in {@code entries}); 0 for none. */
    newer: Size
    /** The entry used just before this one; 0 for none. In a free entry, the next free one. */
    older: Size

/**
 * A {@link Map} with room for a fixed number of keys: when it is full, adding a key drops the one used least recently. Reading
 * with {@link get} and writing with {@link put} count as a use; {@link peek} doesn't.
 *
 *     val recent: LruCache<String, Int> = lruCache(2)
 *     recent.put("a", 1)
 *     recent.put("b", 2)
 *     recent.get("a")                                 // a was used after b
 *     recent.put("c", 3)                              // drops b
 *     io.log(recent.has("b"))                         // false
 *
 * @param <K> the type of the keys, which need `hash(key)` and `==`, as in a Map
 * @param <V> the type of the values
 */
mut ref rec LruCache<K, V> as
    /** The entries, and room for the rest. Entry 0 is none: its {@code newer} is the oldest entry and its {@code older} the newest. */
    entries: LruEntry<K, V>[]
    /** Where each key is in {@code entries}. */
    index: Map<K, Size>
    /** How many of {@code entries} were ever used, entry 0 included. */
    used: Size
    /** The first entry that was removed, to use again (0 for none), chained through {@code older}. */
    free: Size
    /** How many keys it holds at most. */
    capacity: Size

/**
 * A new empty {@link LruCache}, in the current pool: `val c: LruCache<String, Page> = lruCache(100)`.
 * @param capacity how many keys it holds at most, at least 1; the program stops otherwise
 */
fn lruCache(capacity: Size): LruCache<K, V> do
    assert capacity > 0, "an LruCache needs room for at least one key"
    val entries = unsafe new (LruEntry<K, V>)[capacity + 1]     // entry 0's key and value are never read
    entries[0].newer = 0
    entries[0].older = 0
    ret new LruCache<K, V> { entries, map(), 1, 0, capacity }

/** Takes entry `e` out of the order of use. */
priv fn lruUnlink(c: LruCache<K, V>, e: Size) do
    val newer = c.entries[e].newer
    val older = c.entries[e].older
    c.entries[newer].older = older
    c.entries[older].newer = newer

/** Puts entry `e` first in the order of use. */
priv fn lruMakeNewest(c: LruCache<K, V>, e: Size) do
    val newest = c.entries[0].older
    c.entries[e].older = newest
    c.entries[e].newer = 0
    c.entries[newest].newer = e
    c.entries[0].older = e

/** The value of `key`, which counts as a use, or null when the cache doesn't have it. */
fn get(c: LruCache<K, V>, key: <K>): <V>? do
    val e = get(c.index, key) ?? ret null
    lruUnlink(c, e)
    lruMakeNewest(c, e)
    ret c.entries[e].value

/** The value of `key`, without counting as a use, or null when the cache doesn't have it. */
fn peek(c: LruCache<K, V>, key: <K>): <V>? do
    val e = get(c.index, key) ?? ret null
    ret c.entries[e].value

/** Whether the cache has `key`; that doesn't count as a use. */
fn has(c: LruCache<K, V>, key: <K>): Bool = has(c.index, key)

/**
 * Sets the value of `key`, which counts as a use, adding the key when the cache doesn't have it.
 * @return the key and value it dropped to make room, or null when it dropped none
 */
fn put(c: LruCache<K, V>, key: <K>, value: <V>): (<K>, <V>)? do
    if val e = get(c.index, key) do
        c.entries[e].value = value
        lruUnlink(c, e)
        lruMakeNewest(c, e)
        ret null
    var dropped: (<K>, <V>)? = null
    var e: Size = 0
    if c.free != 0 do
        e = c.free
        c.free = c.entries[e].older
    else if c.used <= c.capacity do
        e = c.used
        c.used += 1
    else do
        e = c.entries[0].newer
        dropped = (c.entries[e].key, c.entries[e].value)
        remove(c.index, c.entries[e].key)
        lruUnlink(c, e)
    c.entries[e].key = key
    c.entries[e].value = value
    lruMakeNewest(c, e)
    put(c.index, key, e)
    ret dropped

/** Takes `key` and its value away. @return whether the cache had it */
fn remove(c: LruCache<K, V>, key: <K>): Bool do
    val e = get(c.index, key) ?? ret false
    remove(c.index, key)
    lruUnlink(c, e)
    c.entries[e].older = c.free
    c.free = e
    ret true

/** Takes every key away. */
fn clear(c: LruCache<K, V>) do
    clear(c.index)
    c.entries[0].newer = 0
    c.entries[0].older = 0
    c.used = 1
    c.free = 0

/** Whether the cache has no keys. */
fn isEmpty(c: LruCache<K, V>): Bool = c.index.len == 0

/** How many keys it holds. */
fn size(c: LruCache<K, V>): Size = c.index.len

/** Goes through the keys and values of an {@link LruCache}, for `for`. */
mut rec LruIter<K, V> as
    /** The cache. */
    cache: LruCache<K, V>
    /** The entry to give next, 0 after the last. */
    at: Size

/** What `for key, value in c do` loops over: each key and its value, from the one used most recently. It doesn't count as a use. Don't add or remove keys during the loop. */
fn pairs(c: LruCache<K, V>): LruIter<K, V> = LruIter<K, V> { c, c.entries[0].older }

/** What `for (key, value) in c do` loops over: the same as {@link pairs}. */
fn iter(c: LruCache<K, V>): LruIter<K, V> = pairs(c)

/** The next key and its value, or null after the last. */
fn next(mut i: LruIter<K, V>): (<K>, <V>)? do
    ret null if i.at == 0
    val e = i.at
    i.at = i.cache.entries[e].older
    ret (i.cache.entries[e].key, i.cache.entries[e].value)
