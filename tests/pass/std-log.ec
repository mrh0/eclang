use "core:io.ec"
use "core:string.ec"
use "core:structures.ec"
use "core:thread.ec"
use "core:log.ec"

/** What a thread is told. */
rec Job as
    l: @log.Logger
    id: Int

const LINES = 500
const THREADS = 8

fn work(job: @Job) do
    val filler = repeat("x", 40)
    for i in 0..<LINES do
        log.info(job.l, "thread ${job.id} line ${i} ${filler}")

/** Whether `s` looks like 2026-03-14T09:26:53.589Z, with a space after it. */
fn isStamp(s: String): Bool do
    ret false if s.len != 25
    for i, c in s do
        val wanted = "dddd-dd-ddTdd:dd:dd.dddZ "[i]
        ret false if wanted == 'd' and not isDigit(c)
        ret false if wanted != 'd' and c != wanted
    ret true

fn main(): Int throws Error do
    val dir = try io.tempDir()
    val path = io.joinPath(dir, "eclang-std-log.log")
    if io.exists(path) do
        try io.remove(path)

    // Levels, and what a line looks like.
    val l = log.logger("app")
    try log.toFile(l, path)
    log.setTimestamps(l, :none)
    log.debug(l, "hidden")
    log.info(l, "shown")
    log.warn(l, "careful")
    log.error(l, "broken")
    log.error(l, parseError("bad", "x", 0))
    log.setLevel(l, :debug)
    log.debug(l, "now visible")
    log.setLevel(l, :warn)
    log.info(l, "hidden")
    log.warn(l, "still here")
    log.setLevel(l, :off)
    log.error(l, "hidden")
    log.write(l, :off, "hidden")
    log.setLevel(l, :info)
    log.write(l, :off, "never written")

    io.log(log.enabled(l, :debug))
    io.log(log.enabled(l, :info))
    io.log(log.enabled(l, :error))
    io.log(log.enabled(l, :off))

    // Children share the output, and keep settings of their own.
    val child = log.child(l, "db")
    val grandchild = log.child(child, "queries")
    log.setLevel(child, :error)
    log.info(child, "hidden")
    log.error(child, "from the child")
    log.info(grandchild, "from the grandchild")
    log.info(l, "the parent is still at :info")
    val unnamed = log.logger()
    log.setTimestamps(unnamed, :none)
    val named = log.child(unnamed, "first")
    try log.toFile(named, path)
    log.info(named, "no dot before the name")
    log.info(unnamed, "no name")
    log.toStderr(unnamed)

    log.toStderr(l)
    io.log(try io.readFile(path))
    try io.remove(path)

    // Timestamps.
    val stamped = log.logger()
    try log.toFile(stamped, path)
    log.info(stamped, "stamped")
    log.toStderr(stamped)
    val line = try io.readFile(path)
    io.log(isStamp(line[0..<25]))
    io.log(line[25..<line.len])
    try io.remove(path)
    log.setTimestamps(stamped, :local)
    try log.toFile(stamped, path)
    log.info(stamped, "stamped")
    log.toStderr(stamped)
    val local = try io.readFile(path)
    io.log(local.contains("T") and local.contains(" INFO  stamped\n"))
    try io.remove(path)

    // A file that can't be opened changes nothing.
    val kept = log.logger()
    log.setTimestamps(kept, :none)
    try log.toFile(kept, path)
    try log.toFile(kept, "/nonexistent/dir/x.log") catch e do
        io.log(e.code == ERR_NOT_FOUND)
        io.log(e.path)
    log.info(kept, "still the first file")
    // Another file takes its place, as often as needed, and the old one is closed.
    val other = concat(path, ".2")
    for i in 0..<3000 do
        try log.toFile(kept, other)
        try log.toFile(kept, path)
    log.toStderr(kept)
    io.log(try io.readFile(path))
    try io.remove(path)
    try io.remove(other)

    // Lines of several threads stay whole.
    val shared = log.logger("t")
    try log.toFile(shared, path)
    log.setTimestamps(shared, :none)
    val threads: @List<thread.Thread> = list()
    for i in 0..<THREADS do
        threads.push(try thread.spawn(fn work, new Job { shared, i as Int }))
    for t in threads do
        try thread.join(t)
    log.toStderr(shared)
    val lines = try io.readLines(path)
    io.log(lines.len)
    val seen = new Int[THREADS]
    for text in lines do
        val parts = text.split(" ")
        assert parts.len == 8 and parts[0] == "INFO" and parts[2] == "t:" and parts[7].len == 40, "a line isn't whole"
        val id = (try parseInt(parts[4])) as Size
        val n = try parseInt(parts[6])
        // Each thread's lines come in its own order.
        assert n as Int == seen[id], "a thread's lines are out of order"
        seen[id] += 1
    for count in seen do
        io.log(count)
    try io.remove(path)

    // Messages to stderr, from the logger of the whole program.
    io.flush()
    log.setTimestamps(:none)
    log.debug("hidden")
    log.info("to stderr")
    log.warn("to stderr too")
    log.error("and this")
    log.error(rangeError("out of range"))
    log.setLevel(:error)
    log.warn("hidden")
    io.log(log.enabled(:warn))
    log.setLevel(:debug)
    log.debug("visible")
    try log.toFile(path)
    log.info("in the file")
    log.toStderr()
    io.log(try io.readFile(path))
    try io.remove(path)

    // Reading a level.
    io.log((try log.parseLevel("WARNING")) as Atom)
    io.log((try log.parseLevel("debug")) as Atom)
    try log.parseLevel("loud") catch e2 do
        io.log(e2.message)
    ret 0
