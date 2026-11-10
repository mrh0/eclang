/**
 * Paths of files and directories, as text: joining, taking apart, normalising, and making absolute or relative.
 * Only {@link absolute} and {@link relative} ask the system anything (the current directory); nothing has to exist.
 *
 *     use "core:path.ec"
 *     val file = path.join("logs", "2026", "today.txt")   // logs/2026/today.txt
 *     io.log(path.base(file))                             // today.txt
 *     io.log(path.dir(file))                              // logs/2026
 *     io.log(path.extension(file))                        // txt
 *     io.log(path.stem(file))                             // today
 *     io.log(path.withExtension(file, "md"))              // logs/2026/today.md
 *     io.log(path.normalize("a/./b/../c/"))               // a/c
 *     io.log(try path.absolute("notes.txt"))              // /home/ada/notes.txt
 *     io.log(try path.relative("/a/b", "/a/c/d"))         // ../c/d
 *
 * Paths use "/", as on Linux. A variable named `path` hides the module in its scope, as any variable hides a module
 * of its name: name it `file` or `p` where the module's functions are needed too.
 * What is returned is allocated in the current pool, or is a part of the path given.
 */
module "path"
use "core:errors.ec"
use "core:string.ec"
use "core:structures.ec"
use "core:io.ec"
use "core:apr/errno.ec"
use "core:apr/file_info.ec"

/** The separator between the parts of a path. */
const separator: Char = '/'

/** Joins two paths with one "/" between them: `join("logs/", "today.txt")` is "logs/today.txt". An absolute `b` wins. */
priv fn joinTwo(a: String, b: String): String do
    ret b if a.len == 0 or startsWith(b, '/')
    ret a if b.len == 0
    ret concat(a, b) if endsWith(a, '/')
    ret concat(a, "/", b)

/**
 * The parts with one "/" between each two: `join("logs/", "2026", "today.txt")` is "logs/2026/today.txt". An absolute
 * part starts again from it (`join("a", "/b")` is "/b"), and an empty part adds nothing.
 */
fn join(first: String, ...more: String): String do
    var joined = first
    for part in more do
        joined = joinTwo(joined, part)
    ret joined

/** The last part of a path: `base("a/b/c.txt")` is "c.txt". A "/" at the end is left out; `base("/")` is "/". */
fn base(path: String): String do
    val p = if path.len > 1 do removeSuffix(path, "/") else path
    if val i = lastIndexOf(p, '/') do
        ret if p.len == 1 do p else p[i + 1..<p.len]
    ret p

/** The path without its last part: `dir("a/b/c.txt")` is "a/b", `dir("c.txt")` is ".", `dir("/c.txt")` is "/". */
fn dir(path: String): String do
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
    val name = base(path)
    if val i = lastIndexOf(name, '.') do
        ret if i == 0 do "" else name[i + 1..<name.len]
    ret ""

/** The last part without its extension: `stem("a/photo.tar.gz")` is "photo.tar", `stem(".bashrc")` is ".bashrc". */
fn stem(path: String): String do
    val name = base(path)
    val ext = extension(name)
    ret if ext.len == 0 do name else name[0..<name.len - ext.len - 1]

/**
 * `path` with its extension replaced by `ext` (given without the dot), or with `ext` added when it has none:
 * `withExtension("a/photo.jpg", "png")` is "a/photo.png". An empty `ext` takes the extension away.
 */
fn withExtension(path: String, ext: String): String do
    val p = if path.len > 1 do removeSuffix(path, "/") else path
    val old = extension(p)
    val kept = if old.len == 0 do p else p[0..<p.len - old.len - 1]
    ret if ext.len == 0 do kept else concat(kept, ".", ext)

/** Whether `path` starts at the root of the file system. */
fn isAbsolute(path: String): Bool = startsWith(path, '/')

/**
 * The shortest way to write `path`, as text: "." parts and doubled "/"s go, and ".." takes the part before it away.
 * `normalize("a/./b/../c/")` is "a/c". A relative path can keep ".." at its start; above the root there is only the
 * root. The file system isn't asked, so where `b` is a link to elsewhere, "a/b/.." may not be the same place as "a".
 * @return the path, "." when nothing is left
 */
fn normalize(path: String): String do
    val parts: List<String> = list()
    val rooted = isAbsolute(path)
    for part in path.split('/') do
        if part == ".." do
            if parts.len > 0 and parts.buffer[parts.len - 1] != ".." do
                parts.pop()
            else if not rooted do
                parts.push(part)
        else if part.len > 0 and part != "." do
            parts.push(part)
    val joined = parts.items().join("/")
    ret concat("/", joined) if rooted
    ret if joined.len == 0 do "." else joined

/**
 * `path` as a normalised absolute path, from the current directory. Nothing has to exist there.
 * @throws when the current directory can't be found
 */
fn absolute(path: String): String throws IoError do
    var merged: CString = ""c
    val status = apr_filepath_merge(mut merged, cstr(try io.currentDir()), cstr(path), 0, pool)
    throw ioError(status, path) if status != APR_SUCCESS
    ret fromC(merged)

/**
 * The way from the directory `start` to `target`: `relative("/a/b", "/a/c/d")` is "../c/d". Relative paths start at the
 * current directory.
 * @return the way, "." when they are the same place
 * @throws when the current directory can't be found
 */
fn relative(start: String, target: String): String throws IoError do
    val from = (try absolute(start)).split('/')
    val goal = (try absolute(target)).split('/')
    var same: Size = 0
    while same < from.len and same < goal.len and from[same] == goal[same] do
        same += 1
    val parts: List<String> = list()
    for i in same..<from.len where from[i].len > 0 do
        parts.push("..")
    for i in same..<goal.len where goal[i].len > 0 do
        parts.push(goal[i])
    ret if parts.len == 0 do "." else parts.items().join("/")
