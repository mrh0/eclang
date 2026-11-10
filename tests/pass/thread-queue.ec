use "core:io.ec"
use "core:thread.ec"

// thread.Queue copies its items in and out, so no thread is left with a pointer into another's memory. What a thread is given
// is `flat`: numbers, records of them, and pointers to `sync` records, such as the Queue.

rec Sample as
    id: Int
    value: Long

mut rec Summer as
    samples: thread.Queue<Sample>
    total: Long
    count: Int

fn sum(s: @Summer) do
    while true do
        val next = thread.receive(s.samples)
        break if next == null
        s.total += next.value
        s.count += 1

// A record of one's own that threads share is `sync`: its lock guards its fields.
mut sync rec Tally as
    lock: thread.Mutex
    seen: Int

rec Counter as
    tally: @Tally
    times: Int

fn countUp(c: @Counter) do
    for i in 0..<c.times do
        thread.lock(c.tally.lock)
        c.tally.seen += 1
        thread.unlock(c.tally.lock)

fn main(): Int throws Error do
    val q: thread.Queue<Sample> = try thread.queue(2)
    val summer = new Summer { q, 0, 0 }
    val t = try thread.spawn(fn sum, summer)
    for i in 0..<10 do
        try thread.send(q, Sample { i, (i * 2) as Long })
    thread.close(q)
    try thread.join(t)
    io.log("${summer.count} samples, total ${summer.total}")
    try thread.send(q, Sample { 99, 0 }) catch e do
        io.log(e.message)

    // Without waiting: full, then empty.
    val small: thread.Queue<Int> = try thread.queue(1)
    io.log(thread.trySend(small, 7))
    io.log(thread.trySend(small, 8))
    io.log(thread.size(small))
    io.log(thread.tryReceive(small) ?? -1)
    io.log(thread.tryReceive(small) ?? -1)

    val tally = new Tally { try thread.mutex(), 0 }
    val a = try thread.spawn(fn countUp, new Counter { tally, 500 })
    val b = try thread.spawn(fn countUp, new Counter { tally, 500 })
    try thread.join(a)
    try thread.join(b)
    io.log(tally.seen)
    ret 0
