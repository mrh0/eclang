// Programs the lifetime checks must keep accepting.
use "core:io.ec"

rec P as
    x: Int

rec H as
    p: Pool

rec Named as
    name: String
    tags: String[] = ["a", "b"]

var g = 7
var kept = "none"

fn fails(): Int throws String do
    throw "failed"

fn works(): Int throws String = 1

// An element of a stack array isn't in the array.
fn firstName(): String do
    val names = ["ann", "bob"]
    for n in names do
        ret n
    ret ""

fn firstRest(...r: String): String = r[0]

fn lastRest(...r: String): String do
    var last = ""
    for s in r do
        last = s
    ret last

fn sumRest(...r: Int): Int do
    var total = 0
    for n in r do
        total += n
    ret total

fn defaulted(a: Int[] = [1, 2, 3]): Int = a[1]

fn tag(): String do
    val n = Named { name = "n" }
    ret n.tags[1]

// `try` leaves through the deferred statements without freeing anything for the code after it.
fn deferredFree(): Int throws String do
    val p = newPool()
    defer freePool(p)
    val s = format(p, "x${1}")
    val v = try works()
    io.log(s)
    ret v + s.len as Int

// The error of a call given p is copied before the deferred `freePool` frees p.
fn make(p: Pool): Int throws String do
    throw format(p, "made in ${1}")

fn deferredCopy(): Int throws String do
    val p = newPool()
    defer freePool(p)
    val v = try make(p)
    ret v

fn copied(): String do
    val p = newPool()
    defer freePool(p)
    val s = format(p, "copy ${1}")
    ret clone(s)

// A variable defined in the loop starts over in every round.
fn loopLocal(): @Int do
    var y = 1
    for i in 0..<3 do
        var cur: @Int = addrof g
        if i == 5 do
            ret cur
        cur = addrof y
        @cur = i
    ret addrof g

// A pointer assigned in a loop, but never returned.
fn carried(): Int do
    var x = 1
    var y = 2
    var p: @Int = addrof x
    for i in 0..<3 do
        y += @p
        p = addrof y
    ret @p

fn poolLoop(): Int do
    val p = newPool()
    var s = ""
    var total = 0
    for i in 0..<3 do
        total += s.len as Int
        clearPool(p)
        s = "fresh"
        val t = format(p, "x${i}")
        total += t.len as Int
    freePool(p)
    ret total

// Each round makes the pool it frees.
fn poolPerRound(): Int do
    var total = 0
    for i in 0..<3 do
        val p = newPool()
        total += format(p, "${i}").len as Int
        freePool(p)
    var q = newPool()
    for i in 0..<3 do
        total += format(q, "${i}").len as Int
        freePool(q)
        q = newPool()
    freePool(q)
    ret total

fn blockClear(): Int do
    var total = 0
    pool do
        val s = "x${1}"
        total += s.len as Int
        clearPool(pool)
        val t = "yy${2}"
        total += t.len as Int
    ret total

fn poolFields(): Int do
    val h = H { p = newPool() }
    val s = format(h.p, "ab${1}")
    val n = s.len as Int
    freePool(h.p)
    ret n

fn parentAndChild(): Int do
    val p = newPool()
    val c = newPool(p)
    val s = format(c, "abc")
    val n = s.len as Int
    clearPool(c)
    val t = format(p, "z${1}")
    freePool(p)
    ret n

fn globalReset(): Int do
    val p = newPool()
    kept = format(p, "x${1}")
    val n = kept.len as Int
    kept = "none"
    freePool(p)
    ret n

fn throughPointer(): Int do
    val p = newPool()
    var s = format(p, "a${1}")
    val ps = addrof s
    val n = (@ps).len as Int
    freePool(p)
    ret n

fn catchMessage(): String do
    var saved = "none"
    try fails() catch e do
        saved = e
    ret saved

fn pointerToLiteral(): Int do
    val q = addrof P { x = 5 }
    ret q.x

fn main(): Int do
    io.log(firstName())
    io.log(firstRest("r1", "r2"))
    io.log(lastRest("r1", "r2"))
    io.log(sumRest(1, 2, 3))
    io.log(defaulted())
    io.log(tag())
    io.log(deferredFree() ?? -1)
    try deferredCopy() catch e do
        io.log(e)
    io.log(copied())
    io.log(@loopLocal())
    io.log(carried())
    io.log(poolLoop())
    io.log(poolPerRound())
    io.log(blockClear())
    io.log(poolFields())
    io.log(parentAndChild())
    io.log(globalReset())
    io.log(throughPointer())
    io.log(catchMessage())
    io.log(pointerToLiteral())
    ret 0
