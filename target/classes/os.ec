/**
 * The operating system: what system this is, the environment, users, and running other programs. Built on APR
 * (apr_env.h, apr_user.h, apr_thread_proc.h, apr_poll.h, ...), and on C's uname and sysconf for what APR doesn't have.
 *
 *     use "core:os.ec"
 *     io.log("${os.name()} on ${os.arch()}, ${os.cpuCount()} CPUs")     // Linux on x86_64, 8 CPUs
 *     io.log(try os.hostName())
 *     io.log((try os.user()).name)
 *
 *     try os.setEnv("LANG", "C")
 *     val home = os.env("HOME") ?? "/"
 *
 *     val r = try os.run("git", ["status", "--short"])     // waits, and gives what the program wrote
 *     io.print(r.stdout) if os.ok(r)
 *     val s = try os.shell("ls | wc -l")                    // a command line, run by /bin/sh
 *
 *     val p = try os.spawn("sort", ["-r"], :pipe)           // runs next to this program
 *     try io.writeLine(p.stdin!, "a")
 *     io.close(p.stdin!)
 *     io.print(try io.readAll(p.stdout!))
 *     val exit = try os.wait(p)
 *
 * What is returned is allocated in the current pool. Failures are OsErrors, with the reason. The environment
 * and the current directory belong to the whole program: don't change them while other threads use them.
 * See core:io.ec for `io.args`, `io.currentDir` and `io.tempDir`.
 */
module "os"
use "core:errors.ec"
use "core:string.ec"
use "core:io.ec"
use "core:c/signal.ec"
use "core:c/stdio.ec"
use "core:c/stdlib.ec"
use "core:c/unistd.ec"
use "core:c/utsname.ec"
use "core:apr/env.ec"
use "core:apr/file_info.ec"
use "core:apr/file_io.ec"
use "core:apr/network_io.ec"
use "core:apr/poll.ec"
use "core:apr/signal.ec"
use "core:apr/thread_proc.ec"
use "core:apr/user.ec"
use "core:apr/version.ec"

// ---------------------------------------------------------------------------------------------------
// The system

/** The fields of `uname`; they are empty if it fails, which it doesn't on a working system. */
priv fn systemName(): utsname do
    var u: utsname
    uname(mut u)
    ret u

/** The name of the operating system, such as "Linux" or "Darwin". */
fn name(): String do
    var u = systemName()
    ret fromC(u.sysname !as CString)

/** The release of the system's kernel, such as "6.8.0-45-generic". */
fn release(): String do
    var u = systemName()
    ret fromC(u.release !as CString)

/** The version of the system's kernel, as the system words it (often its build number and date). */
fn version(): String do
    var u = systemName()
    ret fromC(u.version !as CString)

/** The kind of processor the system runs on, such as "x86_64" or "aarch64". */
fn arch(): String do
    var u = systemName()
    ret fromC(u.machine !as CString)

/**
 * The name of this computer on the network.
 * @throws when the system doesn't tell
 */
fn hostName(): String throws OsError do
    val buffer = new Char[256]
    val status = apr_gethostname(buffer.data !as CString, buffer.len as Int, pool)
    throw osError(status, "get the host name") if status != APR_SUCCESS
    ret fromC(buffer.data !as CString)

/** How many processors are online; at least 1. */
fn cpuCount(): Int do
    val count = sysconf(_SC_NPROCESSORS_ONLN)
    ret if count < 1 do 1 else count as Int

/** The size of a page of memory, in bytes. */
fn pageSize(): Long = sysconf(_SC_PAGESIZE)

/** The amount of `pages` in bytes, or null for sysconf's -1 (not known). */
priv fn pagesInBytes(pages: Long): Long? do
    ret null if pages < 0
    ret pages * pageSize()

/** The memory of this computer, in bytes, or null when the system doesn't tell. */
fn totalMemory(): Long? = pagesInBytes(sysconf(_SC_PHYS_PAGES))

/** The memory nothing is using right now, in bytes, or null when the system doesn't tell. */
fn freeMemory(): Long? = pagesInBytes(sysconf(_SC_AVPHYS_PAGES))

/**
 * How busy the system is: the average number of processes running or waiting to run.
 * @return the averages over 1, 5 and 15 minutes; empty when the system doesn't tell
 */
