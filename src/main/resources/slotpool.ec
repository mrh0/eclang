/**
 * SlotPool<T>, values reached by a SlotKey instead of a pointer, for lifetimes that don't nest.
 *
 *     use "core:slotpool.ec"
 *     val nodes: SlotPool<Node> = slotPool()
 *     val a = nodes.add(Node { "a" })
 *     io.log(nodes.get(a) != null)                    // true
 *
 * The functions are top-level, so they can be called on a structure. Its memory is in the pool that was current when it
 * was made (or the pool given), and it grows there. What it holds isn't copied: Strings and pointers put in it must live at
 * least as long as it does, which the compiler checks. core:structures.ec has every structure at once.
 */
use "core:errors.ec"
use "core:string.ec"
use "core:list.ec"

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
