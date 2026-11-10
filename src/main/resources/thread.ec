/**
 * Threads and the tools to share data between them: Mutex, RwLock, Cond, WaitGroup, Atomic, Queue and Channel.
 * Built on pthread.h, APR's apr_thread_*.h and apr_atomic.h.
 *
 *     use "core:thread.ec"
 *
 *     rec Job as
 *         id: Int
 *         total: Long
 *
 *     fn work(job: @Job) do                         // runs in the new thread
 *         for i in 0..<1000 do
 *             job.total += i
 *
 *     fn main(): Int throws Error do
 *         val a = new Job { id = 1, total = 0 }
 *         val t = try thread.spawn(fn work, a)
 *         try thread.join(t)
 *         io.log(a.total)
 *         ret 0
 *
 * A thread runs a function given a pointer to its data. Lambdas don't capture variables, so put what the thread
 * needs in a record and pass a pointer to it. The compiler checks that the data outlives the function starting the
 * thread: it can't be a local variable (`addrof x`), nor be in a `pool do` block or a pool that function made, so
 * allocate it with `new` in the current pool. The data is also `flat`: it holds numbers, records of them, and pointers to
 * `sync` records, which their own functions make safe to share (a {@link Mutex}, an {@link Atomic}, a {@link Queue}), but no
 * Strings, arrays, Lists or pointers to other records, which this thread might free or change while the other reads them.
 * Share mutable state through a `sync rec` of your own that guards its fields with a Mutex, and hand work over in a
 * {@link Queue}, which copies its items. Each {@link Thread} must be joined or detached: dropping one, or never
 * using a variable that holds one, is an error.
 *
 * Each thread starts with its own current pool, freed when its function returns, so its code can allocate as
 * usual. Locks, conditions and channels are allocated in the pool that is current when they are made, and are
 * destroyed with it.
 */
module "thread"
use "core:errors.ec"
use "core:apr/thread_proc.ec"
use "core:c/pthread.ec"
use "core:c/stdlib.ec"
use "core:apr/thread_mutex.ec"
use "core:apr/thread_rwlock.ec"
use "core:apr/thread_cond.ec"
use "core:apr/atomic.ec"
use "core:c/unistd.ec"

// ---------------------------------------------------------------------------------------------------
// Threads

/** What {@link join} and {@link detach} know about a thread. Only the code holding the {@link Thread} uses it, never the thread itself. */
priv mut rec Ending as
    /** The thread was joined or detached, so its handle can't be used any more. */
    done: Bool

/** A running thread, from {@link spawn}. {@link join} or {@link detach} it: it is a `resource`, so it can't be dropped. */
resource rec Thread as
    /** The system's thread. */
    handle: pthread_t
    /** Whether it was joined or detached. */
    ending: @Ending

/** What a thread runs, with what it is given (null for a function that takes nothing). Allocated with `malloc`, not in a pool: the thread can outlive every pool of the code starting it. */
priv rec Task as
    /** The function to run, when it takes data. */
    body: ((@Any) => None)?
    /** The function to run, when it takes nothing. */
    plain: (() => None)?
    /** What `body` is given. */
    data: @Any?

/** The entry point of a thread, in a pool of its own. It frees its {@link Task}. */
priv fn start(arg: @Any): @Any? do
    val task = @(arg !as @Task)
    free(arg)
    pool do
        if val body = task.body do
            body(task.data!)
        else if val plain = task.plain do
            plain()
    ret null

/**
 * Starts a thread that runs `task`.
 * @throws when the system can't start a thread
 */
priv fn launch(task: Task): Thread throws OsError do
    val copy = (malloc(sizeof Task)! !as @Task)
    @copy = task
    var handle: pthread_t = 0
    val status = pthread_create(mut handle, null, fn start, copy)
    if status != 0 do
        free(copy)
    try checkStatus(status as apr_status_t, "start a thread"c)
    ret Thread { handle, new Ending { false } }

