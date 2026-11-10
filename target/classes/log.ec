/**
 * Logging: levels, timestamps, and output to stderr or a file, from any thread.
 *
 *     use "core:log.ec"
 *     log.info("started")                                 // 2026-03-14T09:26:53.589Z INFO  started
 *     log.warn("disk ${percent}% full")
 *     log.error(problem)                                  // a message, or an Error
 *
 *     log.setLevel(:debug)                                // :debug, :info (the first setting), :warn, :error or :off
 *     try log.toFile("app.log")                           // adds to the end of the file, and makes it if needed
 *     log.toStderr()                                      // back to where it started
 *     log.setTimestamps(:local)                           // :utc (the first setting), :local or :none
 *     log.setLocations(true)                              // ... INFO  main.ec:12: started
 *
 *     val db = log.logger("db")                           // named, with its own settings
 *     log.info(db, "connected")                           // ... INFO  db: connected
 *     val queries = log.child(db, "queries")              // writes where db does
 *     log.debug(queries, "SELECT 1")                      // ... DEBUG db.queries: SELECT 1
 *     log.debug("state: ${expensive()}") if log.enabled(:debug)
 *
 * Each line is written whole, so the lines of several threads don't mix, also with other processes adding to the same
 * file. Logging can't fail: when a file can't be written, the line goes to stderr, to say so. Lines aren't held back,
 * so they are there if the program crashes right after.
 *
 * The functions without a {@link Logger} use one that lives as long as the program. Another one belongs to the pool
 * that was current when it was made, and so does its file. A message is made before it gets here, even when its level is
 * off, so one that takes work to make needs {@link enabled}.
 */
module "log"
use "core:errors.ec"
use "core:string.ec"
use "core:time.ec"
use "core:thread.ec"
use "core:io.ec"
use "core:apr/file_io.ec"

/**
 * How much a message matters, from `:debug` to `:error`. A {@link Logger} writes the messages of its level and above;
 * `:off` writes none.
 */
declare type Level as :debug | :info | :warn | :error | :off

/** What is at the start of each line: the time in UTC, the local time, or nothing. */
declare type Timestamps as :utc | :local | :none

/** Where lines go. Loggers can share one. */
priv mut rec Sink as
    /** Held while anything here, or a setting of a {@link Logger} writing here, is used. */
    lock: thread.Mutex
    /** The open file, or null for stderr. */
    file: @apr_file_t?
    /** The path of that file. */
    path: String
    /** What {@code filePool} is a child of. */
    memory: Pool
    /** The pool the open file is in, which is freed when another file takes its place. */
    filePool: Pool?

/** Writes messages somewhere: {@link logger}. Threads can share one (it is `sync`): its {@link Sink}'s lock guards what it writes and its settings. */
mut sync ref rec Logger as
    /** What is before each message, or "" for nothing. */
    name: String
    /** The lowest level it writes. Change it with {@link setLevel}. */
    level: Level
    /** What it writes at the start of each line. Change it with {@link setTimestamps}. */
    timestamps: Timestamps
    /** Where its lines go. */
    sink: @Sink
    /** Whether it writes where each message comes from ("main.ec:12"). Change it with {@link setLocations}. */
    locations: Bool = false

/** A new {@link Sink} for stderr, in the current pool. */
priv fn newSink(): @Sink do
    val lock = thread.mutex() ?? panic("log: the system can't make a mutex")
    ret new Sink { lock, null, "", new Pool, null }

/**
 * A new logger writing to stderr, with the level `:info`, which belongs to the current pool.
 * @param name what is before each message; "" for nothing
 */
fn logger(name: String = ""): Logger = new Logger { name, :info, :utc, newSink() }

/**
 * A logger that writes where `parent` does, with the same settings at first. Those are its own after that, except for the
 * output: {@link toFile} on one of them changes it for both.
 * @param parent the logger
 * @param name its name, which comes after the name of `parent`, with a dot
 */
fn child(parent: Logger, name: String): Logger do
    thread.lock(parent.sink.lock)
    defer thread.unlock(parent.sink.lock)
    val full = if parent.name.len == 0 do name else concat(parent.name, ".", name)
    ret new Logger { full, parent.level, parent.timestamps, parent.sink, parent.locations }

