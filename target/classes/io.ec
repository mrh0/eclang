/**
 * Input and output: the console, files, directories and paths. Files use APR (apr_file_io.h); the console uses
 * C's stdio, so the two can be mixed.
 *
 *     use "core:io.ec"
 *     io.print("name? ")
 *     val name = io.readLine() ?? ""                    // null at the end of the input
 *     try io.writeFile("hello.txt", "hello ${name}\n")
 *     for line in try io.readLines("hello.txt") do
 *         io.log(line)
 *
 *     val f = try io.open("log.txt", :append)          // :read, :write (truncates), :append or :update
 *     defer io.close(f)
 *     try io.writeLine(f, "started")
 *
 * What is read is allocated in the current pool. An open file belongs to the current pool too: it is closed
 * when the pool is freed, if `close` didn't close it before. Failures are IoErrors, with the path and the reason.
 */
module "io"
use "core:errors.ec"
use "core:string.ec"
use "core:time.ec"
use "core:c/stdio.ec"
use "core:c/stdlib.ec"
use "core:c/string.ec"
use "core:apr/file_io.ec"
use "core:apr/file_info.ec"

// ---------------------------------------------------------------------------------------------------
// Console

/**
 * Writes a value and a line break to stdout: `io.log(x)`. There is one `log` for each kind of value: numbers,
 * strings, atoms, ... This one writes a C string.
 */
fn log(value: CString) do
    printf("%s\n"c, value)

/** Writes a String and a line break to stdout. */
fn log(value: String) do
    printf("%.*s\n"c, value.len as Int, value.data)

/** Writes an atom, as `:name` and a line break to stdout. */
fn log(value: Atom) do
    printf(":%s\n"c, value)

/** Writes `true` or `false` and a line break to stdout. */
fn log(value: Bool) do
    printf("%s\n"c, if value do "true"c else "false"c)

/** Writes a character and a line break to stdout. */
fn log(value: Char) do
    printf("%c\n"c, value)

/** Writes a Short and a line break to stdout. */
fn log(value: Short) do
    printf("%hd\n"c, value)

/** Writes an Int and a line break to stdout. */
fn log(value: Int) do
    printf("%d\n"c, value)

/** Writes a Long and a line break to stdout. */
fn log(value: Long) do
    printf("%ld\n"c, value)

/** Writes a UChar, as a number and a line break to stdout. */
fn log(value: UChar) do
    printf("%hhu\n"c, value)

/** Writes a UShort and a line break to stdout. */
fn log(value: UShort) do
    printf("%hu\n"c, value)

/** Writes a UInt and a line break to stdout. */
fn log(value: UInt) do
    printf("%u\n"c, value)

/** Writes a ULong and a line break to stdout. */
fn log(value: ULong) do
    printf("%lu\n"c, value)

/** Writes a Size and a line break to stdout. */
fn log(value: Size) do
    printf("%zu\n"c, value)

/** Writes a Float (as `%g` formats it) and a line break to stdout. */
fn log(value: Float) do
    printf("%g\n"c, value)

/** Writes a Double (as `%g` formats it) and a line break to stdout. */
fn log(value: Double) do
    printf("%g\n"c, value)

/** Writes an error, as {@link describe} words it ("parse error: expected a digit"), and a line break to stdout. */
fn log(e: Error) do
    log(describe(e))

/** Writes `s` to stdout, without a line break. */
fn print(s: String) do
    fwrite(s.data, 1, s.len, stdout)

/** Writes a C string to stdout, without a line break. */
fn print(s: CString) do
    fputs(s, stdout)

/** Writes a character to stdout. */
fn print(c: Char) do
    fputc(c as Int, stdout)

/** Writes `s` and a line break to stdout. */
fn println(s: String) do
    print(s)
    fputc('\n' as Int, stdout)

/** Writes `s` to stderr, without a line break. */
fn eprint(s: String) do
    fwrite(s.data, 1, s.len, stderr)

/** Writes `s` and a line break to stderr. */
fn eprintln(s: String) do
    eprint(s)
    fputc('\n' as Int, stderr)

