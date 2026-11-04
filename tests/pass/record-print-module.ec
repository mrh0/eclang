use "core:io.ec"
use "lib/printed.ec"

rec Holder as
    purse: printed.Purse

// The top level can give a module's record a `str`, too.
rec Shade as
    level: Int

rec Tint extends Shade as
    alpha: Int

/** A child record uses its parent's `str`. */
fn str(s: Shade): String = "shade ${s.level}"

/** A field of each remaining kind. */
rec Mixed as
    either: Int | String
    state: :on | :off
    maybe: :a | :b | Null
    action: (Int) => Int
    words: String[]
    nested: Int[][]
    empty: Int[]
    held: @Int
    other: (Point2, Double)

rec Point2 as
    x: Float

rec Nothing as
    gone: Int?

fn twice(x: Int): Int = x * 2

fn main(): Int do
    val coin = printed.Coin { 3 }
    io.log("${coin}")
    val purse = printed.Purse { coin }
    io.log("${purse}")
    io.log(printed.describe(purse))
    val holder = Holder { purse }
    io.log("${holder}")

    val tint = Tint { 1, 2 }
    io.log("${tint}")

    val rows = clone([clone([1, 2]), clone([3])])
    val mixed = Mixed { "text", :on, null, fn twice, clone(["a", "b"]), rows, new Int[0], new 5, (Point2 { 1.5 }, 2.5) }
    io.log("${mixed}")
    val other = Nothing { null }
    io.log("${other}")

    // The result is built in the pool asked for, whichever pool a `str` allocates in.
    val p = newPool()
    val kept = format(p, "${coin}!")
    pool do
        val inner = format(p, "${purse}")
        io.log(inner)
    io.log(kept)
    freePool(p)
    ret 0
