use "core:io.ec"

// A String's length counts UTF-8 bytes after escapes are decoded.
fn main(): Int do
    io.log("x\ty".len)
    io.log("hé".len)
    io.log("say \"hi\"")
    io.log("say \"hi\"".len)
    io.log("back\\slash \x41 ?? done"c)
    io.log("ünïcödé ✓")
    io.log("ünïcödé ✓".len)
    io.log("".len)
    ret 0
