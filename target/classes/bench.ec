/**
 * Benchmarks: run a function many times, after a warm-up, and report how long one run takes: the fastest, the median,
 * and how much the runs spread. The companion of core:test.ec.
 *
 *     use "core:bench.ec"
 *
 *     fn sortThousand() do
 *         val a = new Int[1000]                           // a fresh array each run
 *         for i in 0..<a.len do
 *             a[i] = ((i * 7919) % 1000) as Int
 *         a.sort()
 *
 *     fn main(): Int do
 *         val b = bench.suite()
 *         bench.run(b, "sort 1000", fn sortThousand)
 *         bench.run(b, "parse", fn () = bench.keep(parseInt("12345") ?? 0))
 *         ret bench.finish(b)
 *
 * which prints a line per benchmark, then each one's median as a multiple of the fastest:
 *
 *     sort 1000                 median   72.4 us   min     71 us   spread  1.6%   cpu   73.4 us   (30 x 90)
 *     parse                     median   19.9 ns   min   19.5 ns   spread  0.4%   cpu   19.9 ns   (30 x 334007)
 *
 * A benchmark first runs for the warm-up time (caches, the CPU's clock), which also tells how long a run takes. Then it
 * takes `samples` measurements, each of as many runs as fit in an equal share of the measuring time, timed with the
 * steady clock ({@link time.instant}); a run's time is that of its sample divided by the runs in it. The median is
 * the one to compare; the spread is the median distance of the samples from it, as a part of it: a few percent is a
 * quiet machine, much more says the numbers can't be trusted. cpu is the processor time ({@link time.cpuTime}) per
 * run, of all threads: well above the median for a function that uses several, well below for one that waits.
 *
 * Each sample runs in a pool of its own, so what a run allocates is freed after the sample, not after every run.
 * Pass what a run computes to {@link keep}, or an optimising C compiler (`gcc -O2`) may leave out a run whose result
 * is never used. A program run with an argument only runs the benchmarks whose name has it in it: `./bench sort`.
 */
module "bench"
use "core:errors.ec"
use "core:string.ec"
use "core:array.ec"
use "core:structures.ec"
use "core:math.ec"
use "core:time.ec"
use "core:io.ec"

/** What one benchmark measured. Times are in nanoseconds per run, with fractions for runs shorter than that. */
rec Result as
    /** Its name. */
    name: String
    /** How many samples were taken. */
    samples: Size
    /** How many runs each sample timed. */
    runs: Long
    /** The time of a run in the fastest sample. */
    min: Double
    /** The median time of a run: the one to compare. */
    median: Double
    /** The mean time of a run. */
    mean: Double
    /** The time of a run in the slowest sample. */
    max: Double
    /** The median distance of the samples from the median, as a part of it: 0.02 is 2%. */
    spread: Double
    /** The processor time of a run, of all the program's threads. */
    cpu: Double

/** How long to warm up and measure, and in how many samples. */
rec Options as
    /** How long to run the function before measuring. */
    warmup: time.Duration = time.millis(200)
    /** About how long to measure, at least one run per sample. */
    measure: time.Duration = time.seconds(1)
    /** How many samples to take. */
    samples: Size = 30

/** A group of benchmarks that are run and reported together: {@link suite}. */
mut ref rec Suite as
    /** Only benchmarks with this in their name run, unless it is empty. */
    filter: String
    /** How each benchmark is run. */
    options: Options
    /** What was measured, in the order of the runs. */
    results: List<Result>
    /** How many benchmarks the filter left out. */
    skipped: Size

/**
 * A new suite, in the current pool, with the usual {@link Options}. The first command-line argument of the program,
 * if any, is its filter.
 */
fn suite(): Suite = suite(Options {})

/** A new suite, in the current pool, that runs its benchmarks with `options`. */
fn suite(options: Options): Suite do
    val args = io.args()
    val results: List<Result> = list()
    ret new Suite { if args.len > 0 do args[0] else "", options, results, 0 }

/** A new suite that only runs the benchmarks with `filter` in their name, with `options`. */
fn suite(filter: String, options: Options = Options {}): Suite do
    val results: List<Result> = list()
    ret new Suite { filter, options, results, 0 }

// ---------------------------------------------------------------------------------------------------
// Keeping results

/** Where {@link keep} writes, which the C compiler can't leave out. */
priv var sink: Volatile<ULong> = 0

/** Uses `x`, so that the C compiler can't leave out the code that computed it. */
fn keep(x: Long) do
    sink = x as ULong

/** Uses `x`, so that the C compiler can't leave out the code that computed it. */
fn keep(x: ULong) do
    sink = x

/** Uses `x`, so that the C compiler can't leave out the code that computed it. */
fn keep(x: Double) do
    sink = x as ULong

