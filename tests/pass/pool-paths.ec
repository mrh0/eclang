// What is known about pools follows the paths through an `if`: a variable given a new value on the path that
// cleared its pool is still usable after it.
use "core:io.ec"

fn refresh(c: Bool): String do
    val p = new Pool
    var s = format(p, "old")
    if c do
        clearPool(p)
        s = format(p, "new")
    ret clone(s)

fn bothWays(c: Bool): String do
    val p = new Pool
    var s = format(p, "old")
    clearPool(p)
    if c do
        s = format(p, "yes")
    else do
        s = format(p, "no")
    ret clone(s)

fn elseOnly(c: Bool): String do
    val p = new Pool
    var s = format(p, "kept")
    if c do
        io.log("nothing cleared")
    else do
        clearPool(p)
        s = format(p, "renewed")
    ret clone(s)

fn chain(n: Int): String do
    val p = new Pool
    var s = format(p, "zero")
    if n == 1 do
        clearPool(p)
        s = format(p, "one")
    else if n == 2 do
        clearPool(p)
        s = format(p, "two")
    ret clone(s)

fn looped(rounds: Int): String do
    val p = new Pool
    var s = format(p, "start")
    for i in 0..<rounds do
        io.log(s)
        if i % 2 == 1 do
            clearPool(p)
            s = format(p, "round ${i}")
    ret clone(s)

fn main() do
    io.log(refresh(true))
    io.log(refresh(false))
    io.log(bothWays(true))
    io.log(bothWays(false))
    io.log(elseOnly(true))
    io.log(elseOnly(false))
    io.log(chain(0))
    io.log(chain(1))
    io.log(chain(2))
    io.log(looped(4))
