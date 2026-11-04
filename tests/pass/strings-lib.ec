use "core:io.ec"
use "core:string.ec"

// The basic String functions of core:string.ec, `cstr`, and `new (T)[n]`.

rec Point as
    x: Int

fn main(): Int do
    val s = "  hello, world  "
    io.log(trim(s))
    io.log(startsWith(trim(s), "hello"))
    io.log(endsWith(s, "x"))
    io.log(indexOf(s, "world") ?? 99)
    io.log(contains(s, "lo, w"))
    for part in split("a,b,,c", ',') do
        io.log("[${part}]")
    io.log(parseInt(" -42 ") ?? 0)
    io.log(parseInt("12x") ?? -1)
    try parseInt("99999999999999999999") catch e do
        io.log(e.message)
        io.log(e.code == ERR_RANGE)
    try parseInt("12x") catch e do
        io.log(e.code == ERR_PARSE)
    try parseInt("") catch e do
        io.log(e.code == ERR_PARSE)
    try parseInt("-") catch e do
        io.log(e.code == ERR_PARSE)
    printf("[%s]\n"c, cstr(trim(s)[0..<5]))
    val points = new (@Point?)[2]
    points[0] = new Point { x = 4 }
    io.log(points[0]?.x ?? -1)
    io.log(points[1]?.x ?? -1)
    ret 0
