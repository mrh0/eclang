# Module `os`

The operating system: what system this is, the environment, users, and running other programs. Built on APR
(apr_env.h, apr_user.h, apr_thread_proc.h, apr_poll.h, ...), and on C's uname and sysconf for what APR doesn't have.

    use "core:os.ec"
    io.log("${os.name()} on ${os.arch()}, ${os.cpuCount()} CPUs")     // Linux on x86_64, 8 CPUs
    io.log(try os.hostName())
    io.log((try os.user()).name)

    try os.setEnv("LANG", "C")
    val home = os.env("HOME") ?? "/"

    val r = try os.run("git", ["status", "--short"])     // waits, and gives what the program wrote
    io.print(r.stdout) if os.ok(r)
    val s = try os.shell("ls | wc -l")                    // a command line, run by /bin/sh

    val p = try os.spawn("sort", ["-r"], :pipe)           // runs next to this program
    try io.writeLine(p.stdin!, "a")
    io.close(p.stdin!)
    io.print(try io.readAll(p.stdout!))
    val exit = try os.wait(p)

What is returned is allocated in the current pool. Failures are OsErrors, with the reason. The environment
and the current directory belong to the whole program: don't change them while other threads use them.
See core:io.ec for `io.args`, `io.currentDir` and `io.tempDir`.

## Types

### <a id="os.Stream"></a>`Stream`

```ec
declare type Stream as :stdin | :stdout | :stderr
```

The standard streams:

- `:stdin`: what the program reads from
- `:stdout`: what it writes to
- `:stderr`: what it writes errors to

### <a id="os.EnvVar"></a>`EnvVar`

```ec
rec EnvVar
```

