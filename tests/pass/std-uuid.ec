use "core:io.ec"
use "core:time.ec"
use "core:structures.ec"
use "core:uuid.ec"

fn main(): Int throws Error do
    val a = try uuid.v4()
    io.log(uuid.version(a))
    val t = uuid.text(a)
    io.log(t.len)
    io.log(t[14])
    io.log(t[19] == '8' or t[19] == '9' or t[19] == 'a' or t[19] == 'b')
    io.log(try uuid.parse(t) == a)
    io.log(try uuid.parse(t.toUpper()) == a)
    io.log(a != try uuid.v4())
    io.log("${a}" == t)

    val at = time.fromUnixMillis(1700000000123)
    val k = try uuid.v7(at)
    io.log(uuid.version(k))
    io.log(uuid.text(k)[0..<13])
    io.log(time.unixMillis(uuid.timeOf(k) ?? time.fromUnix(0)))
    io.log(uuid.timeOf(a) == null)
    val later = try uuid.v7(time.fromUnixMillis(1700000000124))
    io.log(uuid.compare(k, later))
    io.log(uuid.compare(later, k))
    io.log(uuid.compare(k, k))

    val fixed = try uuid.parse("{0B5C9A3E-6F1D-4C8A-9D2E-7A41B3C5E6F8}")
    io.log("${fixed}")
    val urn = try uuid.parse("urn:uuid:0b5c9a3e6f1d4c8a9d2e7a41b3c5e6f8")
    io.log("${urn}")
    io.log(uuid.version(fixed))
    io.log("${uuid.zero}")
    io.log(uuid.isZero(uuid.zero))
    val b = uuid.bytes(fixed)
    io.log(b.len)
    io.log(b[0])
    io.log(b[15])
    io.log(try uuid.fromBytes(b) == fixed)
    try uuid.fromBytes(b[0..<4]) catch e do
        io.log(e.message)

    io.log(uuid.isValid("0b5c9a3e-6f1d-4c8a-9d2e-7a41b3c5e6f8"))
    for bad in ["", "0b5c9a3e-6f1d-4c8a-9d2e-7a41b3c5e6fg", "0b5c9a3e+6f1d-4c8a-9d2e-7a41b3c5e6f8", "0b5c9a3e"] do
        try uuid.parse(bad) catch e do
            io.log("${e.message} at ${e.position}")

    val seen: Set<uuid.Uuid> = set()
    seen.add(fixed)
    seen.add(try uuid.parse(uuid.text(fixed)))
    seen.add(a)
    io.log(seen.size())
    ret 0
