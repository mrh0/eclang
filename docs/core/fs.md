# Module `fs`

The file system beyond single files: walking a directory tree, glob patterns, temporary files and memory-mapped
reads. Built on APR (apr_file_info.h, apr_fnmatch.h, apr_mmap.h). See core:io.ec for reading and writing files,
and core:path.ec for joining, normalising and relating paths.

    use "core:fs.ec"
    for e in try fs.walk("src") do                        // everything below src, parents first
        io.log("${e.path}: ${e.size} bytes") if e.kind == :file

    for path in fs.glob("src/*.ec") do                    // a whole part ** is any number of directories
        io.log(path)
    io.log(fs.matches("*.txt", "notes.txt"))              // true

    val t = try fs.tempFile("report")                     // deleted when it is closed
    try io.writeLine(t.file, "hello")
    io.close(t.file)
    val dir = try fs.tempDir()
    try fs.removeAll(dir)

    val m = try fs.map("big.log")                         // no copy: the system reads it as it is used
    defer fs.unmap(m)
    io.log(m.text.count('\n'))

What is returned is allocated in the current pool. Failures are IoErrors, with the path and the reason.

## Types

### <a id="fs.Kind"></a>`Kind`

```ec
declare type Kind as :file | :dir | :link | :other
```

What an [`Entry`](#fs.Entry) is:

- `:file`: a regular file
- `:dir`: a directory
- `:link`: a symbolic link, whatever it points to
- `:other`: a device, a pipe, a socket, ...

### <a id="fs.Entry"></a>`Entry`

```ec
rec Entry
```

Something in a directory tree, from [`walk`](#fs.walk).

**Fields**

- <a id="fs.Entry.path"></a>`path: String` — Its path: the path given to [`walk`](#fs.walk), and then the way down.
- <a id="fs.Entry.name"></a>`name: String` — Its name, the last part of the path.
- <a id="fs.Entry.kind"></a>`kind: Kind` — What it is.
- <a id="fs.Entry.size"></a>`size: Long` — Its size in bytes.
- <a id="fs.Entry.modified"></a>`modified: time.Time` — When it was last changed.
- <a id="fs.Entry.depth"></a>`depth: Int` — How far below the top it is: 1 for what is directly in it.

### <a id="fs.TempFile"></a>`TempFile`

```ec
rec TempFile
```

A new temporary file, from [`tempFile`](#fs.tempFile).

**Fields**

- <a id="fs.TempFile.file"></a>`file: io.File` — The open file, for reading and writing.
- <a id="fs.TempFile.path"></a>`path: String` — Where it is.

### <a id="fs.Mapped"></a>`Mapped`

```ec
rec Mapped
```

A file mapped into memory, from [`map`](#fs.map).

**Fields**

- <a id="fs.Mapped.text"></a>`text: String` — What the file holds. It isn't a copy: don't change it, and don't use it after [`unmap`](#fs.unmap).
- <a id="fs.Mapped.handle"></a>`handle: @apr_mmap_t?` — The APR mapping, null for an empty file.

## Functions

### <a id="fs.walk"></a>`walk`

```ec
fn walk(root: String, maxDepth: Int = -1): Entry[] throws IoError
```

Everything below a directory: each directory comes before what it holds, and names are sorted by their bytes, so the
order is the same every time. `root` itself isn't part of it. Symbolic links are given as `:link` and not followed,
so a link to a parent can't make it loop. What disappears during the walk is left out.

**Parameters**

- `root` — the directory
- `maxDepth` — how many levels to go down, 1 for what is directly in `root`; -1 for all of them

**Throws**

- `IoError` — when a directory can't be read

### <a id="fs.glob"></a>`glob`

```ec
fn glob(pattern: String): String[]
```

The paths that fit a pattern, sorted by their bytes. In each part of the path, `*` is any text, `?` any one
character and `[a-z]` one of a set; a whole part `**` is any number of directories, and at the end everything below.
As in a shell, a name starting with a dot only fits a pattern that says so, and a directory that can't be read
counts as empty.

    fs.glob("*.txt")                  // in the current directory
    fs.glob("src/**")                 // everything below src
    fs.glob("/var/log/*.[0-9]")

**Returns** the paths; none is no error

### <a id="fs.matches"></a>`matches`

```ec
fn matches(pattern: String, path: String): Bool
```

Whether `path` fits a pattern, by the rules of [`glob`](#fs.glob), without asking the file system.

**Parameters**

- `pattern` — the pattern
- `path` — the path to test, as text

### <a id="fs.tempFile"></a>`tempFile`

```ec
fn tempFile(prefix: String = "tmp"): TempFile throws IoError
```

Makes a new, empty file in the temporary directory, with a name nothing else has, that only this user can read. It
is deleted when it is closed, or when its pool is freed.

**Parameters**

- `prefix` — how the name starts

**Throws**

- `IoError` — when the file can't be made

### <a id="fs.tempDir"></a>`tempDir`

```ec
fn tempDir(prefix: String = "tmp"): String throws IoError
```

Makes a new, empty directory in the temporary directory, that only this user can use. Nothing deletes it by
itself: see [`removeAll`](#fs.removeAll).

**Parameters**

- `prefix` — how the name starts

**Returns** its path

**Throws**

- `IoError` — when it can't be made

### <a id="fs.removeAll"></a>`removeAll`

```ec
fn removeAll(path: String) throws IoError
```

Deletes `path` and, for a directory, everything below it. Symbolic links are deleted, not followed. It is no error
when nothing is there.

**Throws**

- `IoError` — when something can't be deleted, or `path` is the root, "." or ".."

### <a id="fs.map"></a>`map`

```ec
fn map(path: String): Mapped throws IoError
```

Maps a file into memory. Nothing is read now: the system brings in the parts that are used, so this suits a large
file of which a program reads a part, or which doesn't fit a pool. The mapping ends with [`unmap`](#fs.unmap), or when its
pool is freed. If something else makes the file shorter meanwhile, reading past its new end stops the program.

**Throws**

- `IoError` — when the file can't be opened or mapped

### <a id="fs.unmap"></a>`unmap`

```ec
fn unmap(m: Mapped)
```

Ends a mapping. Don't use its `text` after this.
