use "core:io.ec"
use "core:fs.ec"
use "core:string.ec"

fn main(): Int throws Error do
    val dir = try fs.tempDir("eclang-std-fs")
    try io.makeDir(io.joinPath(dir, "a/b"))
    try io.makeDir(io.joinPath(dir, ".hid"))
    try io.writeFile(io.joinPath(dir, "a/x.txt"), "hello\nworld\n")
    try io.writeFile(io.joinPath(dir, "a/b/y.ec"), "1")
    try io.writeFile(io.joinPath(dir, "z.ec"), "22")
    try io.writeFile(io.joinPath(dir, ".hid/w.ec"), "")
    try io.writeFile(io.joinPath(dir, "empty"), "")
    for e in try fs.walk(dir) do
        val size = if e.kind == :file do e.size else 0
        io.log("${try fs.relative(dir, e.path)} ${e.kind as Atom} ${size} ${e.depth}")
    for p in fs.glob(io.joinPath(dir, "**/*.ec")) do
        io.log(try fs.relative(dir, p))
    for p in fs.glob(io.joinPath(dir, "*/x.*")) do
        io.log(try fs.relative(dir, p))
    for p in fs.glob(io.joinPath(dir, "a/**")) do
        io.log(try fs.relative(dir, p))
    for p in fs.glob(io.joinPath(dir, "**/nothing")) do
        io.log(p)
    io.log(fs.glob("/no/such/dir/*").len)
    for w in try fs.walk(dir, 1) do
        io.log(w.name)
    try fs.walk(io.joinPath(dir, "missing")) catch missing do
        io.log(missing.code == ERR_NOT_FOUND)
    io.log(fs.matches("src/**/*.ec", "src/a/b.ec"))
    io.log(fs.matches("src/**/*.ec", "src/b.ec"))
    io.log(fs.matches("*.ec", "a/b.ec"))
    io.log(fs.matches("*.ec", ".hidden.ec"))
    io.log(fs.matches("a/**", "a"))
    io.log(fs.matches("a/**", "a/b/c"))
    io.log(fs.matches("[a-c]?.txt", "b1.txt"))
    io.log(fs.normalize("a/./b/../c/"))
    io.log(fs.normalize("/../a/.."))
    io.log(fs.normalize("../../a"))
    io.log(fs.normalize(""))
    io.log(try fs.relative("/a/b", "/a/c/d"))
    io.log(try fs.relative("/a/b", "/a/b"))
    io.log(fs.isAbsolute(try fs.absolute("x/../y")))
    io.log(endsWith(try fs.absolute("x/../y"), "/y"))
    val m = try fs.map(io.joinPath(dir, "a/x.txt"))
    io.log(m.text.count('\n'))
    io.log(m.text)
    fs.unmap(m)
    val e = try fs.map(io.joinPath(dir, "empty"))
    io.log(e.text.len)
    val t = try fs.tempFile("rep")
    try io.writeLine(t.file, "hi")
    io.log(io.exists(t.path))
    io.close(t.file)
    io.log(io.exists(t.path))
    try fs.removeAll(dir)
    io.log(io.exists(dir))
    try fs.removeAll("/") catch problem do
        io.log(problem.message)
    ret 0
