/**
 * Threads and the tools to share data between them: Mutex, RwLock, Cond, WaitGroup, Atomic and Channel.
 * Built on APR's apr_thread_*.h and apr_atomic.h.
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
 *         var a = Job { id = 1, total = 0 }
 *         val t = try thread.spawn(fn work, addrof(a))
 *         try thread.join(t)
 *         io.log(a.total)
 *         ret 0
 *
 * A thread runs a function given a pointer to its data. Lambdas don't capture variables, so put what the thread
 * needs in a record and pass its address. That data must live until the thread is done with it: join the
 * thread before the data's block ends (or allocate the data with `new`).
 *
 * Each thread starts with its own current pool, freed when its function returns, so its code can allocate as
 * usual. Locks, conditions and channels are allocated in the pool that is current when they are made, and are
 * destroyed with it.
 */
module "thread"
use "core:errors.ec"
use "core:apr/thread_proc.ec"
use "core:apr/thread_mutex.ec"
use "core:apr/thread_rwlock.ec"
use "core:apr/thread_cond.ec"
use "core:apr/atomic.ec"
use "core:c/unistd.ec"

// ---------------------------------------------------------------------------------------------------
// Threads

/** A running thread, from {@link spawn}. {@link join} or {@link detach} it. */
rec Thread as
    /** The APR thread. */
    handle: @apr_thread_t

/** What a thread started with data runs. */
priv rec Task as
    /** The function to run. */
    body: (@Any) => None
    /** What it is given. */
    data: @Any

/** What a thread started without data runs. */
priv rec PlainTask as
    /** The function to run. */
    body: () => None

/** The entry point of a thread running a {@link Task}, in a pool of its own. */
priv fn start(t: @apr_thread_t, arg: @Any): @Any do
    val task = arg !as @Task
    pool do
        task.body(task.data)
    apr_thread_exit(t, APR_SUCCESS)
    ret arg

/** The entry point of a thread running a {@link PlainTask}, in a pool of its own. */
priv fn startPlain(t: @apr_thread_t, arg: @Any): @Any do
    val task = arg !as @PlainTask
    pool do
        task.body()
    apr_thread_exit(t, APR_SUCCESS)
    ret arg

/**
 * Starts a thread that runs `entry` with `task`.
 * @throws when the system can't start a thread
 */
priv fn launch(entry: (@apr_thread_t, @Any) => @Any, task: @Any): Thread throws OsError do
    var handle: @apr_thread_t? = null
    try checkStatus(apr_thread_create(addrof(handle!), null, entry, task, pool), "start a thread"c)
    ret Thread { handle! }

/**
 * Starts a thread that runs `body(data)`. The thread must be joined (or detached).
 * @param body the function to run
 * @param data what it is given; it must live until the thread is done with it
 * @return the new thread
 * @throws when the system can't start a thread
 */
fn spawn(body: (@<T>) => None, data: @<T>): Thread throws OsError do
    val task = new Task { body = body !as (@Any) => None, data = data }
    ret try launch(fn start, task)

/**
 * Starts a thread that runs `body()`. The thread must be joined (or detached).
 * @return the new thread
 * @throws when the system can't start a thread
 */
fn spawn(body: () => None): Thread throws OsError do
    val task = new PlainTask { body }
    ret try launch(fn startPlain, task)

/**
 * Waits until the thread has finished.
 * @throws when it can't be joined (it was detached, say)
 */
fn join(t: Thread) throws OsError do
    var result: apr_status_t = 0
    try checkStatus(apr_thread_join(addrof(result), t.handle), "join a thread"c)

/**
 * Lets the thread run on its own: it can't be joined, and its resources are freed when it ends.
 * @throws when it can't be detached
 */
fn detach(t: Thread) throws OsError do
    try checkStatus(apr_thread_detach(t.handle), "detach a thread"c)

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
rec Mutex as
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
    try checkStatus(apr_thread_mutex_create(addrof(handle!), flags as UInt, pool), "make a mutex"c)
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
rec RwLock as
    /** The APR lock. */
    handle: @apr_thread_rwlock_t