/**
 * Starts a thread that runs `body(data)`. The thread must be joined (or detached).
 * @param body the function to run, which the thread keeps (`escaping`): a lambda that uses a `var` of the caller can't be given,
 * and what a lambda captures must be `flat`
 * @param data what it is given, which the thread keeps (`escaping`): not a local variable, nor in a pool the caller frees. It is
 * `flat`: what it points to holds numbers, records of them, and pointers to `sync` records (a {@link Mutex}, a {@link Queue}, a
 * `sync rec` of your own), but no Strings, arrays, Lists or other pointers, which could reach memory this thread frees or changes
 * @return the new thread
 * @throws when the system can't start a thread
 */
fn spawn(escaping flat body: (@<T>) => None, escaping flat data: @<T>): Thread throws OsError do
    ret try launch(Task { body = body !as (@Any) => None, plain = null, data = data })

/**
 * Starts a thread that runs `body()`. The thread must be joined (or detached).
 * @param body the function to run, which the thread keeps (`escaping`). A lambda can use the caller's `val`s (it copies them, in the
 * current pool) when they are `flat` (numbers, records of them, pointers to `sync` records), but not its `var`s, which it would
 * share with a function that may return before the thread ends
 * @return the new thread
 * @throws when the system can't start a thread
 */
fn spawn(escaping flat body: () => None): Thread throws OsError do
    ret try launch(Task { body = null, plain = body, data = null })

/** Marks the thread as joined or detached: EINVAL if it already is, as its handle may be gone. */
priv fn claim(t: Thread): apr_status_t do
    if t.ending.done do
        ret APR_EINVAL
    t.ending.done = true
    ret APR_SUCCESS

/**
 * Waits until the thread has finished.
 * @throws when it can't be joined (it was detached or joined before, say)
 */
fn join(t: Thread) throws OsError do
    try checkStatus(claim(t), "join a thread"c)
    try checkStatus(pthread_join(t.handle, null) as apr_status_t, "join a thread"c)

/**
 * Lets the thread run on its own: it can't be joined, and its resources are freed when it ends.
 * @throws when it can't be detached
 */
fn detach(t: Thread) throws OsError do
    try checkStatus(claim(t), "detach a thread"c)
    try checkStatus(pthread_detach(t.handle) as apr_status_t, "detach a thread"c)

/** Lets other threads run before this one goes on. */
fn yieldNow() do
    apr_thread_yield()

/** The number of processors that are online (at least 1). */
fn cpuCount(): Int do
    val n = sysconf(_SC_NPROCESSORS_ONLN)
    ret if n < 1 do 1 else n as Int

/**
 * Stops the program with a message when a lock call fails: that only happens when it's used wrongly (such as
 * unlocking a mutex this thread doesn't hold), so it isn't an error to handle.
 */
priv fn must(status: apr_status_t, what: String) do
    if status != APR_SUCCESS do
        panic("thread.${what}: ${statusMessage(status)}")

// ---------------------------------------------------------------------------------------------------
// Mutex

/**
 * A lock for one thread at a time.
 *
 *     val m = try thread.mutex()
 *     thread.lock(m)
 *     defer thread.unlock(m)
 */
sync rec Mutex as
    /** The APR mutex. */
    handle: @apr_thread_mutex_t

/**
 * A mutex in the current pool.
 * @param nested whether the thread holding it can lock it again (and must unlock it as many times)
 * @throws when the system can't make one
 */
fn mutex(nested: Bool = false): Mutex throws OsError do
    var handle: @apr_thread_mutex_t? = null
    val flags = if nested do APR_THREAD_MUTEX_NESTED else APR_THREAD_MUTEX_DEFAULT
    try checkStatus(apr_thread_mutex_create(mut handle, flags as UInt, pool), "make a mutex"c)
    ret Mutex { handle! }

/** Waits until no other thread holds `m`, then holds it. */
fn lock(m: Mutex) do
    must(apr_thread_mutex_lock(m.handle), "lock")