/** Writes out what stdout holds back (it does at each line break when it is a terminal, and when it fills up). */
fn flush() do
    fflush(stdout)

/**
 * Reads the next line of stdin. stdout is flushed first, so a prompt printed without a line break is shown.
 *
 *     io.print("name? ")
 *     val name = io.readLine() ?? ""
 *
 * @return the line, without its line break; null at the end of the input
 */
fn readLine(): String? do
    fflush(stdout)
    val b = stringBuilder(128)
    val buffer = new Char[1024]
    var got = false
    while fgets(buffer.data !as CString, buffer.len as Int, stdin) != null do
        got = true
        val n = strlen(buffer.data !as CString)
        b.append(buffer[0..<n] !as String)
        break if n > 0 and buffer[n - 1] == '\n'
    ret null if not got
    ret removeSuffix(removeSuffix(b.build(), "\n"), "\r")

/** Reads everything left on stdin, up to the end of the input. */
fn readAll(): String do
    val b = stringBuilder(4096)
    val buffer = new Char[65536]
    while true do
        val n = fread(buffer.data, 1, buffer.len, stdin)
        break if n == 0
        b.append(buffer[0..<n] !as String)
    ret b.build()

// ---------------------------------------------------------------------------------------------------
// The command line

/** How many words the command line has, the program's name included; the generated C `main` sets it. Use {@link args}. */
declare val commandLineLength extern "__ec_argc" as Int
/** The words of the command line, as C's `main` gets them. Use {@link args}. */
declare val commandLine extern "__ec_argv" as CString[]c

/**
 * The arguments the program was started with, without its own name: `./app -v in.txt` gives ["-v", "in.txt"].
 * With `eclang run`, they are what follows `--`: `eclang run app.ec -- -v in.txt`.
 */
fn args(): String[] do
    val count = if commandLineLength < 1 do 0 else (commandLineLength - 1) as Size
    val all = new String[count]
    for i in 0..<count do
        all[i] = fromC(commandLine[i + 1])
    ret all

/** How the program was started, the first word of the command line: "./app". */
fn programName(): String = if commandLineLength < 1 do "" else fromC(commandLine[0])

// ---------------------------------------------------------------------------------------------------
// Files

/** An open file, from {@link open}. Close it with {@link close}, or it stays open until its pool is freed. */
rec File as
    /** The APR file, for calling apr_file_* functions directly. */
    handle: @apr_file_t
    /** The path it was opened with, for error messages. */
    path: String

/**
 * How to {@link open} a file:
 *
 * - `:read`: read an existing file
 * - `:write`: write a new file, or replace what an existing one holds
 * - `:append`: write at the end of a file, making it if it doesn't exist
 * - `:update`: read and write an existing file, from its start
 */
declare type OpenMode as :read | :write | :append | :update

/** The APR flags for `mode`. */
priv fn flagsOf(mode: OpenMode): Int do
    when mode do
        :read do ret APR_FOPEN_READ | APR_FOPEN_BUFFERED
        :write do ret APR_FOPEN_WRITE | APR_FOPEN_CREATE | APR_FOPEN_TRUNCATE | APR_FOPEN_BUFFERED
        :append do ret APR_FOPEN_WRITE | APR_FOPEN_CREATE | APR_FOPEN_APPEND | APR_FOPEN_BUFFERED
        :update do ret APR_FOPEN_READ | APR_FOPEN_WRITE | APR_FOPEN_BUFFERED

/**
 * Opens a file. New files get the usual permissions (rw-r--r--, less the umask).
 *
 *     val f = try io.open("log.txt", :append)
 *     defer io.close(f)
 *
 * @param path where the file is
 * @param mode what to do with it; see {@link OpenMode}
 * @throws when the file can't be opened: it doesn't exist (for `:read` and `:update`), permission is denied, ...
 */
fn open(path: String, mode: OpenMode = :read): File throws IoError do
    var handle: @apr_file_t? = null
    val status = apr_file_open(addrof(handle!), cstr(path), flagsOf(mode), APR_FPROT_OS_DEFAULT, pool)
    throw ioError(status, path) if status != APR_SUCCESS
    ret File { handle!, path }