fn loadAverage(): Double[] do
    val loads = new Double[3]
    val count = getloadavg(loads.data !as @Double, 3)
    ret if count < 1 do loads[0..<0] else loads[0..<(count as Size)]

/** The version of the APR library the program runs with, such as "1.7.6". */
fn aprVersion(): String = fromC(apr_version_string())

/**
 * The standard streams:
 *
 * - `:stdin`: what the program reads from
 * - `:stdout`: what it writes to
 * - `:stderr`: what it writes errors to
 */
declare type Stream as :stdin | :stdout | :stderr

/** Whether `stream` is a terminal, and not a file or a pipe: `os.isTerminal(:stdout)` tells if colors make sense. */
fn isTerminal(stream: Stream): Bool do
    when stream do
        :stdin do ret isatty(STDIN_FILENO) == 1
        :stdout do ret isatty(STDOUT_FILENO) == 1
        :stderr do ret isatty(STDERR_FILENO) == 1

// ---------------------------------------------------------------------------------------------------
// The environment

/** One environment variable, from {@link environment}. */
rec EnvVar as
    /** Its name. */
    name: String
    /** Its value. */
    value: String

/** The C array of "NAME=value" texts, ended by a null. Use {@link environment}. */
declare val environTable extern "__ec_environ" as CString?[]c

/** The value of the environment variable `name` (`os.env("HOME")`), or null when it isn't set. */
fn env(name: String): String? do
    var value: CString? = null
    ret null if apr_env_get(mut value, cstr(name), pool) != APR_SUCCESS
    if val found = value do
        ret fromC(found)
    ret null

/**
 * Sets the environment variable `name`, for this program and the programs it starts.
 * @throws when `name` isn't a valid name (it is empty or has a "="), or memory runs out
 */
fn setEnv(name: String, value: String) throws OsError do
    val status = apr_env_set(cstr(name), cstr(value), pool)
    throw osError(status, "set the environment variable '${name}'") if status != APR_SUCCESS

/**
 * Removes the environment variable `name`. It is no error when it isn't set.
 * @throws when `name` isn't a valid name (it is empty or has a "=")
 */
fn unsetEnv(name: String) throws OsError do
    val status = apr_env_delete(cstr(name), pool)
    throw osError(status, "remove the environment variable '${name}'") if status != APR_SUCCESS

/** All environment variables, in the order the system keeps them. */
fn environment(): EnvVar[] do
    var count: Size = 0
    // C ends the table with a null.
    while unsafe environTable[count] != null do
        count += 1
    val all = new EnvVar[count]
    for i in 0..<count do
        val entry = fromC((unsafe environTable[i])!)
        all[i] = EnvVar { entry.before("="), entry.after("=") }
    ret all

// ---------------------------------------------------------------------------------------------------
// Users

/** An account on this computer. */
rec User as
    /** The login name. */
    name: String
    /** The user's number. */
    id: UInt
    /** The number of the user's main group. */
    groupId: UInt
    /** The name of that group, or null when it has none. */
    group: String?
    /** The user's home directory. */
    home: String

/** The {@link User} called `name`, whose numbers are known. */
priv fn describeUser(name: String, id: UInt, groupId: UInt): User throws OsError do
    var home: CString = ""c
    val status = apr_uid_homepath_get(mut home, cstr(name), pool)
    throw osError(status, "find the home directory of '${name}'") if status != APR_SUCCESS
    var groupName: CString? = null
    val named = apr_gid_name_get(mut groupName, groupId, pool) == APR_SUCCESS
    val group: String? = if named do fromC(groupName!) else null
    ret User { name, id, groupId, group, fromC(home) }

/**
 * The user the program runs as.
 * @throws when the system has no account for it (as can happen in a container)
 */
fn user(): User throws OsError do
    var id: apr_uid_t = 0
    var groupId: apr_gid_t = 0
    val status = apr_uid_current(mut id, mut groupId, pool)
    throw osError(status, "find the current user") if status != APR_SUCCESS
    var name: CString = ""c
    val named = apr_uid_name_get(mut name, id, pool)
    throw osError(named, "find the name of user ${id}") if named != APR_SUCCESS
    ret try describeUser(fromC(name), id, groupId)

/**
 * The user called `name`.
 * @throws when there is no such user
 */
