# Module `bench`

Benchmarks: run a function many times, after a warm-up, and report how long one run takes: the fastest, the median,
and how much the runs spread. The companion of core:test.ec.

    use "core:bench.ec"

    fn sortThousand() do
        val a = new Int[1000]                           // a fresh array each run
        for i in 0..<a.len do
            a[i] = ((i * 7919) % 1000) as Int
        a.sort()

    fn main(): Int do
        val b = bench.suite()
        bench.run(b, "sort 1000", fn sortThousand)
        bench.run(b, "parse", fn () = bench.keep(parseInt("12345") ?? 0))
        ret bench.finish(b)

which prints a line per benchmark, then each one's median as a multiple of the fastest:

    sort 1000                 median   72.4 us   min     71 us   spread  1.6%   cpu   73.4 us   (30 x 90)
    parse                     median   19.9 ns   min   19.5 ns   spread  0.4%   cpu   19.9 ns   (30 x 334007)

A benchmark first runs for the warm-up time (caches, the CPU's clock), which also tells how long a run takes. Then it
takes `samples` measurements, each of as many runs as fit in an equal share of the measuring time, timed with the
steady clock (`time.instant`); a run's time is that of its sample divided by the runs in it. The median is
the one to compare; the spread is the median distance of the samples from it, as a part of it: a few percent is a
quiet machine, much more says the numbers can't be trusted. cpu is the processor time (`time.cpuTime`) per
run, of all threads: well above the median for a function that uses several, well below for one that waits.

Each sample runs in a pool of its own, so what a run allocates is freed after the sample, not after every run.
Pass what a run computes to [`keep`](#bench.keep), or an optimising C compiler (`gcc -O2`) may leave out a run whose result
is never used. A program run with an argument only runs the benchmarks whose name has it in it: `./bench sort`.

## Types

### <a id="bench.Result"></a>`Result`

```ec
rec Result
```

What one benchmark measured. Times are in nanoseconds per run, with fractions for runs shorter than that.

**Fields**

- <a id="bench.Result.name"></a>`name: String` — Its name.
- <a id="bench.Result.samples"></a>`samples: Size` — How many samples were taken.
- <a id="bench.Result.runs"></a>`runs: Long` — How many runs each sample timed.
- <a id="bench.Result.min"></a>`min: Double` — The time of a run in the fastest sample.
- <a id="bench.Result.median"></a>`median: Double` — The median time of a run: the one to compare.
- <a id="bench.Result.mean"></a>`mean: Double` — The mean time of a run.
- <a id="bench.Result.max"></a>`max: Double` — The time of a run in the slowest sample.
- <a id="bench.Result.spread"></a>`spread: Double` — The median distance of the samples from the median, as a part of it: 0.02 is 2%.
- <a id="bench.Result.cpu"></a>`cpu: Double` — The processor time of a run, of all the program's threads.

### <a id="bench.Options"></a>`Options`

```ec
rec Options
```

How long to warm up and measure, and in how many samples.

**Fields**

- <a id="bench.Options.warmup"></a>`warmup: time.Duration = time.millis(200)` — How long to run the function before measuring.
- <a id="bench.Options.measure"></a>`measure: time.Duration = time.seconds(1)` — About how long to measure, at least one run per sample.
- <a id="bench.Options.samples"></a>`samples: Size = 30` — How many samples to take.

### <a id="bench.Suite"></a>`Suite`

```ec
mut ref rec Suite
```

A group of benchmarks that are run and reported together: [`suite`](#bench.suite).

**Fields**

- <a id="bench.Suite.filter"></a>`filter: String` — Only benchmarks with this in their name run, unless it is empty.
- <a id="bench.Suite.options"></a>`options: Options` — How each benchmark is run.
- <a id="bench.Suite.results"></a>`results: List<Result>` — What was measured, in the order of the runs.
- <a id="bench.Suite.skipped"></a>`skipped: Size` — How many benchmarks the filter left out.

## Functions

### <a id="bench.suite"></a>`suite`

```ec
fn suite(): Suite
```

A new suite, in the current pool, with the usual [`Options`](#bench.Options). The first command-line argument of the program,
if any, is its filter.

### `suite`

```ec
fn suite(options: Options): Suite
```

A new suite, in the current pool, that runs its benchmarks with `options`.

### `suite`

```ec
fn suite(filter: String, options: Options = Options {}): Suite
```

A new suite that only runs the benchmarks with `filter` in their name, with `options`.

### <a id="bench.keep"></a>`keep`

```ec
fn keep(x: Long)
```

Uses `x`, so that the C compiler can't leave out the code that computed it.

### `keep`

```ec
fn keep(x: ULong)
```

Uses `x`, so that the C compiler can't leave out the code that computed it.

### `keep`

```ec
fn keep(x: Double)
```

Uses `x`, so that the C compiler can't leave out the code that computed it.

### `keep`

```ec
fn keep(x: Bool)
```

Uses `x`, so that the C compiler can't leave out the code that computed it.

### `keep`

```ec
fn keep(s: String)
```

Uses `s` (its length and first byte), so that the C compiler can't leave out the code that computed it.

### <a id="bench.measure"></a>`measure`

```ec
fn measure(name: String, body: () => None, options: Options = Options {}): Result
```

Measures `body`: warms it up, then times `options.samples` samples of it. Nothing is printed.

**Parameters**

- `name` — what to call it in the result
- `body` — the code to measure, run many times
- `options` — how long to warm up and measure

### <a id="bench.text"></a>`text`

```ec
fn text(nanos: Double): String
```

`nanos` with up to three significant digits and a unit: "812 ns", "41.2 us", "1.5 s".

### <a id="bench.line"></a>`line`

```ec
fn line(r: Result): String
```

The line [`run`](#bench.run) prints for `r`.

### <a id="bench.run"></a>`run`

```ec
fn run(s: Suite, name: String, body: () => None)
```

Measures `body` with the suite's options and prints a line about it, unless the filter leaves it out.

**Parameters**

- `s` — the suite
- `name` — what to call it
- `body` — the code to measure, run many times

### <a id="bench.finish"></a>`finish`

```ec
fn finish(s: Suite): Int
```

Prints how the benchmarks compare: each one's median as a multiple of the fastest one's.

**Returns** what `main` should return: 0, or 1 when the filter left nothing to run
