// `own p: Pool`: the function takes the pool over, so it may free it without `unsafe`, and the caller gives it up.

use "core:io.ec"

rec Holder as
    pool: Pool
    count: Int

fn drop(own p: Pool) do
    destroy p

fn drain(own p: Pool, label: String) do
    io.log("draining ${label}")
    clearPool(p)
    drop(p)

fn main(): Int do
    val p = new Pool
    val s = format(p, "in p ${1}")
    io.log(s)
    drop(p)

    var h = Holder { new Pool, 0 }
    val t = format(h.pool, "in h ${2}")
    io.log(t)
    drain(h.pool, "h")
    ret 0