fn findUser(name: String): User throws OsError do
    var id: apr_uid_t = 0
    var groupId: apr_gid_t = 0
    val status = apr_uid_get(mut id, mut groupId, cstr(name), pool)
    throw osError(status, "find the user '${name}'") if status != APR_SUCCESS
    ret try describeUser(name, id, groupId)

/**
 * The home directory of the current user: the HOME environment variable, or else what the system's account says.
 * @throws when neither tells
 */
fn homeDir(): String throws OsError do
    if val home = env("HOME") do
        ret home if home.len > 0
    ret (try user()).home

// ---------------------------------------------------------------------------------------------------
// This program

/** The process id of this program. */
fn pid(): Int = getpid()

/** The process id of the program that started this one. */
fn parentPid(): Int = getppid()

/** C's exit: here because this module's own {@link exit} hides it. */
declare fn exitProgram extern "exit" as (status: Int): Never

/**
 * Ends the program with the exit code `code` (0 means success), after writing out what stdout buffers. Deferred
 * statements don't run.
 */
fn exit(code: Int): Never do
    exitProgram(code)

/**
 * Makes `path` the directory the program runs in (see `io.currentDir`). Relative paths mean something else after this.
 * @throws when it doesn't exist or isn't a directory
 */
fn changeDir(path: String) throws IoError do
    val status = apr_filepath_set(cstr(path), pool)
    throw ioError(status, path) if status != APR_SUCCESS

/**
 * The absolute path of this program's executable file. It reads /proc/self/exe, so it needs Linux.
 * @throws when the system doesn't tell
 */
fn executable(): String throws OsError do
    val buffer = new Char[4096]
    val length = readlink("/proc/self/exe"c, buffer.data !as CString, buffer.len)
    throw osError(apr_get_os_error(), "find this program's file") if length < 0
    ret clone(buffer[0..<(length as Size)] !as String)

// ---------------------------------------------------------------------------------------------------
// Signals

/** Words for the signal number `signal`, such as "Terminated" for SIGTERM. */
fn signalName(signal: Int): String = fromC(apr_signal_description_get(signal))

/**
 * Makes the program ignore a signal. `os.ignoreSignal(SIGPIPE)` is worth doing before writing to a program that may
 * have stopped: the write then fails with an IoError, where the signal would have ended this program.
 */
fn ignoreSignal(signal: Int) do
    apr_signal(signal, SIG_IGN)

// ---------------------------------------------------------------------------------------------------
// Running programs

/** How a program ended. */
rec Exit as
    /** Its exit code, 0 for success. A program stopped by a signal gets 128 plus the signal, as in a shell. */
    code: Int
    /** The signal that stopped it (see {@link signalName}), or 0 when it ended by itself. */
    signal: Int

/** How a program ended, and what it wrote, from {@link run} and {@link shell}. */
rec Output extends Exit as
    /** What it wrote to its standard output. */
    stdout: String
    /** What it wrote to its standard error. */
    stderr: String

/**
 * A running program, from {@link spawn}. {@link wait} for it: until then the system keeps its entry, even when
 * it has ended.
 */
rec Process as
    /** The APR process, for calling apr_proc_* functions directly. */
    handle: @apr_proc_t
    /** Its process id. */
    pid: Int
    /** Writes to its standard input, with `:pipe`. Close it to tell the program there's no more. */
    stdin: io.File?
    /** Reads its standard output, with `:pipe`. */
    stdout: io.File?
    /** Reads its standard error, with `:pipe`. */
    stderr: io.File?

/**
 * What a program started with {@link spawn} is connected to:
 *
 * - `:inherit`: this program's own standard streams
 * - `:pipe`: pipes, which {@link Process} has the other ends of
 */
declare type Stdio as :inherit | :pipe

/**
 * Finds the file a program name stands for, the way a shell does: in the directories of the PATH environment
 * variable, in their order. A name with a "/" in it is a path, and isn't looked up.
 * @return the path of the file, or null when there is none that can be run
 */
fn findProgram(program: String): String? do
    if program.contains("/") do
        ret if access(cstr(program), X_OK) == 0 do program else null
    for dir in (env("PATH") ?? "").split(':') do
        val path = io.joinPath(if dir.len == 0 do "." else dir, program)
        ret path if io.isFile(path) and access(cstr(path), X_OK) == 0
    ret null

/**
 * Starts a program.
 * @param kind how APR runs it
 * @param path the file to run
 * @param args what the program gets as its arguments, its own name first; for a shell, just the command line
 * @param input whether its standard input becomes a pipe
 * @param output whether its standard output and error become pipes
 * @param dir the directory to run it in, or null for this program's
 */
