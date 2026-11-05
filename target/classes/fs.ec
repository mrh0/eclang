/**
 * The file system beyond single files: walking a directory tree, glob patterns, temporary files, normalising paths
 * and memory-mapped reads. Built on APR (apr_file_info.h, apr_fnmatch.h, apr_mmap.h). See core:io.ec for reading and
 * writing files.
 *
 *     use "core:fs.ec"
 *     for e in try fs.walk("src") do                        // everything below src, parents first
 *         io.log("${e.path}: ${e.size} bytes") if e.kind == :file
 *
 *     for path in fs.glob("src/*.ec") do                    // a whole part ** is any number of directories
 *         io.log(path)
 *     io.log(fs.matches("*.txt", "notes.txt"))              // true
 *
 *     val t = try fs.tempFile("report")                     // deleted when it is closed
 *     try io.writeLine(t.file, "hello")
 *     io.close(t.file)
 *     val dir = try fs.tempDir()
 *     try fs.removeAll(dir)
 *
 *     io.log(fs.normalize("a/./b/../c"))                    // a/c
 *     io.log(try fs.absolute("notes.txt"))                  // /home/ada/notes.txt
 *
 *     val m = try fs.map("big.log")                         // no copy: the system reads it as it is used
 *     defer fs.unmap(m)
 *     io.log(m.text.count('\n'))
 *
 * What is returned is allocated in the current pool. Failures are IoErrors, with the path and the reason.
 */
module "fs"
use "core:errors.ec"
use "core:string.ec"
use "core:array.ec"
use "core:structures.ec"
use "core:time.ec"
use "core:io.ec"
use "core:c/stdlib.ec"
use "core:apr/errno.ec"
use "core:apr/file_info.ec"
use "core:apr/file_io.ec"
use "core:apr/fnmatch.ec"
use "core:apr/mmap.ec"

// ---------------------------------------------------------------------------------------------------
// Walking

/**
 * What an {@link Entry} is:
 *
 * - `:file`: a regular file
 * - `:dir`: a directory
 * - `:link`: a symbolic link, whatever it points to
 * - `:other`: a device, a pipe, a socket, ...
 */
declare type Kind as :file | :dir | :link | :other

/** Something in a directory tree, from {@link walk}. */
rec Entry as
    /** Its path: the path given to {@link walk}, and then the way down. */
    path: String
    /** Its name, the last part of the path. */
    name: String
    /** What it is. */
    kind: Kind
    /** Its size in bytes. */
    size: Long
    /** When it was last changed. */
    modified: time.Time
    /** How far below the top it is: 1 for what is directly in it. */
    depth: Int

/** The {@link Kind} of APR's file type `filetype`. */
priv fn kindOf(filetype: Int): Kind do
    ret :file if filetype == APR_REG
    ret :dir if filetype == APR_DIR
    ret :link if filetype == APR_LNK
    ret :other

/**
 * What is at `path`, without following a link, or null when nothing is there any more.
 * @throws when it can't be looked at
 */
priv fn entryAt(path: String, name: String, depth: Int): Entry? throws IoError do
    var info: apr_finfo_t
    val status = apr_stat(addrof(info), cstr(path), APR_FINFO_LINK | APR_FINFO_TYPE | APR_FINFO_SIZE | APR_FINFO_MTIME, pool)
    ret null if APR_STATUS_IS_ENOENT(status) != 0
    // APR_INCOMPLETE: some of the wanted fields aren't known on this system; the others are filled in.
    throw ioError(status, path) if status != APR_SUCCESS and status != APR_INCOMPLETE
    ret Entry { path, name, kindOf(info.filetype), info.size, time.Time { info.mtime }, depth }

/** Adds what is below `dir` to `found`. */
priv fn walkInto(dir: String, depth: Int, maxDepth: Int, found: @List<Entry>) throws IoError do
    val names = try io.listDir(dir)
    names.sort()
    for name in names do
        val path = io.joinPath(dir, name)
        if val e = try entryAt(path, name, depth) do
            found.push(e)
            if e.kind == :dir and (maxDepth < 0 or depth < maxDepth) do
                try walkInto(path, depth + 1, maxDepth, found)

/**
 * Everything below a directory: each directory comes before what it holds, and names are sorted by their bytes, so the
 * order is the same every time. `root` itself isn't part of it. Symbolic links are given as `:link` and not followed,
 * so a link to a parent can't make it loop. What disappears during the walk is left out.
 *
 * @param root the directory
 * @param maxDepth how many levels to go down, 1 for what is directly in `root`; -1 for all of them
 * @throws when a directory can't be read
 */
fn walk(root: String, maxDepth: Int = -1): Entry[] throws IoError do
    val found: @List<Entry> = list()
    try walkInto(root, 1, maxDepth, found)
    ret found.items()

// ---------------------------------------------------------------------------------------------------
// Patterns

/** The parts of `pattern` between its "/"s, without empty ones and with `**` once where it is repeated. */
priv fn segmentsOf(pattern: String): @List<String> do
    val out: @List<String> = list()
    for part in pattern.split('/') do
        val repeated = part == "**" and out.len > 0 and out.buffer[out.len - 1] == "**"
        out.push(part) if part.len > 0 and not repeated
    ret out

