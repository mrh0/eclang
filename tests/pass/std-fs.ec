use "core:io.ec"
use "core:path.ec"
use "core:fs.ec"
use "core:string.ec"

fn main(): Int throws Error do
    val dir = try fs.tempDir("eclang-std-fs")
    try io.makeDir(path.join(dir, "a/b"))
    try io.makeDir(path.join(dir, ".hid"))
    try io.writeFile(path.join(dir, "a/x.txt"), "hello\nworld\n")
    try io.writeFile(path.join(dir, "a/b/y.ec"), "1")
    try io.writeFile(path.join(dir, "z.ec"), "22")
    try io.writeFile(path.join(dir, ".hid/w.ec"), "")
    try io.writeFile(path.join(dir, "empty"), "")
    for e in try fs.walk(dir) do
        val size = if e.kind == :file do e.size else 0
        io.log("${try path.relative(dir, e.path)} ${e.kind as Atom} ${size} ${e.depth}")
    for p in fs.glob(path.join(dir, "**/*.ec")) do
        io.log(try path.relative(dir, p))
    for p in fs.glob(path.join(dir, "*/x.*")) do
        io.log(try path.relative(dir, p))
    for p in fs.glob(path.join(dir, "a/**")) do
        io.log(try path.relative(dir, p))
    for p in fs.glob(path.join(dir, "**/nothing")) do
        io.log(p)
    io.log(fs.glob("/no/such/dir/*").len)
    for w in try fs.walk(dir, 1) do
        io.log(w.name)
    try fs.walk(path.join(dir, "missing")) catch missing do
        io.log(missing.code == ERR_NOT_FOUND)
    io.log(fs.matches("src/**/*.ec", "src/a/b.ec"))
    io.log(fs.matches("src/**/*.ec", "src/b.ec"))
    io.log(fs.matches("*.ec", "a/b.ec"))
    io.log(fs.matches("*.ec", ".hidden.ec"))
    io.log(fs.matches("a/**", "a"))
    io.log(fs.matches("a/**", "a/b/c"))
    io.log(fs.matches("[a-c]?.txt", "b1.txt"))
    io.log(path.normalize("a/./b/../c/"))
    io.log(path.normalize("/../a/.."))
    io.log(path.normalize("../../a"))
    io.log(path.normalize(""))
    io.log(try path.relative("/a/b", "/a/c/d"))
    io.log(try path.relative("/a/b", "/a/b"))
    io.log(path.isAbsolute(try path.absolute("x/../y")))
    io.log(endsWith(try path.absolute("x/../y"), "/y"))
    val m = try fs.map(path.join(dir, "a/x.txt"))
    io.log(m.text.count('\n'))
    io.log(m.text)
    fs.unmap(m)
    val e = try fs.map(path.join(dir, "empty"))
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
