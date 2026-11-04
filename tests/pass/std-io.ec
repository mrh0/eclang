use "core:string.ec"
use "core:io.ec"

// core:io.ec: the console, files, directories and paths.
fn main(): Int throws Error do
    io.print("no line break, ")
    io.print("then one"c)
    io.print('\n')
    io.println("println")
    io.flush()

    val dir = io.joinPath(try io.tempDir(), "eclang-std-io-test")
    try io.makeDir(io.joinPath(dir, "sub"))
    val path = io.joinPath(dir, "file9.txt")
    try io.writeFile(path, "first\r\nsecond\n")
    try io.appendFile(path, "third")
    io.log(io.exists(path))
    io.log(io.isFile(path))
    io.log(io.isDir(dir))
    io.log(try io.fileSize(path))
    for line in try io.readLines(path) do
        io.log(line)

    val f = try io.open(path)
    io.log(try io.read(f, 5))
    io.log(try io.readLine(f) ?? "end")
    io.log(try io.readLine(f) ?? "end")
    io.log(try io.readAll(f))
    io.log(try io.readLine(f) ?? "end")
    io.close(f)

    val g = try io.open(path, :update)
    try io.seek(g, 1)
    try io.write(g, "IRST")
    io.close(g)
    io.log(before(try io.readFile(path), "\r"))

    try io.copy(path, io.joinPath(dir, "file10.txt"))
    for name in io.sorted(try io.listDir(dir)) do
        io.log(name)
    try io.rename(io.joinPath(dir, "file10.txt"), io.joinPath(dir, "renamed.txt"))
    io.log(io.exists(io.joinPath(dir, "file10.txt")))
    io.log(time.isAfter(try io.modified(path), time.fromUnix(0)))

    try io.readFile(io.joinPath(dir, "missing.txt")) catch e do
        io.log(e.code == ERR_NOT_FOUND)
        io.log(io.baseName(e.path))

    try io.remove(path)
    try io.remove(io.joinPath(dir, "renamed.txt"))
    try io.removeDir(io.joinPath(dir, "sub"))
    try io.removeDir(dir)
    io.log(io.exists(dir))

    io.log(io.baseName("a/b/c.txt"))
    io.log(io.dirName("a/b/c.txt"))
    io.log(io.dirName("c.txt"))
    io.log(io.dirName("/c.txt"))
    io.log(io.extension("a/photo.jpg"))
    io.log(io.extension(".bashrc"))
    io.log(io.joinPath("logs/", "today.txt"))
    io.log(io.joinPath("logs", "/abs"))
    io.log(io.env("ECLANG_SURELY_UNSET") ?? "unset")
    io.log(io.env("PATH") != null)
    ret 0
