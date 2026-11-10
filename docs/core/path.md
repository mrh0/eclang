# Module `path`

Paths of files and directories, as text: joining, taking apart, normalising, and making absolute or relative.
Only [`absolute`](#path.absolute) and [`relative`](#path.relative) ask the system anything (the current directory); nothing has to exist.

    use "core:path.ec"
    val file = path.join("logs", "2026", "today.txt")   // logs/2026/today.txt
    io.log(path.base(file))                             // today.txt
    io.log(path.dir(file))                              // logs/2026
    io.log(path.extension(file))                        // txt
    io.log(path.stem(file))                             // today
    io.log(path.withExtension(file, "md"))              // logs/2026/today.md
    io.log(path.normalize("a/./b/../c/"))               // a/c
    io.log(try path.absolute("notes.txt"))              // /home/ada/notes.txt
    io.log(try path.relative("/a/b", "/a/c/d"))         // ../c/d

Paths use "/", as on Linux. A variable named `path` hides the module in its scope, as any variable hides a module
of its name: name it `file` or `p` where the module's functions are needed too.
What is returned is allocated in the current pool, or is a part of the path given.

## Values

### <a id="path.separator"></a>`separator`

```ec
const separator: Char = '/'
```

The separator between the parts of a path.

## Functions

### <a id="path.join"></a>`join`

```ec
fn join(first: String, ...more: String): String
```

The parts with one "/" between each two: `join("logs/", "2026", "today.txt")` is "logs/2026/today.txt". An absolute
part starts again from it (`join("a", "/b")` is "/b"), and an empty part adds nothing.

### <a id="path.base"></a>`base`

```ec
fn base(path: String): String
```

The last part of a path: `base("a/b/c.txt")` is "c.txt". A "/" at the end is left out; `base("/")` is "/".

### <a id="path.dir"></a>`dir`

```ec
fn dir(path: String): String
```

The path without its last part: `dir("a/b/c.txt")` is "a/b", `dir("c.txt")` is ".", `dir("/c.txt")` is "/".

### <a id="path.extension"></a>`extension`

```ec
fn extension(path: String): String
```

The extension of the last part, without the dot: `extension("a/photo.jpg")` is "jpg". A name that starts
with a dot (".bashrc") has none.

**Returns** the extension, "" when there is none

### <a id="path.stem"></a>`stem`

```ec
fn stem(path: String): String
```

The last part without its extension: `stem("a/photo.tar.gz")` is "photo.tar", `stem(".bashrc")` is ".bashrc".

### <a id="path.withExtension"></a>`withExtension`

```ec
fn withExtension(path: String, ext: String): String
```

`path` with its extension replaced by `ext` (given without the dot), or with `ext` added when it has none:
`withExtension("a/photo.jpg", "png")` is "a/photo.png". An empty `ext` takes the extension away.

### <a id="path.isAbsolute"></a>`isAbsolute`

```ec
fn isAbsolute(path: String): Bool
```

Whether `path` starts at the root of the file system.

### <a id="path.normalize"></a>`normalize`

```ec
fn normalize(path: String): String
```

The shortest way to write `path`, as text: "." parts and doubled "/"s go, and ".." takes the part before it away.
`normalize("a/./b/../c/")` is "a/c". A relative path can keep ".." at its start; above the root there is only the
root. The file system isn't asked, so where `b` is a link to elsewhere, "a/b/.." may not be the same place as "a".

**Returns** the path, "." when nothing is left

### <a id="path.absolute"></a>`absolute`

```ec
fn absolute(path: String): String throws IoError
```

`path` as a normalised absolute path, from the current directory. Nothing has to exist there.

**Throws**

- `IoError` — when the current directory can't be found

### <a id="path.relative"></a>`relative`

```ec
fn relative(start: String, target: String): String throws IoError
```

The way from the directory `start` to `target`: `relative("/a/b", "/a/c/d")` is "../c/d". Relative paths start at the
current directory.

**Returns** the way, "." when they are the same place

**Throws**

- `IoError` — when the current directory can't be found
