# Module `thread`

Threads and the tools to share data between them: Mutex, RwLock, Cond, WaitGroup, Atomic, Queue and Channel, and a
WorkerPool of threads that run jobs. core:parallel.ec builds `parallel.map` on them.
Built on pthread.h, APR's apr_thread_*.h and apr_atomic.h.

    use "core:thread.ec"

    rec Job as
        id: Int
        total: Long

    fn work(job: @Job) do                         // runs in the new thread
        for i in 0..<1000 do
            job.total += i

    fn main(): Int throws Error do
        val a = new Job { id = 1, total = 0 }
        val t = try thread.spawn(fn work, a)
        try thread.join(t)
        io.log(a.total)
        ret 0

A thread runs a function given a pointer to its data. Lambdas don't capture variables, so put what the thread
needs in a record and pass a pointer to it. The compiler checks that the data outlives the function starting the
thread: it can't be a local variable (`addrof x`), nor be in a `pool do` block or a pool that function made, so
allocate it with `new` in the current pool. The data is also `flat`: it holds numbers, records of them, and pointers to
`sync` records, which their own functions make safe to share (a [`Mutex`](#thread.Mutex), an [`Atomic`](#thread.Atomic), a [`Queue`](#thread.Queue)), but no
Strings, arrays, Lists or pointers to other records, which this thread might free or change while the other reads them.
Share mutable state through a `sync rec` of your own that guards its fields with a Mutex, and hand work over in a
[`Queue`](#thread.Queue), which copies its items. Each [`Thread`](#thread.Thread) must be joined or detached: dropping one, or never
using a variable that holds one, is an error.

Each thread starts with its own current pool, freed when its function returns, so its code can allocate as
usual. Locks, conditions and channels are allocated in the pool that is current when they are made, and are
destroyed with it.

## Types

### <a id="thread.Thread"></a>`Thread`

```ec
resource rec Thread
```

A running thread, from [`spawn`](#thread.spawn). [`join`](#thread.join) or [`detach`](#thread.detach) it: it is a `resource`, so it can't be dropped.

**Fields**

- <a id="thread.Thread.handle"></a>`handle: pthread_t` — The system's thread.
- <a id="thread.Thread.ending"></a>`ending: @Ending` — Whether it was joined or detached.

### <a id="thread.Mutex"></a>`Mutex`

```ec
sync rec Mutex
```

A lock for one thread at a time.

    val m = try thread.mutex()
    thread.lock(m)
    defer thread.unlock(m)

**Fields**

- <a id="thread.Mutex.handle"></a>`handle: @apr_thread_mutex_t` — The APR mutex.

### <a id="thread.RwLock"></a>`RwLock`

```ec
sync rec RwLock
```

A lock for many readers at a time, or one writer.

**Fields**

- <a id="thread.RwLock.handle"></a>`handle: @apr_thread_rwlock_t` — The APR lock.

### <a id="thread.Cond"></a>`Cond`

```ec
sync rec Cond
```

Lets threads wait until another thread says something changed. Always wait in a loop that checks the
condition, holding the mutex that protects it:

    thread.lock(m)
    while queue.count == 0 do
        thread.wait(c, m)
    ...
    thread.unlock(m)

**Fields**

- <a id="thread.Cond.handle"></a>`handle: @apr_thread_cond_t` — The APR condition variable.

### <a id="thread.WaitGroup"></a>`WaitGroup`

```ec
mut sync ref rec WaitGroup
```

Waits for a number of tasks to finish.

    val g = try thread.waitGroup()
    thread.add(g, 3)
    ... each of the 3 workers calls thread.done(g) when it is finished ...
    thread.wait(g)

**Fields**

- <a id="thread.WaitGroup.count"></a>`count: Long` — How many tasks aren't done yet.
- <a id="thread.WaitGroup.lock"></a>`lock: Mutex` — Protects `count`.
- <a id="thread.WaitGroup.zero"></a>`zero: Cond` — Signalled when `count` reaches 0.

### <a id="thread.Atomic"></a>`Atomic`

```ec
sync rec Atomic
```

A UInt that threads can change at the same time without a lock.

    var hits = thread.Atomic { 0 }
    thread.increment(mut hits)               // from any thread
    io.log(thread.load(mut hits))

**Fields**

- <a id="thread.Atomic.value"></a>`value: UInt` — The number. Use the functions below, not this field, once threads share it.

### <a id="thread.Channel"></a>`Channel`

```ec
mut sync ref rec Channel
```

A queue of pointers, to hand work from one thread to another. [`send`](#thread.send) waits while the channel is full,
[`receive`](#thread.receive) waits while it is empty. After [`close`](#thread.close), `receive` gives what is left and then null.

    val jobs = try thread.channel(16)
    try thread.send(jobs, addrof(job))           // producer
    val next = thread.receive(jobs)              // consumer: a @Any?, null when closed
    if next != null do
        val job = next !as @Job

**Fields**

- <a id="thread.Channel.items"></a>`items: (@Any?)[]` — The ring of items.
- <a id="thread.Channel.head"></a>`head: Size` — The index in `items` of the first item.
- <a id="thread.Channel.count"></a>`count: Size` — How many items it holds.
- <a id="thread.Channel.closed"></a>`closed: Bool` — Whether [`close`](#thread.close) was called.
- <a id="thread.Channel.lock"></a>`lock: Mutex` — Protects the other fields.
- <a id="thread.Channel.notEmpty"></a>`notEmpty: Cond` — Signalled when an item arrives.
- <a id="thread.Channel.notFull"></a>`notFull: Cond` — Signalled when there is room for an item.

### <a id="thread.Queue"></a>`Queue`

```ec
mut sync ref rec Queue<T>
```

A queue of values, to hand work from one thread to another. Unlike a [`Channel`](#thread.Channel), it holds copies: [`send`](#thread.send) copies the
item in, [`receive`](#thread.receive) copies it out, so no thread is left with a pointer into another's memory. Its items are `flat`: numbers,
records of them, and pointers to `sync` records (another Queue, a [`Mutex`](#thread.Mutex)), which the compiler checks at each `send`.
[`send`](#thread.send) waits while it is full, [`receive`](#thread.receive) while it is empty. After [`close`](#thread.close), `receive` gives what is left, then null.

    rec Job as
        id: Int
        size: Long

    val jobs: thread.Queue<Job> = try thread.queue(16)
    try thread.send(jobs, Job { 1, 100 })        // producer: a copy goes in
    val next = thread.receive(jobs)              // consumer: a Job?, null when closed

**Parameters**

- `<T>` — the type of the items

**Fields**

- <a id="thread.Queue.items"></a>`items: T[]` — The ring of items.
- <a id="thread.Queue.head"></a>`head: Size` — The index in `items` of the first item.
- <a id="thread.Queue.count"></a>`count: Size` — How many items it holds.
- <a id="thread.Queue.closed"></a>`closed: Bool` — Whether [`close`](#thread.close) was called.
- <a id="thread.Queue.lock"></a>`lock: Mutex` — Protects the other fields.
- <a id="thread.Queue.notEmpty"></a>`notEmpty: Cond` — Signalled when an item arrives.
- <a id="thread.Queue.notFull"></a>`notFull: Cond` — Signalled when there is room for an item.

### <a id="thread.WorkerPool"></a>`WorkerPool`

```ec
mut sync ref rec WorkerPool
```

A fixed number of threads that run the jobs [`submit`](#thread.submit) gives them, in the order they were given, as soon as one of the
threads is free. Starting a thread for each job costs more than the job when jobs are small; a WorkerPool starts its threads
once.

    val workers = try thread.workerPool(4)
    for i in 0..<100 do
        try thread.submit(workers, fn () = work(i))     // a lambda's `val`s are copied, as for spawn
    thread.wait(workers)                                // until every job is done
    try thread.stop(workers)                            // the threads end: always stop a WorkerPool

A job is a function value as [`spawn`](#thread.spawn) takes one: `escaping` and `flat`. Each job runs in a `pool do` block of its own, so
what it allocates is freed when it ends: it gives results through `sync` records (an [`Atomic`](#thread.Atomic), a [`Queue`](#thread.Queue), a
`sync rec` of your own guarded by a [`Mutex`](#thread.Mutex)). The pool is allocated in the current pool, which must outlive it:
[`stop`](#thread.stop) it before that pool is freed. For work on the elements of an array, core:parallel.ec's `parallel.map` is simpler.

**Fields**

- <a id="thread.WorkerPool.jobs"></a>`jobs: Queue<PoolJob>` — The jobs not started yet.
- <a id="thread.WorkerPool.threads"></a>`threads: (Thread?)[]` — The threads, all set once [`workerPool`](#thread.workerPool) returns.
- <a id="thread.WorkerPool.pending"></a>`pending: WaitGroup` — The jobs given and not finished yet.
- <a id="thread.WorkerPool.stopped"></a>`stopped: Bool` — Whether [`stop`](#thread.stop) was called. Only the thread that made the pool reads or writes it.

## Functions

### <a id="thread.spawn"></a>`spawn`

```ec
fn spawn(escaping flat body: (@<T>) => None, escaping flat data: @<T>): Thread throws OsError
```

Starts a thread that runs `body(data)`. The thread must be joined (or detached).

**Parameters**

- `body` — the function to run, which the thread keeps (`escaping`): a lambda that uses a `var` of the caller can't be given,
  and what a lambda captures must be `flat`
- `data` — what it is given, which the thread keeps (`escaping`): not a local variable, nor in a pool the caller frees. It is
  `flat`: what it points to holds numbers, records of them, and pointers to `sync` records (a [`Mutex`](#thread.Mutex), a [`Queue`](#thread.Queue), a
  `sync rec` of your own), but no Strings, arrays, Lists or other pointers, which could reach memory this thread frees or changes

**Returns** the new thread

**Throws**

- `OsError` — when the system can't start a thread

### `spawn`

```ec
fn spawn(escaping flat body: () => None): Thread throws OsError
```

Starts a thread that runs `body()`. The thread must be joined (or detached).

**Parameters**

- `body` — the function to run, which the thread keeps (`escaping`). A lambda can use the caller's `val`s (it copies them, in the
  current pool) when they are `flat` (numbers, records of them, pointers to `sync` records), but not its `var`s, which it would
  share with a function that may return before the thread ends

**Returns** the new thread

**Throws**

- `OsError` — when the system can't start a thread

### <a id="thread.join"></a>`join`

```ec
fn join(t: Thread) throws OsError
```

Waits until the thread has finished.

**Throws**

- `OsError` — when it can't be joined (it was detached or joined before, say)

### <a id="thread.detach"></a>`detach`

```ec
fn detach(t: Thread) throws OsError
```

Lets the thread run on its own: it can't be joined, and its resources are freed when it ends.

**Throws**

- `OsError` — when it can't be detached

### <a id="thread.yieldNow"></a>`yieldNow`

```ec
fn yieldNow()
```

Lets other threads run before this one goes on.

### <a id="thread.cpuCount"></a>`cpuCount`

```ec
fn cpuCount(): Int
```

The number of processors that are online (at least 1).

### <a id="thread.mutex"></a>`mutex`

```ec
fn mutex(nested: Bool = false): Mutex throws OsError
```

A mutex in the current pool.

**Parameters**

- `nested` — whether the thread holding it can lock it again (and must unlock it as many times)

**Throws**

- `OsError` — when the system can't make one

### <a id="thread.lock"></a>`lock`

```ec
fn lock(m: Mutex)
```

Waits until no other thread holds `m`, then holds it.

### <a id="thread.tryLock"></a>`tryLock`

```ec
fn tryLock(m: Mutex): Bool
```

Holds `m` if no other thread does, without waiting.

**Returns** whether it now holds `m`

### <a id="thread.unlock"></a>`unlock`

```ec
fn unlock(m: Mutex)
```

Lets go of `m`, which this thread must hold.

### <a id="thread.rwLock"></a>`rwLock`

```ec
fn rwLock(): RwLock throws OsError
```

A read-write lock in the current pool.

**Throws**

- `OsError` — when the system can't make one

### <a id="thread.readLock"></a>`readLock`

```ec
fn readLock(l: RwLock)
```

Waits until no thread writes, then holds `l` for reading.

### <a id="thread.writeLock"></a>`writeLock`

```ec
fn writeLock(l: RwLock)
```

Waits until no thread reads or writes, then holds `l` for writing.

### <a id="thread.tryReadLock"></a>`tryReadLock`

```ec
fn tryReadLock(l: RwLock): Bool
```

Holds `l` for reading if no thread writes, without waiting.

**Returns** whether it now holds `l`

### <a id="thread.tryWriteLock"></a>`tryWriteLock`

```ec
fn tryWriteLock(l: RwLock): Bool
```

Holds `l` for writing if no thread reads or writes, without waiting.

**Returns** whether it now holds `l`

### `unlock`

```ec
fn unlock(l: RwLock)
```

Lets go of a read or write lock.

### <a id="thread.cond"></a>`cond`

```ec
fn cond(): Cond throws OsError
```

A condition variable in the current pool.

**Throws**

- `OsError` — when the system can't make one

### <a id="thread.wait"></a>`wait`

```ec
fn wait(c: Cond, m: Mutex)
```

Lets go of `m`, waits for a signal, and holds `m` again. It may also wake without a signal.

### <a id="thread.waitFor"></a>`waitFor`

```ec
fn waitFor(c: Cond, m: Mutex, micros: Long): Bool
```

Like [`wait`](#thread.wait), but with a time limit.

**Parameters**

- `micros` — how many microseconds to wait at most

**Returns** false when the time ran out

### <a id="thread.signal"></a>`signal`

```ec
fn signal(c: Cond)
```

Wakes one waiting thread.

### <a id="thread.broadcast"></a>`broadcast`

```ec
fn broadcast(c: Cond)
```

Wakes every waiting thread.

### <a id="thread.waitGroup"></a>`waitGroup`

```ec
fn waitGroup(): WaitGroup throws OsError
```

A WaitGroup with no tasks, in the current pool.

**Throws**

- `OsError` — when the system can't make its lock

### <a id="thread.add"></a>`add`

```ec
fn add(g: WaitGroup, n: Long)
```

Adds `n` tasks to wait for. The program panics when the count goes below 0.

### <a id="thread.done"></a>`done`

```ec
fn done(g: WaitGroup)
```

One task is finished. The program panics when that is more than were added.

### `wait`

```ec
fn wait(g: WaitGroup)
```

Waits until every task added is done.

### <a id="thread.load"></a>`load`

```ec
fn load(mut a: Atomic): UInt
```

The value now.

### <a id="thread.store"></a>`store`

```ec
fn store(mut a: Atomic, value: UInt)
```

Sets the value.

### `add`

```ec
fn add(mut a: Atomic, n: UInt): UInt
```

Adds `n`.

**Returns** the value before

### <a id="thread.sub"></a>`sub`

```ec
fn sub(mut a: Atomic, n: UInt)
```

Subtracts `n`.

### <a id="thread.increment"></a>`increment`

```ec
fn increment(mut a: Atomic): UInt
```

Adds 1.

**Returns** the value before

### <a id="thread.decrement"></a>`decrement`

```ec
fn decrement(mut a: Atomic): Bool
```

Subtracts 1.

**Returns** whether the value is now something other than 0

### <a id="thread.exchange"></a>`exchange`

```ec
fn exchange(mut a: Atomic, with: UInt): UInt
```

Sets the value to `with`.

**Returns** the value before

### <a id="thread.compareAndSwap"></a>`compareAndSwap`

```ec
fn compareAndSwap(mut a: Atomic, expected: UInt, with: UInt): Bool
```

Sets the value to `with` if it is `expected`.

**Returns** whether it was `expected`

### <a id="thread.channel"></a>`channel`

```ec
fn channel(capacity: Size): Channel throws OsError
```

A channel in the current pool.

**Parameters**

- `capacity` — how many items it holds (0 counts as 1)

**Throws**

- `OsError` — when the system can't make its lock

### <a id="thread.send"></a>`send`

```ec
fn send(c: Channel, item: @Any) throws StateError
```

Puts `item` at the end, waiting while the channel is full.

**Throws**

- `StateError` — when the channel is closed

### <a id="thread.trySend"></a>`trySend`

```ec
fn trySend(c: Channel, item: @Any): Bool
```

Puts `item` at the end if there is room, without waiting.

**Returns** whether it did: false when the channel is full or closed

### <a id="thread.receive"></a>`receive`

```ec
fn receive(c: Channel): @Any?
```

Takes the first item, waiting while the channel is empty.

**Returns** the item; null when the channel is closed and empty

### <a id="thread.tryReceive"></a>`tryReceive`

```ec
fn tryReceive(c: Channel): @Any?
```

Takes the first item if there is one, without waiting.

**Returns** the item, or null when the channel is empty

### <a id="thread.close"></a>`close`

```ec
fn close(c: Channel)
```

No more items can be sent. Threads waiting in [`receive`](#thread.receive) get what is left, then null; threads waiting in [`send`](#thread.send) get a StateError.

### <a id="thread.size"></a>`size`

```ec
fn size(c: Channel): Size
```

The number of items waiting in the channel.

### <a id="thread.queue"></a>`queue`

```ec
fn queue(capacity: Size): Queue<T> throws OsError
```

An empty queue in the current pool: `val q: thread.Queue<Int> = try thread.queue(16)`.

**Parameters**

- `capacity` — how many items it holds (0 counts as 1)

**Throws**

- `OsError` — when the system can't make its lock

### `send`

```ec
fn send(q: Queue<T>, flat item: <T>) throws StateError
```

Puts a copy of `item` at the end, waiting while the queue is full.

**Parameters**

- `item` — what to send: `flat`, so that the thread taking it gets no pointer into this one's memory

**Throws**

- `StateError` — when the queue is closed

### `trySend`

```ec
fn trySend(q: Queue<T>, flat item: <T>): Bool
```

Puts a copy of `item` at the end if there is room, without waiting.

**Returns** whether it did: false when the queue is full or closed

### `receive`

```ec
fn receive(q: Queue<T>): <T>?
```

Takes the first item, waiting while the queue is empty.

**Returns** the item; null when the queue is closed and empty

### `tryReceive`

```ec
fn tryReceive(q: Queue<T>): <T>?
```

Takes the first item if there is one, without waiting.

**Returns** the item, or null when the queue is empty

### `close`

```ec
fn close(q: Queue<T>)
```

No more items can be sent. Threads waiting in [`receive`](#thread.receive) get what is left, then null; threads waiting in [`send`](#thread.send) get a StateError.

### `size`

```ec
fn size(q: Queue<T>): Size
```

The number of items waiting in the queue.

### <a id="thread.workerPool"></a>`workerPool`

```ec
fn workerPool(threads: Int = 0, backlog: Size = 1024): WorkerPool throws OsError
```

A [`WorkerPool`](#thread.WorkerPool) with `threads` threads, in the current pool. [`stop`](#thread.stop) it before that pool is freed.

**Parameters**

- `threads` — how many: 0 for one per processor ([`cpuCount`](#thread.cpuCount))
- `backlog` — how many jobs can wait before [`submit`](#thread.submit) waits for room (0 counts as 1)

**Throws**

- `OsError` — when the system can't start the threads, or make the locks; the threads started are stopped

### <a id="thread.submit"></a>`submit`

```ec
fn submit(p: WorkerPool, escaping flat job: () => None) throws StateError
```

Gives `job` to the threads of `p`: one runs it when it is free. It waits while `p` has its whole backlog waiting.

**Parameters**

- `job` — the function to run, which the pool keeps (`escaping`) and runs on another thread (`flat`), as [`spawn`](#thread.spawn) does

**Throws**

- `StateError` — when `p` was stopped

### `wait`

```ec
fn wait(p: WorkerPool)
```

Waits until every job given to `p` so far has finished. Jobs can be given again afterwards.

### <a id="thread.pending"></a>`pending`

```ec
fn pending(p: WorkerPool): Long
```

How many jobs given to `p` haven't finished: those waiting and those running.

### <a id="thread.stop"></a>`stop`

```ec
fn stop(p: WorkerPool) throws OsError
```

Lets the threads of `p` finish the jobs given so far, then ends them, and waits for that. [`submit`](#thread.submit) fails afterwards; a
second stop does nothing.

**Throws**

- `OsError` — when a thread can't be joined
