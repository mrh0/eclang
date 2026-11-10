// `caller` as a default gives a parameter the place of the call; `here` is the place where it is written.
use "core:io.ec"
use "core:errors.ec"
use "core:log.ec"

fn check(ok: Bool, what: String = "", at: Location = caller) do
    io.log("${what} ${ok} at ${at} ${at.column} ${at.function}")

fn line(at: Int = caller): Int = at
fn spot(s: String = caller): String = s
fn twice(a: <T>, b: <T>, at: Location = caller) do
    io.log("${a} ${b} ${at}")

fn main() do
    check(true)
    check(false, "x")
    check(false, "y", here)
    check(ok = true, what = "named")
    io.log(line())
    io.log(spot())
    twice(1, 2)
    twice("a", "b", here)
    val h = here
    io.log("${h.line}")
    io.log("${h} ${h.function}")
    relay()
    log.setTimestamps(:none)
    log.setLocations(true)
    log.info("located")
    log.setLocations(false)
    log.info("plain")

/** A helper passes its own caller's place on. */
fn relay(at: Location = caller) do
    check(true, "relayed", at)
