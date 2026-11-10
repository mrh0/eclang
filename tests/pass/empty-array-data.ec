use "core:io.ec"
use "core:c/string.ec"

rec Holder as
    items: Int[]
    name: String

fn main(): Int do
    var empty: Int[]
    var h: Holder
    // An empty array may have no memory, but `.data` is never null: C functions can be given it with a length of 0.
    io.log((empty.data !as Size) != 0)
    io.log((h.items.data !as Size) != 0)
    io.log((h.name.data !as Size) != 0)
    val dst = new Int[3]
    memcpy(dst.data, empty.data, 0)
    io.log(dst.len)
    val full = [1, 2, 3]
    io.log(unsafe full.data[1])
    io.log(unsafe "abc".data[2])
    ret 0