priv fn start(kind: apr_cmdtype_e, path: String, args: String[], input: Bool, output: Bool, dir: String?): Process throws OsError do
    val what = "start '${path}'"
    var attr: @apr_procattr_t? = null
    val made = apr_procattr_create(mut attr, pool)
    throw osError(made, what) if made != APR_SUCCESS
    val a = attr!
    val inMode = if input do APR_FULL_BLOCK else APR_NO_PIPE
    val outMode = if output do APR_FULL_BLOCK else APR_NO_PIPE
    try checkStatus(apr_procattr_io_set(a, inMode, outMode, outMode), "connect a program's streams"c)
    try checkStatus(apr_procattr_cmdtype_set(a, kind), "choose how to run a program"c)
    try checkStatus(apr_procattr_error_check_set(a, 1), "check a program"c)
    if val d = dir do
        try checkStatus(apr_procattr_dir_set(a, cstr(d)), "choose a program's directory"c)

    // C's argv: the words, then a null.
    val argv = new (CString?)[args.len + 1]
    for i in 0..<args.len do
        argv[i] = cstr(args[i])
    argv[args.len] = null

    // What is buffered goes first, or it would come after what the program writes to the same place.
    fflush(stdout)
    val proc = new apr_proc_t
    val status = apr_proc_create(proc, cstr(path), argv.data !as @CString, null, a, pool)
    throw osError(status, what) if status != APR_SUCCESS

    var inFile: io.File? = null
    var outFile: io.File? = null
    var errFile: io.File? = null
    if val f = proc.input do
        inFile = io.File { f, "the standard input of ${path}" }
    if val f = proc.out do
        outFile = io.File { f, "the standard output of ${path}" }
    if val f = proc.err do
        errFile = io.File { f, "the standard error of ${path}" }
    ret Process { proc, proc.pid, inFile, outFile, errFile }

/** The path of `program`, for {@link start}. */
priv fn resolve(program: String): String throws OsError do
    if val path = findProgram(program) do
        ret path
    throw OsError { ERR_NOT_FOUND, "can't find the program '${program}': it isn't in a directory of PATH, or can't be run", APR_ENOENT }

/** The words {@link start} passes: `program` and then `args`. */
priv fn argvOf(program: String, args: String[]): String[] do
    val all = new String[args.len + 1]
    all[0] = program
    for i in 0..<args.len do
        all[i + 1] = args[i]
    ret all

/**
 * Starts a program and goes on while it runs. {@link wait} for it.
 *
 *     val p = try os.spawn("sleep", ["10"])
 *     try os.kill(p)
 *     io.log((try os.wait(p)).signal == SIGTERM)       // true
 *
 * It gets the environment of this program. With `:pipe`, read and write its streams with the functions of core:io.ec.
 * A program that fills a pipe nobody reads waits forever, so read `stdout` and `stderr`, or use {@link run}.
 *
 * @param program the program: looked up as {@link findProgram} does
 * @param args its arguments
 * @param stdio what it is connected to; see {@link Stdio}
 * @param dir the directory to run it in, or null for the current one
 * @throws when there is no such program, or it can't be started
 */
fn spawn(program: String, args: String[] = new String[0], stdio: Stdio = :inherit, dir: String? = null): Process throws OsError do
    val pipes = stdio == :pipe
    ret try start(:program_env, try resolve(program), argvOf(program, args), pipes, pipes, dir)

/** The {@link Exit} for what apr_proc_wait gave. */
priv fn exitOf(code: Int, why: Int): Exit do
    ret Exit { 128 + code, code } if (why & APR_PROC_SIGNAL) != 0
    ret Exit { code, 0 }

/**
 * Waits until `p` has ended. Close its `stdin` first if it reads that, or both programs wait for each other.
 * @return how it ended
 * @throws when `p` was already waited for
 */
fn wait(p: Process): Exit throws OsError do
    var code = 0
    var why = 0
    val status = apr_proc_wait(p.handle, mut code, (addrof why) !as @Any, :wait)
    throw osError(status, "wait for process ${p.pid}") if status != APR_CHILD_DONE
    ret exitOf(code, why)

/**
 * Checks on `p` without waiting.
 * @return how it ended, or null while it runs. Once it gave an Exit, `p` has been waited for.
 * @throws when `p` was already waited for
 */
