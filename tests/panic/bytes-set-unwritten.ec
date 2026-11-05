// panic: bytes.set* can only change what is already written
use "core:bytes.ec"

fn main(): Int do
    val w = bytes.writer()
    bytes.writeU16(w, 1)
    bytes.setU32(w, 0, 5)
    ret 0
