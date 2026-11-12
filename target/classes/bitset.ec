/**
 * BitSet, a set of small numbers (from 0), one bit each.
 *
 *     use "core:bitset.ec"
 *     val seen = bitSet()
 *     seen.add(3)
 *     io.log(seen.has(3))                             // true
 */
use "core:errors.ec"
use "core:string.ec"
use "core:math.ec"

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
