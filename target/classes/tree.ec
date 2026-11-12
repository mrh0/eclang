/**
 * SortedMap<K, V> and SortedSet<T>, which keep their keys sorted in a balanced tree (an AA tree), and find the keys
 * near one and ranges of them.
 *
 *     use "core:tree.ec"
 *     val scores: SortedMap<Int, String> = sortedMap()
 *     scores.put(70, "Ada")
 *     for score, name in scores.range(50, 100) do
 *         io.log("${name}: ${score}")
 *
 * Keys are compared with `<` (Strings by their bytes), or with a `less` function given to {@link sortedMap}.
 *
 * The functions are top-level, so they can be called on a structure. Its memory is in the pool that was current when it
 * was made (or the pool given), and it grows there. What it holds isn't copied: Strings and pointers put in it must live at
 * least as long as it does, which the compiler checks. core:structures.ec has every structure at once.
 */
use "core:errors.ec"
use "core:string.ec"
use "core:math.ec"
use "core:list.ec"

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

/** A new {@link List} of `f` of each key and its value, from the smallest key, in the current pool. */
fn map(m: SortedMap<K, V>, f: (<K>, <V>) => <U>): List<U> do
    val out: List<U> = list(m.len)
    for (k, v) in pairs(m) do
        push(out, f(k, v))
    ret out

/** A new {@link SortedMap} of the same keys in the same order, each with `f` of its value, in the current pool. */
fn mapValues(m: SortedMap<K, V>, f: (<V>) => <U>): SortedMap<K, U> do
    val out: SortedMap<K, U> = sortedMap(m.less)
    for (k, v) in pairs(m) do
        put(out, k, f(v))
    ret out

/** A new {@link SortedMap} of the keys and values `test` is true for, with the same order, in the current pool. */
fn filter(m: SortedMap<K, V>, test: (<K>, <V>) => Bool): SortedMap<K, V> do
    val out: SortedMap<K, V> = sortedMap(m.less)
    for (k, v) in pairs(m) where test(k, v) do
        put(out, k, v)
    ret out

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