/**
 * Holds `m` if no other thread does, without waiting.
 * @return whether it now holds `m`
 */
fn tryLock(m: Mutex): Bool do
    val status = apr_thread_mutex_trylock(m.handle)
    ret true if status == APR_SUCCESS
    ret false if APR_STATUS_IS_EBUSY(status) != 0
    must(status, "tryLock")
    ret false

/** Lets go of `m`, which this thread must hold. */
fn unlock(m: Mutex) do
    must(apr_thread_mutex_unlock(m.handle), "unlock")

// ---------------------------------------------------------------------------------------------------
// RwLock

/** A lock for many readers at a time, or one writer. */
sync rec RwLock as
    /** The APR lock. */
    handle: @apr_thread_rwlock_t

/**
 * A read-write lock in the current pool.
 * @throws when the system can't make one
 */
fn rwLock(): RwLock throws OsError do
    var handle: @apr_thread_rwlock_t? = null
    try checkStatus(apr_thread_rwlock_create(mut handle, pool), "make a read-write lock"c)
    ret RwLock { handle! }

/** Waits until no thread writes, then holds `l` for reading. */
fn readLock(l: RwLock) do
    must(apr_thread_rwlock_rdlock(l.handle), "readLock")

/** Waits until no thread reads or writes, then holds `l` for writing. */
fn writeLock(l: RwLock) do
    must(apr_thread_rwlock_wrlock(l.handle), "writeLock")

/**
 * Holds `l` for reading if no thread writes, without waiting.
 * @return whether it now holds `l`
 */
fn tryReadLock(l: RwLock): Bool do
    val status = apr_thread_rwlock_tryrdlock(l.handle)
    ret true if status == APR_SUCCESS
    ret false if APR_STATUS_IS_EBUSY(status) != 0
    must(status, "tryReadLock")
    ret false

/**
 * Holds `l` for writing if no thread reads or writes, without waiting.
 * @return whether it now holds `l`
 */
fn tryWriteLock(l: RwLock): Bool do
    val status = apr_thread_rwlock_trywrlock(l.handle)
    ret true if status == APR_SUCCESS
    ret false if APR_STATUS_IS_EBUSY(status) != 0
    must(status, "tryWriteLock")
    ret false

/** Lets go of a read or write lock. */
fn unlock(l: RwLock) do
    must(apr_thread_rwlock_unlock(l.handle), "unlock")

// ---------------------------------------------------------------------------------------------------
// Cond

/**
 * Lets threads wait until another thread says something changed. Always wait in a loop that checks the
 * condition, holding the mutex that protects it:
 *
 *     thread.lock(m)
 *     while queue.count == 0 do
 *         thread.wait(c, m)
 *     ...
 *     thread.unlock(m)
 */
sync rec Cond as
    /** The APR condition variable. */
    handle: @apr_thread_cond_t

/**
 * A condition variable in the current pool.
 * @throws when the system can't make one
 */
fn cond(): Cond throws OsError do
    var handle: @apr_thread_cond_t? = null
    try checkStatus(apr_thread_cond_create(mut handle, pool), "make a condition variable"c)
    ret Cond { handle! }

/** Lets go of `m`, waits for a signal, and holds `m` again. It may also wake without a signal. */
fn wait(c: Cond, m: Mutex) do
    must(apr_thread_cond_wait(c.handle, m.handle), "wait")

/**
 * Like {@link wait}, but with a time limit.
 * @param micros how many microseconds to wait at most
 * @return false when the time ran out
 */
fn waitFor(c: Cond, m: Mutex, micros: Long): Bool do
    val status = apr_thread_cond_timedwait(c.handle, m.handle, micros)
    ret true if status == APR_SUCCESS
    ret false if APR_STATUS_IS_TIMEUP(status) != 0
    must(status, "waitFor")
    ret false