/** Whether the name `name` fits the part of a pattern `segment`; a name starting with a dot only fits a part that does too. */
priv fn fits(segment: String, name: String): Bool = apr_fnmatch(cstr(segment), cstr(name), APR_FNM_PERIOD) == APR_SUCCESS

/** The names in `dir`, none when it can't be read: a shell's `*` doesn't stop there either. */
priv fn namesIn(dir: String): String[] do
    ret io.listDir(if dir.len == 0 do "." else dir) ?? new String[0]

/** Whether `path` is a directory, and not a link to one: `**` doesn't go through links, so it can't loop. */
priv fn isRealDir(path: String): Bool do
    val e = entryAt(path, "", 0) ?? null
    if val found = e do
        ret found.kind == :dir
    ret false

/** Adds the paths that fit the parts of `segments` from `at` on, below `base`, to `found`. */
priv fn expand(base: String, segments: @List<String>, at: Size, found: @List<String>) do
    if at == segments.len do
        found.push(base)
    else do
        val segment = segments.buffer[at]
        val last = at + 1 == segments.len
        if segment == "**" do
            if not last do
                expand(base, segments, at + 1, found)
            for name in namesIn(base) where not startsWith(name, '.') do
                val path = io.joinPath(base, name)
                found.push(path) if last
                expand(path, segments, at, found) if isRealDir(path)
        else if apr_fnmatch_test(cstr(segment)) == 0 do
            val path = io.joinPath(base, segment)
            expand(path, segments, at + 1, found) if io.exists(path)
        else do
            for name in namesIn(base) where fits(segment, name) do
                val path = io.joinPath(base, name)
                expand(path, segments, at + 1, found) if last or io.isDir(path)

/**
 * The paths that fit a pattern, sorted by their bytes. In each part of the path, `*` is any text, `?` any one
 * character and `[a-z]` one of a set; a whole part `**` is any number of directories, and at the end everything below.
 * As in a shell, a name starting with a dot only fits a pattern that says so, and a directory that can't be read
 * counts as empty.
 *
 *     fs.glob("*.txt")                  // in the current directory
 *     fs.glob("src/**")                 // everything below src
 *     fs.glob("/var/log/*.[0-9]")
 *
 * @return the paths; none is no error
 */
fn glob(pattern: String): String[] do
    val found: @List<String> = list()
    val base = if startsWith(pattern, '/') do "/" else ""
    val segments = segmentsOf(pattern)
    expand(base, segments, 0, found) if segments.len > 0
    val paths = found.items()
    paths.sort()
    ret paths

/** Whether the parts of `path` from `p` on fit the parts of a pattern from `s` on. */
priv fn segmentsFit(segments: @List<String>, s: Size, parts: @List<String>, p: Size): Bool do
    ret p == parts.len if s == segments.len
    val segment = segments.buffer[s]
    if segment == "**" do
        ret p < parts.len if s + 1 == segments.len
        var at = p
        while at <= parts.len do
            ret true if segmentsFit(segments, s + 1, parts, at)
            ret false if at == parts.len or startsWith(parts.buffer[at], '.')
            at += 1
        ret false
    ret p < parts.len and fits(segment, parts.buffer[p]) and segmentsFit(segments, s + 1, parts, p + 1)

/**
 * Whether `path` fits a pattern, by the rules of {@link glob}, without asking the file system.
 * @param pattern the pattern
 * @param path the path to test, as text
 */
fn matches(pattern: String, path: String): Bool do
    ret false if startsWith(pattern, '/') != startsWith(path, '/')
    ret segmentsFit(segmentsOf(pattern), 0, segmentsOf(path), 0)

// ---------------------------------------------------------------------------------------------------
// Temporary files

/** A new temporary file, from {@link tempFile}. */
rec TempFile as
    /** The open file, for reading and writing. */
    file: io.File
    /** Where it is. */
    path: String

/** A path for a template of mkstemp, in the temporary directory. */
priv fn template(prefix: String): String throws IoError = io.joinPath(try io.tempDir(), concat(prefix, "XXXXXX"))

/**
 * Makes a new, empty file in the temporary directory, with a name nothing else has, that only this user can read. It
 * is deleted when it is closed, or when its pool is freed.
 * @param prefix how the name starts
 * @throws when the file can't be made
 */
fn tempFile(prefix: String = "tmp"): TempFile throws IoError do
    val path = cstr(try template(prefix))
    var handle: @apr_file_t? = null
    val status = apr_file_mktemp(addrof(handle!), path, 0, pool)
    throw ioError(status, fromC(path)) if status != APR_SUCCESS
    val name = fromC(path)
    ret TempFile { io.File { handle!, name }, name }

/**
 * Makes a new, empty directory in the temporary directory, that only this user can use. Nothing deletes it by
 * itself: see {@link removeAll}.
 * @param prefix how the name starts
 * @return its path
 * @throws when it can't be made
 */
