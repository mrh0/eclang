use "core:io.ec"
use "core:thread.ec"

// core:thread.ec: threads, a mutex, atomics, a wait group, a channel, a read-write lock and a condition.

// Threads share it, so it is `sync`: its lock guards total, and the rest are made to be shared.
mut sync rec Shared as
    lock: thread.Mutex
    total: Long
    hits: thread.Atomic
    group: thread.WaitGroup

rec Job as
    id: Int
    shared: @Shared

fn work(job: @Job) do
    val label = "job ${job.id}"
    for i in 0..<10000 do
        thread.lock(job.shared.lock)
        job.shared.total += 1
        thread.unlock(job.shared.lock)
        thread.increment(mut job.shared.hits)
    thread.done(job.shared.group)

mut rec Pipe as
    jobs: thread.Channel
    sum: Long

fn consume(p: @Pipe) do
    while true do
        val next = thread.receive(p.jobs)
        break if next == null
        p.sum += @(next !as @Long)

var ran = 0
fn plain() do
    ran = 1

fn main(): Int throws Error do
    // What a thread is given must outlive main's locals: it is allocated in the current pool.
    val shared = new Shared { try thread.mutex(), 0, thread.Atomic { 0 }, try thread.waitGroup() }
    thread.add(shared.group, 4)
    val a = new Job { 1, shared }
    val b = new Job { 2, shared }
    val c3 = new Job { 3, shared }
    val d = new Job { 4, shared }
    val threads = [try thread.spawn(fn work, a), try thread.spawn(fn work, b), try thread.spawn(fn work, c3), try thread.spawn(fn work, d)]
    thread.wait(shared.group)
    for t in threads do
        try thread.join(t)
    io.log(shared.total)
    io.log(thread.load(mut shared.hits))

    val p = new Pipe { try thread.channel(4), 0 }
    val consumer = try thread.spawn(fn consume, p)
    val values = new Long[100]
    for i in 0..<100 do
        values[i] = i as Long
        try thread.send(p.jobs, addrof(values[i]))
    thread.close(p.jobs)
    try thread.join(consumer)
    io.log(p.sum)
    try thread.send(p.jobs, addrof(values[0])) catch e do
        io.log(e.message)

    val t = try thread.spawn(fn plain)
    try thread.join(t)
    io.log(ran)
    io.log(thread.cpuCount() > 0)
    val rw = try thread.rwLock()
    thread.readLock(rw)
    io.log(thread.tryWriteLock(rw))
    thread.unlock(rw)
    val m = try thread.mutex()
    val c = try thread.cond()
    thread.lock(m)
    io.log(thread.waitFor(c, m, 1000))
    thread.unlock(m)
    ret 0
