/**
 * Algorithms on arrays of any type: searching, sorting, and map, filter and reduce.
 *
 *     use "core:array.ec"
 *     val numbers = clone([3, 1, 2])
 *     numbers.sort()                                       // [1, 2, 3], in place
 *     val squares = numbers.map(fn (x) = x * x)            // [1, 4, 9]
 *     val odd = numbers.filter(fn (x) = x % 2 == 1)        // [1, 3]
 *     val total = numbers.reduce(0, fn (sum, x) = sum + x) // 6
 *
 * The functions are top-level, so they can be called on an array. Those that return a new array allocate it in the
 * current pool. An array literal lives on the stack, so `clone` it before sorting it, or to keep it. See
 * core:structures.ec for lists, maps and sets.
 */
use "core:errors.ec"
use "core:string.ec"

// ---------------------------------------------------------------------------------------------------
// Searching

/** Whether `value` is in `values`, compared with `==`. */
fn contains(values: <T>[], value: <T>): Bool do
    for v in values where v == value do
        ret true
    ret false

/** The index of the first `value` in `values`, or null when there is none. */
fn indexOf(values: <T>[], value: <T>): Size? do
    for i, v in values where v == value do
        ret i
    ret null

/** The index of the last `value` in `values`, or null when there is none. */
fn lastIndexOf(values: <T>[], value: <T>): Size? do
    var i = values.len
    while i > 0 do
        i -= 1
        ret i if values[i] == value
    ret null

/** How many elements are equal to `value`. */
fn count(values: <T>[], value: <T>): Size do
    var n: Size = 0
    for v in values where v == value do
        n += 1
    ret n

/** The first element `test` is true for, or null when there is none. */
fn find(values: <T>[], test: (<T>) => Bool): <T>? do
    for v in values where test(v) do
        ret v
    ret null

/** The index of the first element `test` is true for, or null when there is none. */
fn findIndex(values: <T>[], test: (<T>) => Bool): Size? do
    for i, v in values where test(v) do
        ret i
    ret null

/** Whether `test` is true for at least one element. */
fn any(values: <T>[], test: (<T>) => Bool): Bool do
    for v in values where test(v) do
        ret true
    ret false

/** Whether `test` is true for every element (so true for an empty array). */
fn all(values: <T>[], test: (<T>) => Bool): Bool do
    for v in values where not test(v) do
        ret false
    ret true

/**
 * Finds `value` in a sorted array, in about log2(n) steps.
 * @param values sorted from small to large, as {@link sort} sorts
 * @return the index of an element equal to `value`, or null when there is none
 */
fn binarySearch(values: <T>[], value: <T>): Size? do
    var low: Size = 0
    var high = values.len
    while low < high do
        val middle = low + (high - low) / 2
        if isLess(values[middle], value) do
            low = middle + 1
        else if isLess(value, values[middle]) do
            high = middle
        else do
            ret middle
    ret null

// ---------------------------------------------------------------------------------------------------
// New arrays

/** A new array of `f` of each element: `[1, 2].map(fn (x) = x * 2)` is [2, 4]. */
fn map(values: <T>[], f: (<T>) => <U>): <U>[] do
    val out = new (<U>)[values.len]
    for i, v in values do
        out[i] = f(v)
    ret out

/** A new array of the elements `test` is true for, in the same order. */
fn filter(values: <T>[], test: (<T>) => Bool): <T>[] do
    val out = new (<T>)[values.len]
    var n: Size = 0
    for v in values where test(v) do
        out[n] = v
        n += 1
    ret out[0..<n]

/**
 * Combines the elements into one value, from the first to the last: `[1, 2, 3].reduce(0, fn (sum, x) = sum + x)` is 6.
 * @param initial the value to start from, and the result for an empty array
 * @param f makes the next value from the value so far and an element
 */
fn reduce(values: <T>[], initial: <U>, f: (<U>, <T>) => <U>): <U> do
    var result = initial
    for v in values do
        result = f(result, v)
    ret result

/** A new array with the same elements: not a deep copy, as {@code clone} makes. */
fn copyOf(values: <T>[]): <T>[] do
    val out = new (<T>)[values.len]
    for i, v in values do
        out[i] = v
    ret out

