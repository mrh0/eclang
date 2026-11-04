use "core:lib.ec"

// A String's length counts UTF-8 bytes after escapes are decoded.
fn main(): Int do
    log("x\ty".len)
    log("hé".len)
    log("say \"hi\"")
    log("say \"hi\"".len)
    log("back\\slash \x41 ?? done"c)
    log("ünïcödé ✓")
    log("ünïcödé ✓".len)
    log("".len)
    ret 0
