// panic: the index is past the end of the List
use "core:io.ec"
use "core:structures.ec"

fn main(): Int do
    val l = listOf(1, 2)
    io.log(l.get(2))
    ret 0