/**
 * A read-write lock in the current pool.
 * @throws when the system can't make one
 */
fn rwLock(): RwLock throws OsError do
    var handle: @apr_thread_rwlock_t? = null
    try checkStatus(apr_thread_rwlock_create(addrof(handle!), pool), "make a read-write lock"c)
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
rec Cond as
    /** The APR condition variable. */
    handle: @apr_thread_cond_t

/**
 * A condition variable in the current pool.
 * @throws when the system can't make one
 */
fn cond(): Cond throws OsError do
    var handle: @apr_thread_cond_t? = null
    try checkStatus(apr_thread_cond_create(addrof(handle!), pool), "make a condition variable"c)
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
rec WaitGroup as
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
fn waitGroup(): @WaitGroup throws OsError = new WaitGroup { 0, try mutex(), try cond() }

/** Adds `n` tasks to wait for. The program panics when the count goes below 0. */
fn add(g: @WaitGroup, n: Long) do
    lock(g.lock)
    g.count += n
    if g.count < 0 do
        unlock(g.lock)
        panic("thread.add: the WaitGroup count went below 0 (more `done` than `add`)")
    if g.count == 0 do
        broadcast(g.zero)
    unlock(g.lock)

/** One task is finished. The program panics when that is more than were added. */
fn done(g: @WaitGroup) do
    add(g, -1)

/** Waits until every task added is done. */
fn wait(g: @WaitGroup) do
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
 *     thread.increment(addrof(hits))               // from any thread
 *     io.log(thread.load(addrof(hits)))
 */
rec Atomic as
    /** The number. Use the functions below, not this field, once threads share it. */
    value: UInt

/** APR's atomics must be set up once before use. */
priv val atomicsReady = apr_atomic_init(pool)

/** The value now. */
fn load(a: @Atomic): UInt = apr_atomic_read32(addrof(a.value))

/** Sets the value. */
fn store(a: @Atomic, value: UInt) do
    apr_atomic_set32(addrof(a.value), value)

/**
 * Adds `n`.
 * @return the value before
 */
fn add(a: @Atomic, n: UInt): UInt = apr_atomic_add32(addrof(a.value), n)

/** Subtracts `n`. */
fn sub(a: @Atomic, n: UInt) do
    apr_atomic_sub32(addrof(a.value), n)

/**
 * Adds 1.
 * @return the value before
 */
fn increment(a: @Atomic): UInt = apr_atomic_inc32(addrof(a.value))

/**
 * Subtracts 1.
 * @return whether the value is now something other than 0
 */
fn decrement(a: @Atomic): Bool = apr_atomic_dec32(addrof(a.value)) != 0

/**
 * Sets the value to `with`.
 * @return the value before
 */
fn exchange(a: @Atomic, with: UInt): UInt = apr_atomic_xchg32(addrof(a.value), with)

/**
 * Sets the value to `with` if it is `expected`.
 * @return whether it was `expected`
 */
fn compareAndSwap(a: @Atomic, expected: UInt, with: UInt): Bool = apr_atomic_cas32(addrof(a.value), with, expected) == expected

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
rec Channel as
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
fn channel(capacity: Size): @Channel throws OsError do
    val items = new (@Any?)[if capacity == 0 do 1 else capacity]
    ret new Channel { items, 0, 0, false, try mutex(), try cond(), try cond() }

/**
 * Puts `item` at the end, waiting while the channel is full.
 * @throws when the channel is closed
 */
fn send(c: @Channel, item: @Any) throws StateError do
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
fn trySend(c: @Channel, item: @Any): Bool do
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
fn receive(c: @Channel): @Any? do
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
fn tryReceive(c: @Channel): @Any? do
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
fn close(c: @Channel) do
    lock(c.lock)
    c.closed = true
    broadcast(c.notEmpty)
    broadcast(c.notFull)
    unlock(c.lock)

/** The number of items waiting in the channel. */
fn size(c: @Channel): Size do
    lock(c.lock)
    val n = c.count
    unlock(c.lock)
    ret n
