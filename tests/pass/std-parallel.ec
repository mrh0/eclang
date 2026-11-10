use "core:io.ec"
use "core:thread.ec"
use "core:parallel.ec"
use "core:array.ec"

// core:thread.ec's WorkerPool, and core:parallel.ec.

mut sync rec Tally as
    lock: thread.Mutex
    total: Long
    jobs: thread.Atomic

fn work(t: @Tally, n: Int) do
    thread.lock(t.lock)
    t.total += n
    thread.unlock(t.lock)
    thread.increment(mut t.jobs)

fn main(): Int throws Error do
    val tally = new Tally { try thread.mutex(), 0, thread.Atomic { 0 } }
    val workers = try thread.workerPool(4, 8)
    for i in 1..=100 do
        try thread.submit(workers, fn () = work(tally, i))
    thread.wait(workers)
    io.log(tally.total)
    io.log(thread.load(mut tally.jobs))
    io.log(thread.pending(workers))
    try thread.submit(workers, fn () = work(tally, 1000))
    try thread.stop(workers)
    io.log(tally.total)
    try thread.stop(workers)
    try thread.submit(workers, fn () = work(tally, 1)) catch e do
        io.log(e.message)

    val numbers = new Int[1000]
    for i in 0..<numbers.len do
        numbers[i] = i as Int
    val squares = parallel.map(numbers, fn (x) = x * x)
    io.log(squares.len)
    io.log(squares[999])
    io.log(squares.reduce(0l, fn (sum, x) = sum + x))
    val labels = parallel.map(numbers, fn (x) = "#${x}", 3)
    io.log(labels[0])
    io.log(labels[999])
    io.log(parallel.map(numbers[0..<0], fn (x) = x).len)
    io.log(parallel.map(numbers[0..<1], fn (x) = x + 1)[0])

    val seen = new Tally { try thread.mutex(), 0, thread.Atomic { 0 } }
    parallel.each(numbers, fn (x) = work(seen, x))
    io.log(seen.total)
    parallel.times(10, fn (i) = work(seen, i as Int), 2)
    io.log(seen.total)
    io.log(thread.load(mut seen.jobs))
    ret 0
