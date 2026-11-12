/**
 * Map<K, V>, a hash table that keeps its keys in the order they were added, the `hash` functions it uses, and
 * `groupBy`, `associate` and the like, which make a Map of an array.
 *
 *     use "core:map.ec"
 *     val ages: Map<String, Int> = map()
 *     ages.put("Ada", 36)
 *     io.log(ages.get("Ada") ?? 0)                    // 36
 *     for name, age in ages do
 *         io.log("${name}: ${age}")
 *
 * Keys are found with `hash(key)` and `==`. Anything `==` can compare works as it is: the compiler generates a `hash` for
 * records and tuples. For one of your own, define `fn hash(p: Point): ULong` at the top level of a file.
 *
 * The functions are top-level, so they can be called on a structure. Its memory is in the pool that was current when it
 * was made (or the pool given), and it grows there. What it holds isn't copied: Strings and pointers put in it must live at
 * least as long as it does, which the compiler checks. core:structures.ec has every structure at once.
 */
use "core:errors.ec"
use "core:string.ec"
use "core:math.ec"
use "core:list.ec"

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
