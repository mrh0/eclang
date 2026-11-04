use "core:io.ec"

rec Point as
    x: Int
    y: Int

// `as` to a wider number type is a C cast too: C wouldn't widen these by itself.
fn main(): Int do
    val a = 7
    val b = 2
    io.log((a as Double) / b)
    val big = 2000000000
    io.log((big as Long) * big)
    val u: UInt = 1
    io.log((u as ULong) << 40)
    io.log((1 as Long) << 40)
    printf("%ld\n"c, a as Long)
    val smallest: Long = -9223372036854775807 - 1
    io.log(smallest)
    val smallestInt: Int = -2147483647 - 1
    io.log(smallestInt)

    // `!as` between a String and a Char[] or UChar[] shares the bytes.
    val buffer = new Char[3]
    buffer[0] = 'a'
    buffer[1] = 'b'
    buffer[2] = 'c'
    val s = buffer !as String
    io.log(s)
    val bytes = "hi" !as UChar[]
    io.log(bytes[0])
    val p = Point { 1, 2 }
    val q = (addrof p) !as @Int
    io.log(@q)
    ret 0