/** Wakes one waiting thread. */
fn signal(c: Cond) do
    must(apr_thread_cond_signal(c.handle), "signal")

/** Wakes every waiting thread. */
fn broadcast(c: Cond) do
    must(apr_thread_cond_broadcast(c.handle), "broadcast")

// ---------------------------------------------------------------------------------------------------
// WaitGroup

/**
 * Waits for a number of tasks to finish.
 *
 *     val g = try thread.waitGroup()
 *     thread.add(g, 3)
 *     ... each of the 3 workers calls thread.done(g) when it is finished ...
 *     thread.wait(g)
 */
mut sync ref rec WaitGroup as
    /** How many tasks aren't done yet. */
    count: Long
    /** Protects {@code count}. */
    lock: Mutex
    /** Signalled when {@code count} reaches 0. */
    zero: Cond

/**
 * A WaitGroup with no tasks, in the current pool.
 * @throws when the system can't make its lock
 */
fn waitGroup(): WaitGroup throws OsError = new WaitGroup { 0, try mutex(), try cond() }

/** Adds `n` tasks to wait for. The program panics when the count goes below 0. */
fn add(g: WaitGroup, n: Long) do
    lock(g.lock)
    g.count += n
    if g.count < 0 do
        unlock(g.lock)
        panic("thread.add: the WaitGroup count went below 0 (more `done` than `add`)")
    if g.count == 0 do
        broadcast(g.zero)
    unlock(g.lock)

/** One task is finished. The program panics when that is more than were added. */
fn done(g: WaitGroup) do
    add(g, -1)

/** Waits until every task added is done. */
fn wait(g: WaitGroup) do
    lock(g.lock)
    while g.count > 0 do
        wait(g.zero, g.lock)
    unlock(g.lock)

// ---------------------------------------------------------------------------------------------------
// Atomic

/**
 * A UInt that threads can change at the same time without a lock.
 *
 *     var hits = thread.Atomic { 0 }
 *     thread.increment(mut hits)               // from any thread
 *     io.log(thread.load(mut hits))
 */
sync rec Atomic as
    /** The number. Use the functions below, not this field, once threads share it. */
    value: UInt

/** APR's atomics must be set up once before use. */
priv val atomicsReady = apr_atomic_init(pool)

/** The value now. */
fn load(mut a: Atomic): UInt = apr_atomic_read32(addrof(a.value))

/** Sets the value. */
fn store(mut a: Atomic, value: UInt) do
    apr_atomic_set32(addrof(a.value), value)

/**
 * Adds `n`.
 * @return the value before
 */
fn add(mut a: Atomic, n: UInt): UInt = apr_atomic_add32(addrof(a.value), n)

/** Subtracts `n`. */
fn sub(mut a: Atomic, n: UInt) do
    apr_atomic_sub32(addrof(a.value), n)

/**
 * Adds 1.
 * @return the value before
 */
fn increment(mut a: Atomic): UInt = apr_atomic_inc32(addrof(a.value))

/**
 * Subtracts 1.
 * @return whether the value is now something other than 0
 */
fn decrement(mut a: Atomic): Bool = apr_atomic_dec32(addrof(a.value)) != 0

/**
 * Sets the value to `with`.
 * @return the value before
 */
fn exchange(mut a: Atomic, with: UInt): UInt = apr_atomic_xchg32(addrof(a.value), with)

/**
 * Sets the value to `with` if it is `expected`.
 * @return whether it was `expected`
 */
fn compareAndSwap(mut a: Atomic, expected: UInt, with: UInt): Bool = apr_atomic_cas32(addrof(a.value), with, expected) == expected

// ---------------------------------------------------------------------------------------------------
// Channel

/**
 * A queue of pointers, to hand work from one thread to another. {@link send} waits while the channel is full,
 * {@link receive} waits while it is empty. After {@link close}, `receive` gives what is left and then null.
 *
 *     val jobs = try thread.channel(16)
 *     try thread.send(jobs, addrof(job))           // producer
 *     val next = thread.receive(jobs)              // consumer: a @Any?, null when closed
 *     if next != null do
 *         val job = next !as @Job
 */
