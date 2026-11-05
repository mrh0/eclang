use "core:io.ec"
use "core:os.ec"
use "core:time.ec"
use "core:net.ec"
use "core:term.ec"

// Names that C headers these modules include declare: unistd.h, time.h, termios.h, netinet/in.h. They get other C names.

val read = 1
val sleep = 2
val pipe: Int = 3
val alarm = 4
val INT_MAX = 7
val NCCS = 11

fn sum(close: Int, getpid: Int): Int do
    val gmtime = close + getpid
    ret gmtime

fn main() do
    val socket = 8
    io.log("${read + sleep + pipe + alarm + NCCS + INT_MAX + socket}")
    io.log("${sum(1, 2)}")