/** Writes out what is buffered and closes `f`. Don't use `f` after this. */
fn close(f: File) do
    apr_file_close(f.handle)

/**
 * Reads up to `n` bytes from where `f` is.
 * @return the bytes read: fewer than `n` at the end of the file, and "" when nothing is left
 * @throws when reading fails
 */
fn read(f: File, n: Size): String throws IoError do
    val buffer = new Char[n]
    var got = n
    val status = apr_file_read(f.handle, buffer.data, addrof(got))
    ret "" if APR_STATUS_IS_EOF(status) != 0
    throw ioError(status, f.path) if status != APR_SUCCESS
    ret buffer[0..<got] !as String

/**
 * Reads the next line of `f`.
 * @return the line, without its line break ("\n" or "\r\n"); null at the end of the file
 * @throws when reading fails
 */
fn readLine(f: File): String? throws IoError do
    val b = stringBuilder(128)
    val buffer = new Char[1024]
    var got = false
    while true do
        val status = apr_file_gets(buffer.data !as CString, buffer.len as Int, f.handle)
        break if APR_STATUS_IS_EOF(status) != 0
        throw ioError(status, f.path) if status != APR_SUCCESS
        got = true
        val n = strlen(buffer.data !as CString)
        b.append(buffer[0..<n] !as String)
        break if n == 0 or buffer[n - 1] == '\n'
    ret null if not got
    ret removeSuffix(removeSuffix(b.build(), "\n"), "\r")

/**
 * Reads everything from where `f` is to its end.
 * @throws when reading fails
 */
fn readAll(f: File): String throws IoError do
    val b = stringBuilder(4096)
    while true do
        val part = try read(f, 65536)
        break if part.len == 0
        b.append(part)
    ret b.build()

/**
 * Writes all of `s` at where `f` is. The file buffers it; see {@link flush}.
 * @throws when writing fails (the disk is full, say)
 */
fn write(f: File, s: String) throws IoError do
    var written: Size = 0
    val status = apr_file_write_full(f.handle, s.data, s.len, addrof(written))
    throw ioError(status, f.path) if status != APR_SUCCESS

/**
 * Writes `s` and a line break.
 * @throws when writing fails
 */
fn writeLine(f: File, s: String) throws IoError do
    try write(f, s)
    try write(f, "\n")

/**
 * Writes out what `f` buffers.
 * @throws when writing fails
 */
fn flush(f: File) throws IoError do
    val status = apr_file_flush(f.handle)
    throw ioError(status, f.path) if status != APR_SUCCESS

/**
 * Moves to byte `offset` from the start of `f`, where the next read or write happens.
 * @throws when `f` can't seek (a pipe, say)
 */
fn seek(f: File, offset: Long) throws IoError do
    var at = offset
    val status = apr_file_seek(f.handle, APR_SET, addrof(at))
    throw ioError(status, f.path) if status != APR_SUCCESS

// ---------------------------------------------------------------------------------------------------
// Whole files

/**
 * Reads a whole file: `val text = try io.readFile("notes.txt")`.
 * @return what the file holds
 * @throws when the file can't be opened or read
 */
fn readFile(path: String): String throws IoError do
    val f = try open(path, :read)
    defer close(f)
    ret try readAll(f)

/**
 * Reads a whole file as lines: `for line in try io.readLines("notes.txt") do ...`.
 * @return the lines, without their line breaks
 * @throws when the file can't be opened or read
 */
fn readLines(path: String): String[] throws IoError do
    val text = try readFile(path)
    ret lines(text)

/**
 * Makes the file at `path` hold `s`, replacing what it held; it is made if it doesn't exist.
 * @throws when the file can't be opened or written
 */
fn writeFile(path: String, s: String) throws IoError do
    val f = try open(path, :write)
    defer close(f)
    try write(f, s)

/**
 * Adds `s` at the end of the file at `path`, making the file if it doesn't exist.
 * @throws when the file can't be opened or written
 */
fn appendFile(path: String, s: String) throws IoError do
    val f = try open(path, :append)
    defer close(f)
    try write(f, s)