mut sync ref rec Channel as
    /** The ring of items. */
    items: (@Any?)[]
    /** The index in {@code items} of the first item. */
    head: Size
    /** How many items it holds. */
    count: Size
    /** Whether {@link close} was called. */
    closed: Bool
    /** Protects the other fields. */
    lock: Mutex
    /** Signalled when an item arrives. */
    notEmpty: Cond
    /** Signalled when there is room for an item. */
    notFull: Cond

/**
 * A channel in the current pool.
 * @param capacity how many items it holds (0 counts as 1)
 * @throws when the system can't make its lock
 */
fn channel(capacity: Size): Channel throws OsError do
    val items = new (@Any?)[if capacity == 0 do 1 else capacity]
    ret new Channel { items, 0, 0, false, try mutex(), try cond(), try cond() }

/**
 * Puts `item` at the end, waiting while the channel is full.
 * @throws when the channel is closed
 */
fn send(c: Channel, item: @Any) throws StateError do
    lock(c.lock)
    while c.count == c.items.len and not c.closed do
        wait(c.notFull, c.lock)
    if c.closed do
        unlock(c.lock)
        throw stateError("can't send on a closed channel")
    c.items[(c.head + c.count) % c.items.len] = item
    c.count += 1
    signal(c.notEmpty)
    unlock(c.lock)

/**
 * Puts `item` at the end if there is room, without waiting.
 * @return whether it did: false when the channel is full or closed
 */
fn trySend(c: Channel, item: @Any): Bool do
    lock(c.lock)
    val ok = c.count < c.items.len and not c.closed
    if ok do
        c.items[(c.head + c.count) % c.items.len] = item
        c.count += 1
        signal(c.notEmpty)
    unlock(c.lock)
    ret ok

/**
 * Takes the first item, waiting while the channel is empty.
 * @return the item; null when the channel is closed and empty
 */
fn receive(c: Channel): @Any? do
    lock(c.lock)
    while c.count == 0 and not c.closed do
        wait(c.notEmpty, c.lock)
    var item: @Any? = null
    if c.count > 0 do
        item = c.items[c.head]
        c.items[c.head] = null
        c.head = (c.head + 1) % c.items.len
        c.count -= 1
        signal(c.notFull)
    unlock(c.lock)
    ret item

/**
 * Takes the first item if there is one, without waiting.
 * @return the item, or null when the channel is empty
 */
fn tryReceive(c: Channel): @Any? do
    lock(c.lock)
    var item: @Any? = null
    if c.count > 0 do
        item = c.items[c.head]
        c.items[c.head] = null
        c.head = (c.head + 1) % c.items.len
        c.count -= 1
        signal(c.notFull)
    unlock(c.lock)
    ret item

/**
 * No more items can be sent. Threads waiting in {@link receive} get what is left, then null; threads waiting in {@link send} get a StateError.
 */
fn close(c: Channel) do
    lock(c.lock)
    c.closed = true
    broadcast(c.notEmpty)
    broadcast(c.notFull)
    unlock(c.lock)

/** The number of items waiting in the channel. */
fn size(c: Channel): Size do
    lock(c.lock)
    val n = c.count
    unlock(c.lock)
    ret n

// ---------------------------------------------------------------------------------------------------
// Queue

/**
 * A queue of values, to hand work from one thread to another. Unlike a {@link Channel}, it holds copies: {@link send} copies the
 * item in, {@link receive} copies it out, so no thread is left with a pointer into another's memory. Its items are `flat`: numbers,
 * records of them, and pointers to `sync` records (another Queue, a {@link Mutex}), which the compiler checks at each `send`.
 * {@link send} waits while it is full, {@link receive} while it is empty. After {@link close}, `receive` gives what is left, then null.
 *
 *     rec Job as
 *         id: Int
 *         size: Long
 *
 *     val jobs: thread.Queue<Job> = try thread.queue(16)
 *     try thread.send(jobs, Job { 1, 100 })        // producer: a copy goes in
 *     val next = thread.receive(jobs)              // consumer: a Job?, null when closed
 *
 * @param <T> the type of the items
 */
