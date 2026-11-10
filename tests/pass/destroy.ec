use "core:io.ec"

// `new Pool` makes a pool, and `destroy` frees it. A function frees only pools it made, unless it says `unsafe`.

mut rec Holder as
    p: Pool?
    name: String

// A pool of this function's own: destroyed, also under another name or from a field of a local.
fn own() do
    val p = new Pool
    val s = format(p, "own ${1}")
    io.log(s)
    val q = p
    destroy q
    var h = Holder { p = new Pool, name = "h" }
    destroy h.p
    io.log("h.p is null: ${h.p == null}")

// A `Pool?` that may be null: null is skipped, and the place is set to null, so destroying it again does nothing.
fn nullable(make: Bool) do
    var p: Pool? = if make do new Pool else null
    destroy p
    destroy p
    io.log("p is null: ${p == null}")

// A child pool, freed with its parent.
fn child() do
    val parent = new Pool
    val c = new Pool in parent
    io.log(format(c, "child ${2}"))
    destroy parent

// `defer destroy`, and a pool made in a loop.
fn deferred(): Int do
    val p = new Pool
    defer destroy p
    var total = 0
    for i in 0..<3 do
        val round = new Pool in p
        total += format(round, "${i}").len as Int
        destroy round
    ret total

// A pool this function didn't make: the caller says it is done with it.
fn drop(p: Pool) do
    unsafe destroy p

// The same for a field the caller hands over: `unsafe` covers the one statement.
fn release(h: @Holder) do
    unsafe destroy h.p

// `unsafe` around `defer` covers the deferred statement too.
fn dropLater(p: Pool): Int do
    unsafe defer destroy p
    ret format(p, "later").len as Int

fn main(): Int do
    own()
    nullable(true)
    nullable(false)
    child()
    io.log("deferred: ${deferred()}")
    drop(new Pool)
    io.log("later: ${dropLater(new Pool)}")
    var h = Holder { p = new Pool, name = "held" }
    release(addrof h)
    io.log("released: ${h.p == null}")
    pool do
        io.log(format("block"))
        clearPool(pool)
        io.log(format("cleared"))
    ret 0
