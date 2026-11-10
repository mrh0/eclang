use "core:io.ec"
use "core:path.ec"
use "core:hash.ec"
use "core:encoding.ec"
use "core:string.ec"

// Names the C code embedded for core:hash.ec uses inside itself: it must not take them from a program.
val F = 1
val k = 2
val BYTE = 3
val WORD = 4
val ROTLEFT = 5
fn H(x: Int): Int = x + 1
fn sha256_init(): Int = 6

fn main(): Int throws Error do
    io.log(F + k + BYTE + WORD + ROTLEFT + H(0) + sha256_init())
    // Vectors from RFC 1321, FIPS 180 and RFC 4231.
    io.log(hash.md5(""))
    io.log(hash.md5("abc"))
    io.log(hash.sha1("abc"))
    io.log(hash.sha256(""))
    io.log(hash.sha256("abc"))
    io.log(hash.sha256("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"))
    io.log(hash.crc32("123456789"))
    io.log(hash.crc32("6789", hash.crc32("12345")))
    io.log(hash.fnv32("a"))
    io.log(hash.fnv64("a"))
    io.log(hash.hmac(:sha256, "Jefe", "what do ya want for nothing?"))
    io.log(hash.hmac(:md5, "Jefe", "what do ya want for nothing?"))
    io.log(hash.hmac(:sha1, "Jefe", "what do ya want for nothing?"))
    // A key longer than a block is hashed first.
    io.log(hash.hmac(:sha256, repeat("\xaa", 131), "Test Using Larger Than Block-Size Key - Hash Key First"))

    val h = hash.hasher(:sha256)
    hash.update(h, "ab")
    hash.update(h, "")
    hash.update(h, "c")
    io.log(hash.hex(h))
    io.log(hash.digest(:md5, "abc").len)
    io.log(encoding.hex(hash.digest(:sha1, "abc")) == hash.sha1("abc"))
    io.log(hash.size(:sha256))
    io.log(hash.equal("abc", "abc"))
    io.log(hash.equal("abc", "abd"))
    io.log(hash.equal("abc", "ab"))

    val file = path.join(try io.tempDir(), "eclang-std-hash.txt")
    try io.writeFile(file, repeat("x", 200000))
    io.log(try hash.file(file))
    io.log(try hash.file(file, :md5))
    try io.remove(file)
    try hash.file(file) catch problem do
        io.log(problem.code == ERR_NOT_FOUND)
    ret 0
