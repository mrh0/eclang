use "core:string.ec"
use "core:io.ec"
use "core:os.ec"

// core:os.ec: system information, the environment, users and running programs.
fn main(): Int throws Error do
    // The system: the values differ between machines, so only their shape is checked.
    io.log(os.name().len > 0)
    io.log(os.release().len > 0)
    io.log(os.version().len > 0)
    io.log(os.arch().len > 0)
    io.log((try os.hostName()).len > 0)
    io.log(os.cpuCount() >= 1)
    io.log(os.pageSize() >= 512)
    io.log((os.totalMemory() ?? 1) > 0)
    io.log((os.freeMemory() ?? 1) >= 0)
    io.log(os.loadAverage().len <= 3)
    io.log(os.aprVersion().contains("."))
    io.log(os.isTerminal(:stdout))
    io.log(os.isTerminal(:stderr))
    val typing = os.isTerminal(:stdin)

    // This program.
    io.log(os.pid() > 0)
    io.log(os.parentPid() > 0)
    io.log((try os.executable()).startsWith("/"))
    val pidText = try os.shell("echo $PPID")
    io.log(pidText.stdout.trim() == "${os.pid()}")

    // The environment.
    io.log(os.env("ECLANG_OS_TEST") == null)
    try os.setEnv("ECLANG_OS_TEST", "a=b")
    io.log(os.env("ECLANG_OS_TEST") ?? "unset")
    var found = 0
    for v in os.environment() do
        if v.name == "ECLANG_OS_TEST" do
            io.log(v.value)
            found += 1
    io.log(found)
    io.print((try os.shell("echo \"$ECLANG_OS_TEST\"")).stdout)
    try os.unsetEnv("ECLANG_OS_TEST")
    io.log(os.env("ECLANG_OS_TEST") == null)
    try os.unsetEnv("ECLANG_OS_TEST")
    try os.setEnv("", "x") catch e do
        io.log("empty name refused")

    // Users: root exists everywhere.
    val root = try os.findUser("root")
    io.log(root.name)
    io.log(root.id)
    io.log(root.groupId)
    io.log(root.home)
    try os.findUser("no-such-user-here") catch e do
        io.log("no such user")
    try os.setEnv("HOME", "/somewhere")
    io.log(try os.homeDir())

    // Directories.
    val start = try io.currentDir()
    try os.changeDir("/")
    io.log(try io.currentDir())
    try os.changeDir("/no/such/dir") catch e do
        io.log(e.code == ERR_NOT_FOUND)
    try os.changeDir(start)

    // run: the arguments arrive as they are, and both streams are kept apart.
    val r = try os.run("sh", ["-c", "echo \"$1\"; echo oops >&2; exit 3", "sh", "two words $HOME"])
    io.print(r.stdout)
    io.print(r.stderr)
    io.log(r.code)
    io.log(r.signal)
    io.log(os.ok(r))
    io.log(os.ok(try os.run("true")))
    io.print((try os.run("pwd", dir = "/")).stdout)

    // More than a pipe holds on both streams: reading one at a time would never end.
    val big = try os.shell("i=0; while [ $i -lt 3000 ]; do echo 0123456789012345678901234567890123456789; echo 0123456789012345678901234567890123456789 >&2; i=$((i+1)); done")
    io.log(big.stdout.len)
    io.log(big.stderr.len)

    // A program that isn't there is an error; a command that isn't there is an exit code of the shell.
    try os.run("no-such-program-here") catch e do
        io.log(e.code == ERR_NOT_FOUND)
        io.log(e.message)
    io.log((try os.shell("no-such-program-here 2> /dev/null")).code)
    io.print((try os.shell("/bin/echo an absolute path")).stdout)
    io.log(os.findProgram("no-such-program-here") == null)
    io.log((os.findProgram("sh") ?? "").endsWith("/sh"))
    io.log(os.findProgram("/bin/sh") ?? "")

    // spawn with pipes: text goes in, the result comes out.
    val sorter = try os.spawn("sort", ["-r"], :pipe)
    try io.writeLine(sorter.stdin!, "a")
    try io.writeLine(sorter.stdin!, "b")
    try io.flush(sorter.stdin!)
    io.close(sorter.stdin!)
    io.print(try io.readAll(sorter.stdout!))
    io.log(os.ok(try os.wait(sorter)))

    // spawn with this program's own streams: what is buffered comes first.
    io.print("before, ")
    io.log(os.ok(try os.wait(try os.spawn("echo", ["from the child"]))))

    // A signal ends it, and poll tells.
    val sleeper = try os.spawn("sleep", ["30"])
    io.log(sleeper.pid > 0)
    io.log((try os.poll(sleeper)) == null)
    try os.kill(sleeper)
    val ended = try os.wait(sleeper)
    io.log(ended.code)
    io.log(ended.signal == SIGTERM)
    io.log(os.ok(ended))
    io.log(os.signalName(SIGTERM).len > 0)
    try os.wait(sleeper) catch e do
        io.log("already waited for")

    // With SIGPIPE ignored, writing to a program that ended is an error, and not the end of this program.
    os.ignoreSignal(SIGPIPE)
    val gone = try os.spawn("true", stdio = :pipe)
    try os.wait(gone)
    try io.write(gone.stdin!, "x") catch e do
        io.log("broken pipe")

    os.exit(0)
