/**
 * Data structures: List, Map, Set, Deque, Heap and SlotPool. They are generic, so each holds one type of element.
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
 * The type on the left says what a new structure holds, as `list()` itself has no element to tell. The functions
 * are top-level, so they can be called on a structure. Its memory is in the pool that was current when it was made (or
 * the pool given), and it grows there. What it holds isn't copied: Strings and pointers put in it must live at least
 * as long as it does, which the compiler checks: `l.push("${i}")` in a `pool do` block is an error when l is from outside it.
 *
 * Map and Set find keys with `hash(key)` and `==`. Anything `==` can compare works as it is: the compiler generates a
 * `hash` for records and tuples. For one of your own, define `fn hash(p: Point): ULong` at the top level of a file. See core:array.ec for sorting and searching: `numbers.items().sort()`.
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
