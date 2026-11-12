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
 *
 * This file uses all of them; each also has a file of its own: core:list.ec, core:map.ec (with `hash`, `groupBy` and
 * `associate`), core:set.ec, core:deque.ec, core:heap.ec, core:slotpool.ec, core:tree.ec (SortedMap and SortedSet),
 * core:bitset.ec and core:lru.ec (LruCache). core:ringbuffer.ec, core:counter.ec, core:grid.ec and core:disjointset.ec
 * aren't part of it, so that a program using it can still name its own types Grid or Counter: `use` them by themselves.
 */
use "core:list.ec"
use "core:map.ec"
use "core:set.ec"
use "core:deque.ec"
use "core:heap.ec"
use "core:slotpool.ec"
use "core:tree.ec"
use "core:bitset.ec"
use "core:lru.ec"