/** Uses `x`, so that the C compiler can't leave out the code that computed it. */
fn keep(x: Bool) do
    sink = if x do 1 else 0

/** Uses `s` (its length and first byte), so that the C compiler can't leave out the code that computed it. */
fn keep(s: String) do
    sink = s.len as ULong + (if s.len > 0 do s[0] as UChar as ULong else 0)

// ---------------------------------------------------------------------------------------------------
// Measuring

/** The median of sorted values. */
priv fn medianOf(sorted: Double[]): Double do
    val n = sorted.len
    ret if n % 2 == 1 do sorted[n / 2] else (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0

/** Runs `body` `n` times in a pool of its own, and gives the nanoseconds that took. */
priv fn sample(body: () => None, n: Long): Long do
    var took: Long = 0
    pool do
        val start = time.instant()
        for i in 0..<n do
            body()
        took = time.nanosBetween(start, time.instant())
    ret took

/**
 * Measures `body`: warms it up, then times `options.samples` samples of it. Nothing is printed.
 * @param name what to call it in the result
 * @param body the code to measure, run many times
 * @param options how long to warm up and measure
 */
fn measure(name: String, body: () => None, options: Options = Options {}): Result do
    val samples = math.max(options.samples, 1 as Size)
    // Warm up, in samples that double, and learn how long a run takes.
    var warmRuns: Long = 0
    var warmNanos: Long = 0
    var batch: Long = 1
    while warmRuns == 0 or warmNanos < options.warmup.micros * 1000 do
        warmNanos += sample(body, batch)
        warmRuns += batch
        batch = math.min(batch * 2, 1000000 as Long)
    val perRun = math.max((warmNanos as Double) / (warmRuns as Double), 1.0)
    val share = (options.measure.micros as Double) * 1000.0 / (samples as Double)
    val runs = math.max((share / perRun) as Long, 1 as Long)

    val times = new Double[samples]
    val cpuStart = time.cpuTime()
    for i in 0..<samples do
        times[i] = (sample(body, runs) as Double) / (runs as Double)
    val cpuNanos = (time.cpuTime().micros - cpuStart.micros) as Double * 1000.0
    sort(times)
    val median = medianOf(times)
    val distances = new Double[samples]
    var total = 0.0
    for i in 0..<samples do
        distances[i] = math.abs(times[i] - median)
        total += times[i]
    sort(distances)
    ret Result {
        name = name,
        samples = samples,
        runs = runs,
        min = times[0],
        median = median,
        mean = total / (samples as Double),
        max = times[samples - 1],
        spread = if median > 0.0 do medianOf(distances) / median else 0.0,
        cpu = cpuNanos / ((samples as Double) * (runs as Double))
    }

/** `nanos` with up to three significant digits and a unit: "812 ns", "41.2 us", "1.5 s". */
fn text(nanos: Double): String do
    ret "${nanos:.3g} ns" if nanos < 1000.0
    ret "${nanos / 1000.0:.3g} us" if nanos < 1000000.0
    ret "${nanos / 1000000.0:.3g} ms" if nanos < 1000000000.0
    ret "${nanos / 1000000000.0:.3g} s"

/** The line {@link run} prints for `r`. */
fn line(r: Result): String do
    val median = text(r.median)
    val min = text(r.min)
    val cpu = text(r.cpu)
    val spread = r.spread * 100.0
    ret "${r.name:<24}  median ${median:>9}   min ${min:>9}   spread ${spread:>4.1f}%   cpu ${cpu:>9}   (${r.samples} x ${r.runs})"

/**
 * Measures `body` with the suite's options and prints a line about it, unless the filter leaves it out.
 * @param s the suite
 * @param name what to call it
 * @param body the code to measure, run many times
 */
fn run(s: Suite, name: String, body: () => None) do
    if indexOf(name, s.filter) == null do
        s.skipped += 1
        ret
    val r = measure(clone(name, s.results.mem), body, s.options)
    s.results.push(r)
    io.log(line(r))
    io.flush()

/**
 * Prints how the benchmarks compare: each one's median as a multiple of the fastest one's.
 * @return what `main` should return: 0, or 1 when the filter left nothing to run
 */
fn finish(s: Suite): Int do
    val left = if s.skipped > 0 do format(", ${s.skipped} left out") else ""
    io.log("")
    io.log("${s.results.len} benchmark(s)${left}")
    ret 1 if s.results.len == 0
    var fastest = s.results.buffer[0].median
    for r in s.results.items() do
        fastest = math.min(fastest, r.median)
    if s.results.len > 1 and fastest > 0.0 do
        for r in s.results.items() do
            val times = r.median / fastest
            io.log("  ${r.name:<24}  ${times:>8.2f}x")
    ret 0
