use "core:io.ec"

// Fields with a default value can be left out of a record literal, by name or at the end.

const START = 3

rec Config as
    name: CString
    retries: Int = START
    verbose: Bool = false

rec Big extends Config as
    level: Int = 9

fn main(): Int do
    val a = Config { name = "a"c }
    val b = Config { "b"c, 5 }
    val c = Big { name = "c"c, verbose = true }
    io.log(a.retries)
    io.log(b.retries)
    io.log(b.verbose)
    io.log(c.retries)
    io.log(c.level)
    io.log(c.verbose)
    ret 0