mut sync ref rec Queue<T> as
    /** The ring of items. */
    items: T[]
    /** The index in {@code items} of the first item. */
    head: Size
    /** How many items it holds. */
    count: Size
    /** Whether {@link close} was called. */
    closed: Bool
    /** Protects the other fields. */
    lock: Mutex
    /** Signalled when an item arrives. */
    notEmpty: Cond
    /** Signalled when there is room for an item. */
    notFull: Cond

/**
 * An empty queue in the current pool: `val q: thread.Queue<Int> = try thread.queue(16)`.
 * @param capacity how many items it holds (0 counts as 1)
 * @throws when the system can't make its lock
 */
fn queue(capacity: Size): Queue<T> throws OsError do
    val items = unsafe new (<T>)[if capacity == 0 do 1 else capacity]
    ret new Queue<T> { items, 0, 0, false, try mutex(), try cond(), try cond() }

/** Puts a copy of `item` at the end; the lock is held. */
priv fn putLocked(q: Queue<T>, item: <T>) do
    q.items[(q.head + q.count) % q.items.len] = item
    q.count += 1
    signal(q.notEmpty)

/** Takes the first item out; the lock is held and there is one. */
priv fn takeLocked(q: Queue<T>): <T> do
    val item = q.items[q.head]
    q.head = (q.head + 1) % q.items.len
    q.count -= 1
    signal(q.notFull)
    ret item

/**
 * Puts a copy of `item` at the end, waiting while the queue is full.
 * @param item what to send: `flat`, so that the thread taking it gets no pointer into this one's memory
 * @throws when the queue is closed
 */
fn send(q: Queue<T>, flat item: <T>) throws StateError do
    lock(q.lock)
    while q.count == q.items.len and not q.closed do
        wait(q.notFull, q.lock)
    if q.closed do
        unlock(q.lock)
        throw stateError("can't send on a closed queue")
    putLocked(q, item)
    unlock(q.lock)

/**
 * Puts a copy of `item` at the end if there is room, without waiting.
 * @return whether it did: false when the queue is full or closed
 */
fn trySend(q: Queue<T>, flat item: <T>): Bool do
    lock(q.lock)
    val ok = q.count < q.items.len and not q.closed
    if ok do
        putLocked(q, item)
    unlock(q.lock)
    ret ok

/**
 * Takes the first item, waiting while the queue is empty.
 * @return the item; null when the queue is closed and empty
 */
fn receive(q: Queue<T>): <T>? do
    lock(q.lock)
    while q.count == 0 and not q.closed do
        wait(q.notEmpty, q.lock)
    if q.count == 0 do
        unlock(q.lock)
        ret null
    val item = takeLocked(q)
    unlock(q.lock)
    ret item

/**
 * Takes the first item if there is one, without waiting.
 * @return the item, or null when the queue is empty
 */
fn tryReceive(q: Queue<T>): <T>? do
    lock(q.lock)
    if q.count == 0 do
        unlock(q.lock)
        ret null
    val item = takeLocked(q)
    unlock(q.lock)
    ret item

/**
 * No more items can be sent. Threads waiting in {@link receive} get what is left, then null; threads waiting in {@link send} get a StateError.
 */
fn close(q: Queue<T>) do
    lock(q.lock)
    q.closed = true
    broadcast(q.notEmpty)
    broadcast(q.notFull)
    unlock(q.lock)

/** The number of items waiting in the queue. */
fn size(q: Queue<T>): Size do
    lock(q.lock)
    val n = q.count
    unlock(q.lock)
    ret n