// ---------------------------------------------------------------------------------------------------
// Files and directories

/** What the system knows about `path` (the `wanted` APR_FINFO_* fields), or null when it can't be looked at. */
priv fn stat(path: String, wanted: Int): apr_finfo_t? do
    var info: apr_finfo_t
    val status = apr_stat(addrof(info), cstr(path), wanted, pool)
    // APR_INCOMPLETE: some of the wanted fields aren't known on this system; the others are filled in.
    ret null if status != APR_SUCCESS and status != APR_INCOMPLETE
    ret info

/** Whether something (a file, a directory, ...) is at `path`. */
fn exists(path: String): Bool = stat(path, APR_FINFO_TYPE) != null

/** Whether `path` is a regular file (not a directory, a device, ...). False when nothing is there. */
fn isFile(path: String): Bool do
    if val info = stat(path, APR_FINFO_TYPE) do
        ret info.filetype == APR_REG
    ret false

/** Whether `path` is a directory. False when nothing is there. */
fn isDir(path: String): Bool do
    if val info = stat(path, APR_FINFO_TYPE) do
        ret info.filetype == APR_DIR
    ret false

/**
 * The size of the file at `path`, in bytes.
 * @throws when nothing is at `path`, or it can't be looked at
 */
fn fileSize(path: String): Long throws IoError do
    var info: apr_finfo_t
    val status = apr_stat(addrof(info), cstr(path), APR_FINFO_SIZE, pool)
    throw ioError(status, path) if status != APR_SUCCESS and status != APR_INCOMPLETE
    ret info.size

/**
 * When the file at `path` was last changed.
 * @throws when nothing is at `path`, or it can't be looked at
 */
fn modified(path: String): time.Time throws IoError do
    var info: apr_finfo_t
    val status = apr_stat(addrof(info), cstr(path), APR_FINFO_MTIME, pool)
    throw ioError(status, path) if status != APR_SUCCESS and status != APR_INCOMPLETE
    ret time.Time { info.mtime }

/**
 * Deletes the file at `path`. See {@link removeDir} for a directory.
 * @throws when it doesn't exist or can't be deleted
 */
fn remove(path: String) throws IoError do
    val status = apr_file_remove(cstr(path), pool)
    throw ioError(status, path) if status != APR_SUCCESS

/**
 * Moves (or renames) a file, replacing what is at `target`.
 * @param source the file to move
 * @param target where it goes
 * @throws when it can't be moved (the error has the path of `source`)
 */
fn rename(source: String, target: String) throws IoError do
    val status = apr_file_rename(cstr(source), cstr(target), pool)
    throw ioError(status, source) if status != APR_SUCCESS

/**
 * Copies a file, replacing what is at `target`; the copy gets the same permissions.
 * @param source the file to copy
 * @param target where the copy goes
 * @throws when it can't be copied (the error has the path of `source`)
 */
fn copy(source: String, target: String) throws IoError do
    val status = apr_file_copy(cstr(source), cstr(target), APR_FPROT_FILE_SOURCE_PERMS, pool)
    throw ioError(status, source) if status != APR_SUCCESS

/**
 * Makes a directory, and the directories above it that don't exist. Nothing happens if it exists.
 * @throws when a directory can't be made
 */
fn makeDir(path: String) throws IoError do
    val status = apr_dir_make_recursive(cstr(path), APR_FPROT_OS_DEFAULT, pool)
    throw ioError(status, path) if status != APR_SUCCESS

/**
 * Deletes a directory, which must be empty.
 * @throws when it doesn't exist, isn't empty or can't be deleted
 */
fn removeDir(path: String) throws IoError do
    val status = apr_dir_remove(cstr(path), pool)
    throw ioError(status, path) if status != APR_SUCCESS

/**
 * Lists a directory: `for name in io.sorted(try io.listDir(".")) do ...`.
 * @return the names in it (not "." and "..", and not the whole paths), in the order the system gives them;
 *     see {@link sorted} for an order
 * @throws when the directory can't be opened
 */
