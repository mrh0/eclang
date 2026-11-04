use "core:io.ec"

val READ = 1 << 0
val WRITE = 1 << 1
val EXEC = 1 << 2

fn has(flags: Int, flag: Int): Bool = flags & flag != 0

fn main(): Int do
    val flags = READ | EXEC
    io.log(flags)
    io.log(has(flags, WRITE))
    io.log(has(flags, EXEC))
    io.log(flags ^ READ)
    io.log(~0)
    io.log(256 >> 4)

    // Shifts keep the type of the left side.
    val c: UChar = 200
    io.log(c << 1)
    io.log(~c)
    val big: Long = 1 << 40
    io.log(big)
    var x: Long = 3
    io.log(x << 33)
    ret 0
