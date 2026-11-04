use "core:lib.ec"

val READ = 1 << 0
val WRITE = 1 << 1
val EXEC = 1 << 2

fn has(flags: Int, flag: Int): Bool = flags & flag != 0

fn main(): Int do
    val flags = READ | EXEC
    log(flags)
    log(has(flags, WRITE))
    log(has(flags, EXEC))
    log(flags ^ READ)
    log(~0)
    log(256 >> 4)

    // Shifts keep the type of the left side.
    val c: UChar = 200
    log(c << 1)
    log(~c)
    val big: Long = 1 << 40
    log(big)
    var x: Long = 3
    log(x << 33)
    ret 0
