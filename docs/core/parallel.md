# Module `parallel`

Runs a function on the elements of an array on several threads at once, and waits until it is done: `parallel.map` is
`map` (core:array.ec) on as many threads as there are processors.

    use "core:parallel.ec"
    val squares = parallel.map(numbers, fn (x) = x * x)        // in order, as `numbers.map(...)` would give them
    parallel.each(files, fn (f) = check(f))
    parallel.times(100, fn (i) = render(rows, i))             // i from 0 to 99

The array is cut into one part per thread, so it pays for itself when there are many elements, or when each takes long.
The function runs on several threads at the same time: it can read what it captures, but what it changes must be
shared safely, through a `sync` record (core:thread.ec's Atomic, or a Mutex). The compiler doesn't check that here,
as it does for `thread.spawn`: the threads end before the call returns, so they can't outlive what they read.

What the function allocates for an element is freed after it, except the value `parallel.map` returns: that is copied with
`clone` into a pool made in the current pool for each thread, which lives as long as the current pool. When the system
can't start a thread, the thread calling does that part itself.

## Functions

### <a id="parallel.map"></a>`map`

```ec
fn map(values: <T>[], f: (<T>) => <U>, threads: Int = 0): <U>[]
```

A new array of `f` of each element, in the same order, computed on several threads: `parallel.map(numbers, fn (x) = x * x)`.
Each value is copied (`clone`) into a pool made in the current pool for its thread.

**Parameters**

- `f` — called once for each element, on several threads at the same time
- `threads` — how many threads at most: 0 for one per processor

### <a id="parallel.each"></a>`each`

```ec
fn each(values: <T>[], f: (<T>) => None, threads: Int = 0)
```

Calls `f` with each element, on several threads: `parallel.each(files, fn (f) = check(f))`. It returns when every call has.

**Parameters**

- `f` — called once for each element, on several threads at the same time, in no particular order
- `threads` — how many threads at most: 0 for one per processor

### <a id="parallel.times"></a>`times`

```ec
fn times(n: Size, f: (Size) => None, threads: Int = 0)
```

Calls `f` with each number from 0 to `n - 1`, on several threads: `parallel.times(rows, fn (y) = render(image, y))`. It returns
when every call has.

**Parameters**

- `f` — called once for each number, on several threads at the same time, in no particular order
- `threads` — how many threads at most: 0 for one per processor
