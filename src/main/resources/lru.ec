/**
 * LruCache<K, V>, a Map with room for a fixed number of keys, which drops the one used least recently.
 *
 *     use "core:lru.ec"
 *     val recent: LruCache<String, Int> = lruCache(100)
 *     recent.put("a", 1)
 *     io.log(recent.get("a") ?? 0)                    // 1
 *
 * The functions are top-level, so they can be called on a structure. Its memory is in the pool that was current when it
 * was made (or the pool given), and it grows there. What it holds isn't copied: Strings and pointers put in it must live at
 * least as long as it does, which the compiler checks. core:structures.ec has every structure at once.
 */
use "core:errors.ec"
use "core:string.ec"
use "core:map.ec"

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
