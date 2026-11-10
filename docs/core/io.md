# Module `io`

Input and output: the console, files and directories. Paths are in core:path.ec. Files use APR (apr_file_io.h); the console uses
C's stdio, so the two can be mixed.

    use "core:io.ec"
    io.print("name? ")
    val name = io.readLine() ?? ""                    // null at the end of the input
    try io.writeFile("hello.txt", "hello ${name}\n")
    for line in try io.readLines("hello.txt") do
        io.log(line)

    val f = try io.open("log.txt", :append)          // :read, :write (truncates), :append or :update
    defer io.close(f)
    try io.writeLine(f, "started")

What is read is allocated in the current pool. An open file belongs to the current pool too: it is closed
when the pool is freed, if `close` didn't close it before. Failures are IoErrors, with the path and the reason.

## Types

### <a id="io.File"></a>`File`

```ec
rec File
```

An open file, from [`open`](#io.open). Close it with [`close`](#io.close), or it stays open until its pool is freed.

**Fields**

- <a id="io.File.handle"></a>`handle: @apr_file_t` — The APR file, for calling apr_file_* functions directly.
- <a id="io.File.path"></a>`path: String` — The path it was opened with, for error messages.

### <a id="io.OpenMode"></a>`OpenMode`

```ec
declare type OpenMode as :read | :write | :append | :update
```

How to [`open`](#io.open) a file:

- `:read`: read an existing file
- `:write`: write a new file, or replace what an existing one holds
- `:append`: write at the end of a file, making it if it doesn't exist
- `:update`: read and write an existing file, from its start

## Values

### <a id="io.commandLineLength"></a>`commandLineLength`

```ec
declare val commandLineLength extern "__ec_argc" as Int
```

How many words the command line has, the program's name included; the generated C `main` sets it. Use [`args`](#io.args).

### <a id="io.commandLine"></a>`commandLine`

```ec
declare val commandLine extern "__ec_argv" as CString[]c
```

The words of the command line, as C's `main` gets them. Use [`args`](#io.args).

## Functions

### <a id="io.log"></a>`log`

```ec
fn log(value: CString)
```

Writes a value and a line break to stdout: `io.log(x)`. There is one `log` for each kind of value: numbers,
strings, atoms, ... This one writes a C string.

### `log`

```ec
fn log(value: String)
```

Writes a String and a line break to stdout.

### `log`

```ec
fn log(value: Atom)
```

Writes an atom, as `:name` and a line break to stdout.

### `log`

```ec
fn log(value: Bool)
```

Writes `true` or `false` and a line break to stdout.

### `log`

```ec
fn log(value: Char)
```

Writes a character and a line break to stdout.

### `log`

```ec
fn log(value: Short)
```

Writes a Short and a line break to stdout.

### `log`

```ec
fn log(value: Int)
```

Writes an Int and a line break to stdout.

### `log`

```ec
fn log(value: Long)
```

Writes a Long and a line break to stdout.

### `log`

```ec
fn log(value: UChar)
```

Writes a UChar, as a number and a line break to stdout.

### `log`

```ec
fn log(value: UShort)
```

Writes a UShort and a line break to stdout.

### `log`

```ec
fn log(value: UInt)
```

Writes a UInt and a line break to stdout.

### `log`

```ec
fn log(value: ULong)
```

Writes a ULong and a line break to stdout.

### `log`

```ec
fn log(value: Size)
```

Writes a Size and a line break to stdout.

### `log`

```ec
fn log(value: Float)
```

Writes a Float (as `%g` formats it) and a line break to stdout.

### `log`

```ec
fn log(value: Double)
```

Writes a Double (as `%g` formats it) and a line break to stdout.

### `log`

```ec
fn log(e: Error)
```

Writes an error, as `describe` words it ("parse error: expected a digit"), and a line break to stdout.

### <a id="io.print"></a>`print`

```ec
fn print(s: String)
```

Writes `s` to stdout, without a line break.

### `print`

```ec
fn print(s: CString)
```

Writes a C string to stdout, without a line break.

### `print`

```ec
fn print(c: Char)
```

Writes a character to stdout.

### <a id="io.println"></a>`println`

```ec
fn println(s: String)
```

Writes `s` and a line break to stdout.

### <a id="io.eprint"></a>`eprint`

```ec
fn eprint(s: String)
```

Writes `s` to stderr, without a line break.

### <a id="io.eprintln"></a>`eprintln`

```ec
fn eprintln(s: String)
```

Writes `s` and a line break to stderr.

### <a id="io.flush"></a>`flush`

```ec
fn flush()
```

Writes out what stdout holds back (it does at each line break when it is a terminal, and when it fills up).

### <a id="io.readLine"></a>`readLine`

```ec
fn readLine(): String?
```

Reads the next line of stdin. stdout is flushed first, so a prompt printed without a line break is shown.

    io.print("name? ")
    val name = io.readLine() ?? ""

**Returns** the line, without its line break; null at the end of the input

### <a id="io.readAll"></a>`readAll`

```ec
fn readAll(): String
```

Reads everything left on stdin, up to the end of the input.

### <a id="io.args"></a>`args`

```ec
fn args(): String[]
```

The arguments the program was started with, without its own name: `./app -v in.txt` gives ["-v", "in.txt"].
With `eclang run`, they are what follows `--`: `eclang run app.ec -- -v in.txt`.

### <a id="io.programName"></a>`programName`

```ec
fn programName(): String
```

How the program was started, the first word of the command line: "./app".

### <a id="io.open"></a>`open`

```ec
fn open(path: String, mode: OpenMode = :read): File throws IoError
```

Opens a file. New files get the usual permissions (rw-r--r--, less the umask).

    val f = try io.open("log.txt", :append)
    defer io.close(f)

**Parameters**

- `path` — where the file is
- `mode` — what to do with it; see [`OpenMode`](#io.OpenMode)

**Throws**

- `IoError` — when the file can't be opened: it doesn't exist (for `:read` and `:update`), permission is denied, ...

### <a id="io.close"></a>`close`

```ec
fn close(f: File)
```

Writes out what is buffered and closes `f`. Don't use `f` after this.

### <a id="io.read"></a>`read`

```ec
fn read(f: File, n: Size): String throws IoError
```

Reads up to `n` bytes from where `f` is.

**Returns** the bytes read: fewer than `n` at the end of the file, and "" when nothing is left

**Throws**

- `IoError` — when reading fails

### `readLine`

```ec
fn readLine(f: File): String? throws IoError
```

Reads the next line of `f`.

**Returns** the line, without its line break ("\n" or "\r\n"); null at the end of the file

**Throws**

- `IoError` — when reading fails

### `readAll`

```ec
fn readAll(f: File): String throws IoError
```

Reads everything from where `f` is to its end.

**Throws**

- `IoError` — when reading fails

### <a id="io.write"></a>`write`

```ec
fn write(f: File, s: String) throws IoError
```

Writes all of `s` at where `f` is. The file buffers it; see [`flush`](#io.flush).

**Throws**

- `IoError` — when writing fails (the disk is full, say)

### <a id="io.writeLine"></a>`writeLine`

```ec
fn writeLine(f: File, s: String) throws IoError
```

Writes `s` and a line break.

**Throws**

- `IoError` — when writing fails

### `flush`

```ec
fn flush(f: File) throws IoError
```

Writes out what `f` buffers.

**Throws**

- `IoError` — when writing fails

### <a id="io.seek"></a>`seek`

```ec
fn seek(f: File, offset: Long) throws IoError
```

Moves to byte `offset` from the start of `f`, where the next read or write happens.

**Throws**

- `IoError` — when `f` can't seek (a pipe, say)

### <a id="io.readFile"></a>`readFile`

```ec
fn readFile(path: String): String throws IoError
```

Reads a whole file: `val text = try io.readFile("notes.txt")`.

**Returns** what the file holds

**Throws**

- `IoError` — when the file can't be opened or read

### <a id="io.readLines"></a>`readLines`

```ec
fn readLines(path: String): String[] throws IoError
```

Reads a whole file as lines: `for line in try io.readLines("notes.txt") do ...`.

**Returns** the lines, without their line breaks

**Throws**

- `IoError` — when the file can't be opened or read

### <a id="io.writeFile"></a>`writeFile`

```ec
fn writeFile(path: String, s: String) throws IoError
```

Makes the file at `path` hold `s`, replacing what it held; it is made if it doesn't exist.

**Throws**

- `IoError` — when the file can't be opened or written

### <a id="io.appendFile"></a>`appendFile`

```ec
fn appendFile(path: String, s: String) throws IoError
```

Adds `s` at the end of the file at `path`, making the file if it doesn't exist.

**Throws**

- `IoError` — when the file can't be opened or written

### <a id="io.exists"></a>`exists`

```ec
fn exists(path: String): Bool
```

Whether something (a file, a directory, ...) is at `path`.

### <a id="io.isFile"></a>`isFile`

```ec
fn isFile(path: String): Bool
```

Whether `path` is a regular file (not a directory, a device, ...). False when nothing is there.

### <a id="io.isDir"></a>`isDir`

```ec
fn isDir(path: String): Bool
```

Whether `path` is a directory. False when nothing is there.

### <a id="io.fileSize"></a>`fileSize`

```ec
fn fileSize(path: String): Long throws IoError
```

The size of the file at `path`, in bytes.

**Throws**

- `IoError` — when nothing is at `path`, or it can't be looked at

### <a id="io.modified"></a>`modified`

```ec
fn modified(path: String): time.Time throws IoError
```

When the file at `path` was last changed.

**Throws**

- `IoError` — when nothing is at `path`, or it can't be looked at

### <a id="io.remove"></a>`remove`

```ec
fn remove(path: String) throws IoError
```

Deletes the file at `path`. See [`removeDir`](#io.removeDir) for a directory.

**Throws**

- `IoError` — when it doesn't exist or can't be deleted

### <a id="io.rename"></a>`rename`

```ec
fn rename(source: String, target: String) throws IoError
```

Moves (or renames) a file, replacing what is at `target`.

**Parameters**

- `source` — the file to move
- `target` — where it goes

**Throws**

- `IoError` — when it can't be moved (the error has the path of `source`)

### <a id="io.copy"></a>`copy`

```ec
fn copy(source: String, target: String) throws IoError
```

Copies a file, replacing what is at `target`; the copy gets the same permissions.

**Parameters**

- `source` — the file to copy
- `target` — where the copy goes

**Throws**

- `IoError` — when it can't be copied (the error has the path of `source`)

### <a id="io.makeDir"></a>`makeDir`

```ec
fn makeDir(path: String) throws IoError
```

Makes a directory, and the directories above it that don't exist. Nothing happens if it exists.

**Throws**

- `IoError` — when a directory can't be made

### <a id="io.removeDir"></a>`removeDir`

```ec
fn removeDir(path: String) throws IoError
```

Deletes a directory, which must be empty.

**Throws**

- `IoError` — when it doesn't exist, isn't empty or can't be deleted

### <a id="io.listDir"></a>`listDir`

```ec
fn listDir(path: String): String[] throws IoError
```

Lists a directory: `for name in io.sorted(try io.listDir(".")) do ...`.

**Returns** the names in it (not "." and "..", and not the whole paths), in the order the system gives them;
see [`sorted`](#io.sorted) for an order

**Throws**

- `IoError` — when the directory can't be opened

### <a id="io.sorted"></a>`sorted`

```ec
fn sorted(names: String[]): String[]
```

Sorts names in place, the way a person sorts file names ("file9" before "file10"; see `naturalCompare`).

**Returns** `names`, sorted

### <a id="io.currentDir"></a>`currentDir`

```ec
fn currentDir(): String throws IoError
```

The directory the program runs in, as an absolute path.

**Throws**

- `IoError` — when it can't be found (it was deleted, say)

### <a id="io.tempDir"></a>`tempDir`

```ec
fn tempDir(): String throws IoError
```

The directory for temporary files (such as /tmp).

**Throws**

- `IoError` — when there is none that can be written

### <a id="io.env"></a>`env`

```ec
fn env(name: String): String?
```

The value of the environment variable `name` (`io.env("HOME")`), or null when it isn't set.