fn listDir(path: String): String[] throws IoError do
    var dir: @apr_dir_t? = null
    val status = apr_dir_open(addrof(dir!), cstr(path), pool)
    throw ioError(status, path) if status != APR_SUCCESS
    val d = dir!
    defer apr_dir_close(d)
    // Count the entries first, then read them again into an array of that size.
    var count: Size = 0
    var info: apr_finfo_t
    while apr_dir_read(addrof(info), APR_FINFO_NAME, d) == APR_SUCCESS do
        if not isDotName(nameOf(info)) do
            count += 1
    apr_dir_rewind(d)
    val names = new String[count]
    var i: Size = 0
    while i < count and apr_dir_read(addrof(info), APR_FINFO_NAME, d) == APR_SUCCESS do
        val name = nameOf(info)
        if not isDotName(name) do
            names[i] = fromC(name ?? ""c)
            i += 1
    ret names[0..<i]

/** The name field, which C declares `const char*` (eclang has no const). */
priv fn nameOf(info: apr_finfo_t): CString? = info.name !as CString?

/** Whether `name` is "." or ".." (or missing). */
priv fn isDotName(name: CString?): Bool do
    ret true if name == null
    val s = fromC(name)
    ret s == "." or s == ".."

/**
 * Sorts names in place, the way a person sorts file names ("file9" before "file10"; see {@link naturalCompare}).
 * @return `names`, sorted
 */
fn sorted(names: String[]): String[] do
    // Insertion sort: directory listings are short.
    for i in 1..<names.len do
        val item = names[i]
        var j = i
        while j > 0 and naturalCompare(names[j - 1], item) > 0 do
            names[j] = names[j - 1]
            j -= 1
        names[j] = item
    ret names

/**
 * The directory the program runs in, as an absolute path.
 * @throws when it can't be found (it was deleted, say)
 */
fn currentDir(): String throws IoError do
    var path: CString = ""c
    val status = apr_filepath_get(addrof(path), 0, pool)
    throw ioError(status, ".") if status != APR_SUCCESS
    ret fromC(path)

/**
 * The directory for temporary files (such as /tmp).
 * @throws when there is none that can be written
 */
fn tempDir(): String throws IoError do
    var path: CString = ""c
    val status = apr_temp_dir_get(addrof(path), pool)
    throw ioError(status, "the temporary directory") if status != APR_SUCCESS
    ret fromC(path)

/** The value of the environment variable `name` (`io.env("HOME")`), or null when it isn't set. */
fn env(name: String): String? do
    if val value = getenv(cstr(name)) do
        ret fromC(value)
    ret null

// ---------------------------------------------------------------------------------------------------
// Paths (as text; the file system isn't asked)

/** `a` and `b` with one "/" between them: `joinPath("logs/", "today.txt")` is "logs/today.txt". An absolute `b` wins. */
fn joinPath(a: String, b: String): String do
    ret b if a.len == 0 or startsWith(b, '/')
    ret concat(a, b) if endsWith(a, '/')
    ret concat(a, "/", b)

/** The last part of a path: `baseName("a/b/c.txt")` is "c.txt". A "/" at the end is left out. */
fn baseName(path: String): String do
    val p = if path.len > 1 do removeSuffix(path, "/") else path
    if val i = lastIndexOf(p, '/') do
        ret if p.len == 1 do p else p[i + 1..<p.len]
    ret p

/** The path without its last part: `dirName("a/b/c.txt")` is "a/b", `dirName("c.txt")` is ".". */
fn dirName(path: String): String do
    val p = if path.len > 1 do removeSuffix(path, "/") else path
    if val i = lastIndexOf(p, '/') do
        ret if i == 0 do "/" else p[0..<i]
    ret "."

/**
 * The extension of the last part, without the dot: `extension("a/photo.jpg")` is "jpg". A name that starts
 * with a dot (".bashrc") has none.
 * @return the extension, "" when there is none
 */
fn extension(path: String): String do
    val name = baseName(path)
    if val i = lastIndexOf(name, '.') do
        ret if i == 0 do "" else name[i + 1..<name.len]
    ret ""