/** The logger of the functions that aren't given one. */
priv val DEFAULT = logger()

// ---------------------------------------------------------------------------------------------------
// Levels

/** Where `level` is in the order of the levels. */
priv fn rank(level: Level): Int do
    when level do
        :debug do ret 0
        :info do ret 1
        :warn do ret 2
        :error do ret 3
        :off do ret 4

/** The name of `level` in a line, always 5 wide. */
priv fn label(level: Level): String do
    when level do
        :debug do ret "DEBUG"
        :info do ret "INFO "
        :warn do ret "WARN "
        :error do ret "ERROR"
        :off do ret "OFF  "

/**
 * The level called `s`, in any case, for reading it from a setting: `parseLevel("warn")`. "warning" is `:warn`.
 * @throws when there is no such level
 */
fn parseLevel(s: String): Level throws ParseError do
    val name = s.toLower()
    ret :debug if name == "debug"
    ret :info if name == "info"
    ret :warn if name == "warn" or name == "warning"
    ret :error if name == "error"
    ret :off if name == "off"
    throw parseError("'${s}' is not a log level: the levels are debug, info, warn, error and off", s, 0)

/** Sets the lowest level `l` writes. */
fn setLevel(l: Logger, level: Level) do
    thread.lock(l.sink.lock)
    defer thread.unlock(l.sink.lock)
    l.level = level

/** Sets the lowest level that is written. */
fn setLevel(level: Level) do
    setLevel(DEFAULT, level)

/** Whether `l` would write a message of `level`, to skip making one that takes work. */
fn enabled(l: Logger, level: Level): Bool do
    thread.lock(l.sink.lock)
    defer thread.unlock(l.sink.lock)
    ret level != :off and rank(level) >= rank(l.level)

/** Whether a message of `level` would be written, to skip making one that takes work. */
fn enabled(level: Level): Bool = enabled(DEFAULT, level)

/** Sets what `l` writes at the start of each line. */
fn setTimestamps(l: Logger, timestamps: Timestamps) do
    thread.lock(l.sink.lock)
    defer thread.unlock(l.sink.lock)
    l.timestamps = timestamps

/** Sets what is written at the start of each line. */
fn setTimestamps(timestamps: Timestamps) do
    setTimestamps(DEFAULT, timestamps)

/** Sets whether `l` writes where each message comes from: the file and line of the call that logs it, after the level. */
fn setLocations(l: Logger, on: Bool) do
    thread.lock(l.sink.lock)
    defer thread.unlock(l.sink.lock)
    l.locations = on

/** Sets whether each line says where its message comes from: the file and line of the call that logs it, after the level. */
fn setLocations(on: Bool) do
    setLocations(DEFAULT, on)

// ---------------------------------------------------------------------------------------------------
// Output

/** Closes the file of `s`, if it has one, so that lines go to stderr. The lock of `s` is held. */
priv fn closeFile(s: @Sink) do
    // The pool belongs to the Sink, whose lock is held, and only the file and its path are in it: both are reset here.
    unsafe destroy s.filePool
    s.file = null
    s.path = ""

/**
 * Makes `l`, and the loggers sharing its output ({@link child}), add their lines to the end of a file, which is made if it
 * doesn't exist. Writing to one file from several processes is fine.
 * @param l the logger
 * @param path where the file is
 * @throws when it can't be opened; lines then go where they went before
 */
fn toFile(l: Logger, path: String) throws IoError do
    val s = l.sink
    thread.lock(s.lock)
    defer thread.unlock(s.lock)
    val memory = new Pool in s.memory
    var handle: @apr_file_t? = null
    val status = apr_file_open(mut handle, cstr(path), APR_FOPEN_WRITE | APR_FOPEN_CREATE | APR_FOPEN_APPEND, APR_FPROT_OS_DEFAULT, memory)
    if status != APR_SUCCESS do
        destroy memory
        throw ioError(status, path)
    closeFile(s)
    s.filePool = memory
    s.file = handle
    s.path = clone(path, memory)