fn poll(p: Process): Exit? throws OsError do
    var code = 0
    var why = 0
    val status = apr_proc_wait(p.handle, mut code, (addrof why) !as @Any, :nowait)
    ret null if status == APR_CHILD_NOTDONE
    throw osError(status, "check on process ${p.pid}") if status != APR_CHILD_DONE
    ret exitOf(code, why)

/**
 * Sends a signal to `p`.
 * @param signal the signal; the default asks it to stop, and SIGKILL makes it stop
 * @throws when it can't be sent: it has ended and was waited for, say
 */
fn kill(p: Process, signal: Int = SIGTERM) throws OsError do
    val status = apr_proc_kill(p.handle, signal)
    throw osError(status, "send signal ${signal} to process ${p.pid}") if status != APR_SUCCESS

/**
 * Reads the two pipes of a program until both end. It has to read them at the same time: a program that fills one
 * while this reads the other would wait forever.
 */
priv fn collect(p: Process): Output throws OsError do
    val files = new (@apr_file_t?)[2]
    files[0] = p.stdout!.handle
    files[1] = p.stderr!.handle
    val outText = stringBuilder(4096)
    val errText = stringBuilder(256)
    val buffer = new Char[65536]
    val waiting = new (apr_pollfd_t)[2]

    while files[0] != null or files[1] != null do
        var count = 0
        for i in 0..<2 do
            if val f = files[i] do
                waiting[count].p = pool
                waiting[count].desc_type = APR_POLL_FILE
                waiting[count].reqevents = APR_POLLIN as Short
                waiting[count].rtnevents = 0
                waiting[count].desc.f = f
                waiting[count].client_data = null
                count += 1
        var ready = 0
        val polled = apr_poll(waiting.data !as @apr_pollfd_t, count, mut ready, -1)
        continue if APR_STATUS_IS_EINTR(polled) != 0
        throw osError(polled, "read from process ${p.pid}") if polled != APR_SUCCESS
        for i in 0..<(count as Size) do
            continue if waiting[i].rtnevents == 0
            val f = waiting[i].desc.f!
            val index: Size = if f == p.stdout!.handle do 0 else 1
            var got = buffer.len
            val status = apr_file_read(f, buffer.data, mut got)
            if APR_STATUS_IS_EOF(status) != 0 do
                files[index] = null
            else if status != APR_SUCCESS do
                throw osError(status, "read from process ${p.pid}")
            else
                (if index == 0 do outText else errText).append(buffer[0..<got] !as String)

    io.close(p.stdout!)
    io.close(p.stderr!)
    val exit = try wait(p)
    ret Output { exit.code, exit.signal, outText.build(), errText.build() }

/**
 * Runs a program until it ends, and gives what it wrote. That it fails isn't an error here: check with {@link ok}.
 *
 *     val r = try os.run("uname", ["-r"])
 *     io.log(r.stdout.trim()) if os.ok(r)
 *
 * It reads from this program's standard input; use {@link spawn} to give it text.
 *
 * @param program the program: looked up as {@link findProgram} does
 * @param args its arguments, as they are: nothing is split, expanded or quoted
 * @param dir the directory to run it in, or null for the current one
 * @throws when there is no such program, or it can't be started
 */
fn run(program: String, args: String[] = new String[0], dir: String? = null): Output throws OsError do
    ret try collect(try start(:program_env, try resolve(program), argvOf(program, args), false, true, dir))

/**
 * Runs a command line with /bin/sh until it ends, and gives what it wrote: `os.shell("ls *.ec | wc -l")`. Pipes, `*`
 * and `$NAME` work. Text from outside the program can run anything this way: pass it to {@link run} as an argument.
 *
 * @param command the command line
 * @param dir the directory to run it in, or null for the current one
 * @throws when the shell can't be started; a failing command gives an {@link Output} with its exit code
 */
fn shell(command: String, dir: String? = null): Output throws OsError do
    val line = new String[1]
    line[0] = command
    // APR runs its own shell with `line`, but checks the name it is given, so that can't be the command line.
    ret try collect(try start(:shellcmd_env, "/bin/sh", line, false, true, dir))

/** Whether a program succeeded: it ended by itself, with exit code 0. */
fn ok(e: Exit): Bool = e.code == 0 and e.signal == 0
