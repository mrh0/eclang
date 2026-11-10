/**
 * Runs a function on the elements of an array on several threads at once, and waits until it is done: `parallel.map` is
 * `map` (core:array.ec) on as many threads as there are processors.
 *
 *     use "core:parallel.ec"
 *     val squares = parallel.map(numbers, fn (x) = x * x)        // in order, as `numbers.map(...)` would give them
 *     parallel.each(files, fn (f) = check(f))
 *     parallel.times(100, fn (i) = render(rows, i))             // i from 0 to 99
 *
 * The array is cut into one part per thread, so it pays for itself when there are many elements, or when each takes long.
 * The function runs on several threads at the same time: it can read what it captures, but what it changes must be
 * shared safely, through a `sync` record (core:thread.ec's Atomic, or a Mutex). The compiler doesn't check that here,
 * as it does for `thread.spawn`: the threads end before the call returns, so they can't outlive what they read.
 *
 * What the function allocates for an element is freed after it, except the value `parallel.map` returns: that is copied with
 * `clone` into a pool made in the current pool for each thread, which lives as long as the current pool. When the system
 * can't start a thread, the thread calling does that part itself.
 */
module "parallel"
use "core:errors.ec"
use "core:structures.ec"
use "core:thread.ec"

/** How many threads to use for `n` elements: `threads`, or one per processor for 0, but no more than `n`, and at least 1. */
priv fn threadsFor(n: Size, threads: Int): Size do
    val wanted = if threads > 0 do threads as Size else thread.cpuCount() as Size
    ret 1 if n <= 1
    ret if wanted < n do wanted else n

/**
 * Runs `body(from, until, part)` for `parts` parts of the indices from 0 to `n`, each on a thread of its own, and waits for them.
 * A part whose thread can't be started runs on this one.
 */
priv fn split(n: Size, parts: Size, body: (Size, Size, Size) => None) do
    if parts <= 1 do
        body(0, n, 0)
        ret
    val running: List<thread.Thread> = list(parts)
    for part in 0..<parts do
        val from = n * part / parts
        val until = n * (part + 1) / parts
        // The threads are joined below, before anything they use can be freed.
        val t = try unsafe thread.spawn(fn () = body(from, until, part)) catch e do
            body(from, until, part)
            continue
        running.push(t)
    for t in running.items() do
        thread.join(t) ?? panic("parallel: can't join a thread it started")

/** `f` of the elements from `from` to `until`, each copied into `mem`, into `out`. */
priv fn mapPart(values: <T>[], out: <U>[], f: (<T>) => <U>, mem: Pool, from: Size, until: Size) do
    for i in from..<until do
        pool do
            out[i] = clone(f(values[i]), mem)

/** `f` of the elements from `from` to `until`. */
priv fn eachPart(values: <T>[], f: (<T>) => None, from: Size, until: Size) do
    for i in from..<until do
        pool do
            f(values[i])

/** `f` of the numbers from `from` to `until`. */
priv fn timesPart(f: (Size) => None, from: Size, until: Size) do
    for i in from..<until do
        pool do
            f(i)

/**
 * A new array of `f` of each element, in the same order, computed on several threads: `parallel.map(numbers, fn (x) = x * x)`.
 * Each value is copied (`clone`) into a pool made in the current pool for its thread.
 * @param f called once for each element, on several threads at the same time
 * @param threads how many threads at most: 0 for one per processor
 */
fn map(values: <T>[], f: (<T>) => <U>, threads: Int = 0): <U>[] do
    val parts = threadsFor(values.len, threads)
    val out = unsafe new (<U>)[values.len]           // every element is written before the call returns
    val mems = new (Pool?)[parts]
    for i in 0..<parts do
        mems[i] = new Pool                            // lives as long as the current pool, as out does
    split(values.len, parts, fn (from, until, part) = mapPart(values, out, f, mems[part]!, from, until))
    ret out

/**
 * Calls `f` with each element, on several threads: `parallel.each(files, fn (f) = check(f))`. It returns when every call has.
 * @param f called once for each element, on several threads at the same time, in no particular order
 * @param threads how many threads at most: 0 for one per processor
 */
fn each(values: <T>[], f: (<T>) => None, threads: Int = 0) do
    split(values.len, threadsFor(values.len, threads), fn (from, until, part) = eachPart(values, f, from, until))

/**
 * Calls `f` with each number from 0 to `n - 1`, on several threads: `parallel.times(rows, fn (y) = render(image, y))`. It returns
 * when every call has.
 * @param f called once for each number, on several threads at the same time, in no particular order
 * @param threads how many threads at most: 0 for one per processor
 */
fn times(n: Size, f: (Size) => None, threads: Int = 0) do
    split(n, threadsFor(n, threads), fn (from, until, part) = timesPart(f, from, until))
