use "core:io.ec"

fn main() do
    val a = 0xFFFFFFFFFFFFFFFFul
    io.log("${a}")
    val b: ULong = 0x8000000000000000
    io.log("${b}")
    val c = 0b1000000000000000000000000000000000000000000000000000000000000001ul
    io.log("${c}")
    io.log("${a == ~(0 as ULong)}")
    io.log("${0x7FFFFFFFFFFFFFFF}")
