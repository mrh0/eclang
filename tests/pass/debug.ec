// `debug` statements and `debug do` blocks only run in a debug build; `--release` leaves them out (see debug.release.out).
use "core:io.ec"

var checks = 0

fn check(): Bool do
    checks += 1
    ret true

fn main(): Int do
    io.log("start")
    debug io.log("debug build")
    debug do
        val n = 2
        io.log("in a debug block: ${n}")
    debug io.log("tail if") if checks == 0
    debug assert check()
    // Deferred in a debug build only.
    debug defer io.log("deferred, debug only")
    defer io.log("deferred")
    debug pool do
        io.log("pool block: ${1 + 1}")
    var total = 0
    for i in 0..<4 do
        debug continue if i == 1
        debug total += i
    io.log("total ${total}")
    io.log("checks ${checks}")
    // `debug` is still a name elsewhere: a log level, a field.
    val debug = checks > 5
    io.log(debug)
    ret 0
