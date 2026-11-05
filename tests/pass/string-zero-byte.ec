use "core:io.ec"

fn main() do
    val x = 7
    val s = "a\0b${x}\0"
    io.log(s.len)
    io.log(s[1] as Int)
    io.log(s[2])
    io.log(s[3])
    io.log(s[4] as Int)
    val p = "100%\0${x}%"
    io.log(p.len)