One environment variable, from [`environment`](#os.environment).

**Fields**

- <a id="os.EnvVar.name"></a>`name: String` — Its name.
- <a id="os.EnvVar.value"></a>`value: String` — Its value.

### <a id="os.User"></a>`User`

```ec
rec User
```

An account on this computer.

**Fields**

- <a id="os.User.name"></a>`name: String` — The login name.
- <a id="os.User.id"></a>`id: UInt` — The user's number.
- <a id="os.User.groupId"></a>`groupId: UInt` — The number of the user's main group.
- <a id="os.User.group"></a>`group: String?` — The name of that group, or null when it has none.
- <a id="os.User.home"></a>`home: String` — The user's home directory.

### <a id="os.Exit"></a>`Exit`

```ec
rec Exit
```

How a program ended.

**Fields**

- <a id="os.Exit.code"></a>`code: Int` — Its exit code, 0 for success. A program stopped by a signal gets 128 plus the signal, as in a shell.
- <a id="os.Exit.signal"></a>`signal: Int` — The signal that stopped it (see [`signalName`](#os.signalName)), or 0 when it ended by itself.

### <a id="os.Output"></a>`Output`

```ec
rec Output extends Exit
```

How a program ended, and what it wrote, from [`run`](#os.run) and [`shell`](#os.shell).

**Fields**

- <a id="os.Output.stdout"></a>`stdout: String` — What it wrote to its standard output.
- <a id="os.Output.stderr"></a>`stderr: String` — What it wrote to its standard error.

### <a id="os.Process"></a>`Process`

```ec
rec Process
```

A running program, from [`spawn`](#os.spawn). [`wait`](#os.wait) for it: until then the system keeps its entry, even when
it has ended.

**Fields**

- <a id="os.Process.handle"></a>`handle: @apr_proc_t` — The APR process, for calling apr_proc_* functions directly.
- <a id="os.Process.pid"></a>`pid: Int` — Its process id.
- <a id="os.Process.stdin"></a>`stdin: io.File?` — Writes to its standard input, with `:pipe`. Close it to tell the program there's no more.
- <a id="os.Process.stdout"></a>`stdout: io.File?` — Reads its standard output, with `:pipe`.
- <a id="os.Process.stderr"></a>`stderr: io.File?` — Reads its standard error, with `:pipe`.

### <a id="os.Stdio"></a>`Stdio`

```ec
declare type Stdio as :inherit | :pipe
```

What a program started with [`spawn`](#os.spawn) is connected to:

- `:inherit`: this program's own standard streams
- `:pipe`: pipes, which [`Process`](#os.Process) has the other ends of

## Values

### <a id="os.environTable"></a>`environTable`

```ec
declare val environTable extern "__ec_environ" as CString?[]c
```

The C array of "NAME=value" texts, ended by a null. Use [`environment`](#os.environment).

## Functions

### <a id="os.name"></a>`name`

```ec
fn name(): String
```

The name of the operating system, such as "Linux" or "Darwin".

### <a id="os.release"></a>`release`

```ec
fn release(): String
```

The release of the system's kernel, such as "6.8.0-45-generic".

### <a id="os.version"></a>`version`

```ec
fn version(): String
```

The version of the system's kernel, as the system words it (often its build number and date).

### <a id="os.arch"></a>`arch`

```ec
fn arch(): String
```

The kind of processor the system runs on, such as "x86_64" or "aarch64".

### <a id="os.hostName"></a>`hostName`

```ec
fn hostName(): String throws OsError
```

The name of this computer on the network.

**Throws**

- `OsError` — when the system doesn't tell

### <a id="os.cpuCount"></a>`cpuCount`

```ec
fn cpuCount(): Int
```

How many processors are online; at least 1.

### <a id="os.pageSize"></a>`pageSize`

```ec
fn pageSize(): Long
```

The size of a page of memory, in bytes.

### <a id="os.totalMemory"></a>`totalMemory`

```ec
fn totalMemory(): Long?
```

The memory of this computer, in bytes, or null when the system doesn't tell.

### <a id="os.freeMemory"></a>`freeMemory`

```ec
fn freeMemory(): Long?
```

The memory nothing is using right now, in bytes, or null when the system doesn't tell.

### <a id="os.loadAverage"></a>`loadAverage`

```ec
fn loadAverage(): Double[]
```

How busy the system is: the average number of processes running or waiting to run.

**Returns** the averages over 1, 5 and 15 minutes; empty when the system doesn't tell

### <a id="os.aprVersion"></a>`aprVersion`

```ec
fn aprVersion(): String
```

The version of the APR library the program runs with, such as "1.7.6".

### <a id="os.isTerminal"></a>`isTerminal`

```ec
fn isTerminal(stream: Stream): Bool
```

Whether `stream` is a terminal, and not a file or a pipe: `os.isTerminal(:stdout)` tells if colors make sense.

### <a id="os.env"></a>`env`

```ec
fn env(name: String): String?
```

The value of the environment variable `name` (`os.env("HOME")`), or null when it isn't set.

### <a id="os.setEnv"></a>`setEnv`

```ec
fn setEnv(name: String, value: String) throws OsError
```

Sets the environment variable `name`, for this program and the programs it starts.

**Throws**

- `OsError` — when `name` isn't a valid name (it is empty or has a "="), or memory runs out

### <a id="os.unsetEnv"></a>`unsetEnv`

```ec
fn unsetEnv(name: String) throws OsError
```

Removes the environment variable `name`. It is no error when it isn't set.

**Throws**

- `OsError` — when `name` isn't a valid name (it is empty or has a "=")

### <a id="os.environment"></a>`environment`

```ec
fn environment(): EnvVar[]
```

All environment variables, in the order the system keeps them.

### <a id="os.user"></a>`user`

```ec
fn user(): User throws OsError
```

The user the program runs as.

**Throws**

- `OsError` — when the system has no account for it (as can happen in a container)

### <a id="os.findUser"></a>`findUser`

```ec
fn findUser(name: String): User throws OsError
```

The user called `name`.

**Throws**

- `OsError` — when there is no such user

### <a id="os.homeDir"></a>`homeDir`

```ec
fn homeDir(): String throws OsError
```

The home directory of the current user: the HOME environment variable, or else what the system's account says.

**Throws**

- `OsError` — when neither tells

### <a id="os.pid"></a>`pid`

```ec
fn pid(): Int
```

The process id of this program.

### <a id="os.parentPid"></a>`parentPid`

```ec
fn parentPid(): Int
```

The process id of the program that started this one.

### <a id="os.exitProgram"></a>`exitProgram`

```ec
fn exitProgram(status: Int): Never
```

C's exit: here because this module's own [`exit`](#os.exit) hides it.

*Binds the C function `exit`.*

### <a id="os.exit"></a>`exit`

```ec
fn exit(code: Int): Never
```

Ends the program with the exit code `code` (0 means success), after writing out what stdout buffers. Deferred
statements don't run.

### <a id="os.changeDir"></a>`changeDir`

```ec
fn changeDir(path: String) throws IoError
```

Makes `path` the directory the program runs in (see `io.currentDir`). Relative paths mean something else after this.

**Throws**

- `IoError` — when it doesn't exist or isn't a directory

### <a id="os.executable"></a>`executable`

```ec
fn executable(): String throws OsError
```

The absolute path of this program's executable file. It reads /proc/self/exe, so it needs Linux.

**Throws**

- `OsError` — when the system doesn't tell

### <a id="os.signalName"></a>`signalName`

```ec
fn signalName(signal: Int): String
```

Words for the signal number `signal`, such as "Terminated" for SIGTERM.

### <a id="os.ignoreSignal"></a>`ignoreSignal`

```ec
fn ignoreSignal(signal: Int)
```

Makes the program ignore a signal. `os.ignoreSignal(SIGPIPE)` is worth doing before writing to a program that may
have stopped: the write then fails with an IoError, where the signal would have ended this program.

### <a id="os.findProgram"></a>`findProgram`

```ec
fn findProgram(program: String): String?
```

Finds the file a program name stands for, the way a shell does: in the directories of the PATH environment
variable, in their order. A name with a "/" in it is a path, and isn't looked up.

**Returns** the path of the file, or null when there is none that can be run

### <a id="os.spawn"></a>`spawn`

```ec
fn spawn(program: String, args: String[] = new String[0], stdio: Stdio = :inherit, dir: String? = null): Process throws OsError
```

Starts a program and goes on while it runs. [`wait`](#os.wait) for it.

    val p = try os.spawn("sleep", ["10"])
    try os.kill(p)
    io.log((try os.wait(p)).signal == SIGTERM)       // true

It gets the environment of this program. With `:pipe`, read and write its streams with the functions of core:io.ec.
A program that fills a pipe nobody reads waits forever, so read `stdout` and `stderr`, or use [`run`](#os.run).

**Parameters**

- `program` — the program: looked up as [`findProgram`](#os.findProgram) does
- `args` — its arguments
- `stdio` — what it is connected to; see [`Stdio`](#os.Stdio)
- `dir` — the directory to run it in, or null for the current one

**Throws**

- `OsError` — when there is no such program, or it can't be started

### <a id="os.wait"></a>`wait`

```ec
fn wait(p: Process): Exit throws OsError
```

Waits until `p` has ended. Close its `stdin` first if it reads that, or both programs wait for each other.

**Returns** how it ended

**Throws**

- `OsError` — when `p` was already waited for

### <a id="os.poll"></a>`poll`

```ec
fn poll(p: Process): Exit? throws OsError
```

Checks on `p` without waiting.

**Returns** how it ended, or null while it runs. Once it gave an Exit, `p` has been waited for.

**Throws**

- `OsError` — when `p` was already waited for

### <a id="os.kill"></a>`kill`

```ec
fn kill(p: Process, signal: Int = SIGTERM) throws OsError
```

Sends a signal to `p`.

**Parameters**

- `signal` — the signal; the default asks it to stop, and SIGKILL makes it stop

**Throws**

- `OsError` — when it can't be sent: it has ended and was waited for, say

### <a id="os.run"></a>`run`

```ec
fn run(program: String, args: String[] = new String[0], dir: String? = null): Output throws OsError
```

Runs a program until it ends, and gives what it wrote. That it fails isn't an error here: check with [`ok`](#os.ok).

    val r = try os.run("uname", ["-r"])
    io.log(r.stdout.trim()) if os.ok(r)

It reads from this program's standard input; use [`spawn`](#os.spawn) to give it text.

**Parameters**

- `program` — the program: looked up as [`findProgram`](#os.findProgram) does
- `args` — its arguments, as they are: nothing is split, expanded or quoted
- `dir` — the directory to run it in, or null for the current one

**Throws**

- `OsError` — when there is no such program, or it can't be started

### <a id="os.shell"></a>`shell`

```ec
fn shell(command: String, dir: String? = null): Output throws OsError
```

Runs a command line with /bin/sh until it ends, and gives what it wrote: `os.shell("ls *.ec | wc -l")`. Pipes, `*`
and `$NAME` work. Text from outside the program can run anything this way: pass it to [`run`](#os.run) as an argument.

**Parameters**

- `command` — the command line
- `dir` — the directory to run it in, or null for the current one

**Throws**

- `OsError` — when the shell can't be started; a failing command gives an [`Output`](#os.Output) with its exit code

### <a id="os.ok"></a>`ok`

```ec
fn ok(e: Exit): Bool
```

Whether a program succeeded: it ended by itself, with exit code 0.