fn tempDir(prefix: String = "tmp"): String throws IoError do
    val path = cstr(try template(prefix))
    if val made = mkdtemp(path) do
        ret fromC(made)
    throw ioError(apr_get_os_error(), fromC(path))

/**
 * Deletes `path` and, for a directory, everything below it. Symbolic links are deleted, not followed. It is no error
 * when nothing is there.
 * @throws when something can't be deleted, or `path` is the root, "." or ".."
 */
fn removeAll(path: String) throws IoError do
    val clean = normalize(path)
    throw IoError { ERR_ARGUMENT, "refusing to delete '${path}': name the directory itself, not the root, '.' or '..'", APR_EINVAL, path } if clean == "/" or clean == "." or clean == ".." or endsWith(clean, "/..")
    if val e = try entryAt(path, "", 0) do
        if e.kind == :dir do
            for name in try io.listDir(path) do
                try removeAll(io.joinPath(path, name))
            try io.removeDir(path)
        else do
            try io.remove(path)

// ---------------------------------------------------------------------------------------------------
// Paths

/** Whether `path` starts at the root of the file system. */
fn isAbsolute(path: String): Bool = startsWith(path, '/')

/**
 * The shortest way to write `path`, as text: "." parts and doubled "/"s go, and ".." takes the part before it away.
 * `normalize("a/./b/../c/")` is "a/c". A relative path can keep ".." at its start; above the root there is only the
 * root. The file system isn't asked, so where `b` is a link to elsewhere, "a/b/.." may not be the same place as "a".
 * @return the path, "." when nothing is left
 */
fn normalize(path: String): String do
    val parts: @List<String> = list()
    val rooted = isAbsolute(path)
    for part in path.split('/') do
        if part == ".." do
            if parts.len > 0 and parts.buffer[parts.len - 1] != ".." do
                parts.pop()
            else if not rooted do
                parts.push(part)
        else if part.len > 0 and part != "." do
            parts.push(part)
    val joined = join(parts.items(), "/")
    ret concat("/", joined) if rooted
    ret if joined.len == 0 do "." else joined

/**
 * `path` as a normalised absolute path, from the current directory. Nothing has to exist there.
 * @throws when the current directory can't be found
 */
fn absolute(path: String): String throws IoError do
    var merged: CString = ""c
    val status = apr_filepath_merge(addrof(merged), cstr(try io.currentDir()), cstr(path), 0, pool)
    throw ioError(status, path) if status != APR_SUCCESS
    ret fromC(merged)

/**
 * The way from the directory `base` to `path`: `relative("/a/b", "/a/c/d")` is "../c/d". Relative paths start at the
 * current directory.
 * @return the way, "." when they are the same place
 * @throws when the current directory can't be found
 */
fn relative(base: String, path: String): String throws IoError do
    val start = (try absolute(base)).split('/')
    val target = (try absolute(path)).split('/')
    var same: Size = 0
    while same < start.len and same < target.len and start[same] == target[same] do
        same += 1
    val parts: @List<String> = list()
    for i in same..<start.len where start[i].len > 0 do
        parts.push("..")
    for i in same..<target.len where target[i].len > 0 do
        parts.push(target[i])
    ret if parts.len == 0 do "." else join(parts.items(), "/")

// ---------------------------------------------------------------------------------------------------
// Memory-mapped files

/** A String's two fields: the only way to make one from a pointer without copying what it points to. */
priv rec StringFields as
    len: Size
    data: @Any

/** A file mapped into memory, from {@link map}. */
rec Mapped as
    /** What the file holds. It isn't a copy: don't change it, and don't use it after {@link unmap}. */
    text: String
    /** The APR mapping, null for an empty file. */
    handle: @apr_mmap_t?

/**
 * Maps a file into memory. Nothing is read now: the system brings in the parts that are used, so this suits a large
 * file of which a program reads a part, or which doesn't fit a pool. The mapping ends with {@link unmap}, or when its
 * pool is freed. If something else makes the file shorter meanwhile, reading past its new end stops the program.
 * @throws when the file can't be opened or mapped
 */
fn map(path: String): Mapped throws IoError do
    val size = try io.fileSize(path)
    ret Mapped { "", null } if size == 0
    // Not io.open: APR doesn't map a buffered file.
    var file: @apr_file_t? = null
    val opened = apr_file_open(addrof(file!), cstr(path), APR_FOPEN_READ, APR_FPROT_OS_DEFAULT, pool)
    throw ioError(opened, path) if opened != APR_SUCCESS
    defer apr_file_close(file!)
    var handle: @apr_mmap_t? = null
    val status = apr_mmap_create(addrof(handle!), file!, 0, size as Size, APR_MMAP_READ, pool)
    throw ioError(status, path) if status != APR_SUCCESS
    var start: @Any? = null
    val located = apr_mmap_offset(addrof(start!), handle!, 0)
    throw ioError(located, path) if located != APR_SUCCESS
    val view = new StringFields { size as Size, start! }
    ret Mapped { @(view !as @String), handle }

/** Ends a mapping. Don't use its `text` after this. */
fn unmap(m: Mapped) do
    if val handle = m.handle do
        apr_mmap_delete(handle)
