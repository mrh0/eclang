use "core:string.ec"
use "core:io.ec"
use "core:path.ec"

// core:io.ec: the console, files, directories and paths.
fn main(): Int throws Error do
    io.print("no line break, ")
    io.print("then one"c)
    io.print('\n')
    io.println("println")
    io.flush()

    val dir = path.join(try io.tempDir(), "eclang-std-io-test")
    try io.makeDir(path.join(dir, "sub"))
    val file = path.join(dir, "file9.txt")
    try io.writeFile(file, "first\r\nsecond\n")
    try io.appendFile(file, "third")
    io.log(io.exists(file))
    io.log(io.isFile(file))
    io.log(io.isDir(dir))
    io.log(try io.fileSize(file))
    for line in try io.readLines(file) do
        io.log(line)

    val f = try io.open(file)
    io.log(try io.read(f, 5))
    io.log(try io.readLine(f) ?? "end")
    io.log(try io.readLine(f) ?? "end")
    io.log(try io.readAll(f))
    io.log(try io.readLine(f) ?? "end")
    io.close(f)

    val g = try io.open(file, :update)
    try io.seek(g, 1)
    try io.write(g, "IRST")
    io.close(g)
    io.log(before(try io.readFile(file), "\r"))

    try io.copy(file, path.join(dir, "file10.txt"))
    for name in io.sorted(try io.listDir(dir)) do
        io.log(name)
    try io.rename(path.join(dir, "file10.txt"), path.join(dir, "renamed.txt"))
    io.log(io.exists(path.join(dir, "file10.txt")))
    io.log(time.isAfter(try io.modified(file), time.fromUnix(0)))

    try io.readFile(path.join(dir, "missing.txt")) catch e do
        io.log(e.code == ERR_NOT_FOUND)
        io.log(path.base(e.path))

    try io.remove(file)
    try io.remove(path.join(dir, "renamed.txt"))
    try io.removeDir(path.join(dir, "sub"))
    try io.removeDir(dir)
    io.log(io.exists(dir))

    io.log(path.base("a/b/c.txt"))
    io.log(path.dir("a/b/c.txt"))
    io.log(path.dir("c.txt"))
    io.log(path.dir("/c.txt"))
    io.log(path.extension("a/photo.jpg"))
    io.log(path.extension(".bashrc"))
    io.log(path.join("logs/", "today.txt"))
    io.log(path.join("logs", "/abs"))
    io.log(io.env("ECLANG_SURELY_UNSET") ?? "unset")
    io.log(io.env("PATH") != null)
    ret 0
