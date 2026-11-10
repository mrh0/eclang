use "core:io.ec"
use "core:c/string.ec"

rec Named as
    name: Char[8]
    id: Int

fn label(n: @Named): CString = cstr(n.name)

fn main(): Int do
    val full: Char[3] = ['a', 'b', 'c']
    io.log(cstr(full))                  // all 3 Chars: no 0 to stop at
    io.log(strlen(cstr(full)))
    val short: Char[5] = ['h', 'i', 0 as Char, 'x', 'y']
    io.log(cstr(short))                 // up to the first 0
    var n = Named { ['e', 'c', 'l', 'a', 'n', 'g', 0 as Char, 0 as Char], 7 }
    io.log(cstr(n.name))
    io.log(label(addrof n))
    pool p do
        io.log(cstr(p, n.name))
    val s = "a slice"
    io.log(cstr(s[2..]))
    ret 0