/**
 * Makes lines go to the end of a file; see the other `toFile`.
 * @param path where the file is
 * @throws when it can't be opened; lines then go where they went before
 */
fn toFile(path: String) throws IoError do
    try toFile(DEFAULT, path)

/** Makes `l`, and the loggers sharing its output, write to stderr. A file it wrote to is closed. */
fn toStderr(l: Logger) do
    thread.lock(l.sink.lock)
    defer thread.unlock(l.sink.lock)
    closeFile(l.sink)

/** Makes lines go to stderr. A file they went to is closed. */
fn toStderr() do
    toStderr(DEFAULT)

/** The time at the start of a line, with a space after it. */
priv fn stamp(timestamps: Timestamps): String do
    ret "" if timestamps == :none
    val now = time.now()
    val d = if timestamps == :local do time.local(now) else time.utc(now)
    val offset = if d.offset < 0 do -d.offset else d.offset
    val zone = if d.offset == 0 do format("Z") else format("${if d.offset < 0 do '-' else '+'}${offset / 3600:02}:${offset / 60 % 60:02}")
    ret "${d.year:04}-${d.month:02}-${d.day:02}T${d.hour:02}:${d.minute:02}:${d.second:02}.${d.micro / 1000:03}${zone} "

/** Writes `line` where `s` says. The lock of `s` is held. */
priv fn deliver(s: @Sink, line: String) do
    if val file = s.file do
        // One write, so that it stays whole next to other writers: the file isn't buffered.
        var written: Size = 0
        val status = apr_file_write_full(file, line.data, line.len, mut written)
        if status != APR_SUCCESS do
            val problem = ioError(status, s.path)
            io.eprintln("log: can't write to the file: ${problem.message}")
            io.eprint(line)
    else do
        io.eprint(line)

/**
 * Writes a message, if `level` is one that `l` writes.
 * @param l the logger
 * @param level how much it matters
 * @param message what to say
 * @param at where it comes from, written with {@link setLocations}: the call, unless it is given
 */
fn write(l: Logger, level: Level, message: String, at: Location = caller) do
    thread.lock(l.sink.lock)
    defer thread.unlock(l.sink.lock)
    if level != :off and rank(level) >= rank(l.level) do
        pool do
            val place = if l.locations do format("${at}: ") else ""
            val name = if l.name.len == 0 do "" else concat(l.name, ": ")
            deliver(l.sink, concat(stamp(l.timestamps), label(level), " ", place, name, message, "\n"))

/** Writes a message of the level `:debug`: details that are only of use when looking for a problem. */
fn debug(message: String, at: Location = caller) do
    write(DEFAULT, :debug, message, at)

/** Writes a message of the level `:info`: what the program is doing. */
fn info(message: String, at: Location = caller) do
    write(DEFAULT, :info, message, at)

/** Writes a message of the level `:warn`: something is wrong, but the program can go on. */
fn warn(message: String, at: Location = caller) do
    write(DEFAULT, :warn, message, at)

/** Writes a message of the level `:error`: something has failed. */
fn error(message: String, at: Location = caller) do
    write(DEFAULT, :error, message, at)

/** Writes an Error, of any kind, as a message of the level `:error`. */
fn error(e: Error, at: Location = caller) do
    write(DEFAULT, :error, describe(e), at)

/** Writes a message of the level `:debug` to `l`. */
fn debug(l: Logger, message: String, at: Location = caller) do
    write(l, :debug, message, at)

/** Writes a message of the level `:info` to `l`. */
fn info(l: Logger, message: String, at: Location = caller) do
    write(l, :info, message, at)

/** Writes a message of the level `:warn` to `l`. */
fn warn(l: Logger, message: String, at: Location = caller) do
    write(l, :warn, message, at)

/** Writes a message of the level `:error` to `l`. */
fn error(l: Logger, message: String, at: Location = caller) do
    write(l, :error, message, at)

/** Writes an Error, of any kind, as a message of the level `:error` to `l`. */
fn error(l: Logger, e: Error, at: Location = caller) do
    write(l, :error, describe(e), at)