/** A new array of the elements of `a`, then those of `b`. */
fn joined(a: <T>[], b: <T>[]): <T>[] do
    val out = new (<T>)[a.len + b.len]
    for i, v in a do
        out[i] = v
    for i, v in b do
        out[a.len + i] = v
    ret out

/** A new array of the elements, without repeats: the first of each stays. It compares every pair, so it suits short arrays; see {@code Set} for long ones. */
fn distinct(values: <T>[]): <T>[] do
    val out = new (<T>)[values.len]
    var n: Size = 0
    for v in values do
        if not contains(out[0..<n], v) do
            out[n] = v
            n += 1
    ret out[0..<n]

/** The first `n` elements (all of them when there are fewer): a slice, sharing the elements of `values`. */
fn take(values: <T>[], n: Size): <T>[] = if n >= values.len do values else values[0..<n]

/** The elements after the first `n`: a slice, sharing the elements of `values`. */
fn drop(values: <T>[], n: Size): <T>[] = if n >= values.len do values[0..<0] else values[n..<values.len]

// ---------------------------------------------------------------------------------------------------
// In place

/** Sets every element to `value`. */
fn fill(values: <T>[], value: <T>) do
    for i in 0..<values.len do
        values[i] = value

/** Swaps the elements at `i` and `j`. */
fn swap(values: <T>[], i: Size, j: Size) do
    val t = values[i]
    values[i] = values[j]
    values[j] = t

/** Puts the elements in the opposite order. */
fn reverse(values: <T>[]) do
    var i: Size = 0
    var j = values.len
    while i + 1 < j do
        j -= 1
        swap(values, i, j)
        i += 1

/** Whether `a` comes before `b`: `<`, or {@link compare} for Strings. */
priv fn isLess(a: <T>, b: <T>): Bool = a < b

/** Whether `a` comes before `b`, byte by byte. */
priv fn isLess(a: String, b: String): Bool = compare(a, b) < 0

/** Sorts in place from small to large, with `<` (Strings byte by byte). Equal elements keep their order. See {@link sortBy}. */
fn sort(values: <T>[]) do
    sortBy(values, fn (a, b) = isLess(a, b))

/**
 * Sorts in place, in O(n log n). Equal elements keep their order (a merge sort).
 * @param less whether its first argument must come before its second
 */
fn sortBy(values: <T>[], less: (<T>, <T>) => Bool) do
    if values.len >= 2 do
        mergeSort(values, new (<T>)[values.len], less)

/** Sorts `values`, with `scratch` (as long as it) to merge in. */
priv fn mergeSort(values: <T>[], scratch: <T>[], less: (<T>, <T>) => Bool) do
    val n = values.len
    if n <= 8 do
        insertionSort(values, less)
    else do
        val middle = n / 2
        mergeSort(values[0..<middle], scratch[0..<middle], less)
        mergeSort(values[middle..<n], scratch[middle..<n], less)
        merge(values, scratch, middle, less)

/** Sorts `values`, a few elements, one by one. */
priv fn insertionSort(values: <T>[], less: (<T>, <T>) => Bool) do
    for i in 1..<values.len do
        val item = values[i]
        var j = i
        while j > 0 and less(item, values[j - 1]) do
            values[j] = values[j - 1]
            j -= 1
        values[j] = item

/** Merges the sorted parts of `values` before and after `middle`, with the left part copied to `scratch`. */
priv fn merge(values: <T>[], scratch: <T>[], middle: Size, less: (<T>, <T>) => Bool) do
    for i in 0..<middle do
        scratch[i] = values[i]
    var left: Size = 0
    var right = middle
    var at: Size = 0
    while left < middle and right < values.len do
        // On a tie the left element goes first, which keeps equal elements in order.
        if less(values[right], scratch[left]) do
            values[at] = values[right]
            right += 1
        else do
            values[at] = scratch[left]
            left += 1
        at += 1
    while left < middle do
        values[at] = scratch[left]
        left += 1
        at += 1

/** Whether the elements are sorted from small to large, as {@link sort} sorts. */
fn isSorted(values: <T>[]): Bool do
    for i in 1..<values.len where isLess(values[i], values[i - 1]) do
        ret false
    ret true
