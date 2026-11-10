use "core:io.ec"
use "core:array.ec"

rec Node as
    value: Int

/** Storage that is written in full before it is read: the caller says so with `unsafe`. */
fn reversed(values: <T>[]): <T>[] do
    val out = unsafe new (<T>)[values.len]
    for i, v in values do
        out[values.len - 1 - i] = v
    ret out

/** The same as a block. */
fn pairUp(a: <T>, b: <T>): <T>[] do
    var out: <T>[]
    unsafe do
        out = new (<T>)[2]
    out[0] = a
    out[1] = b
    ret out

/** Nullable elements need no `unsafe`: they start out null. */
fn slots(n: Size, first: <T>): (<T>?)[] do
    val out = new (<T>?)[n]
    out[0] = first
    ret out

fn main(): Int do
    val r = reversed([1, 2, 3])
    io.log("${r[0]} ${r[1]} ${r[2]}")
    val a = new Node { 1 }
    val b = new Node { 2 }
    io.log("${reversed([a, b])[0].value}")
    val xy = pairUp("x", "y")
    io.log("${xy[0]}${xy[1]}")
    val sevens = filled(3, 7)
    io.log("${sevens.len} ${sevens[0]} ${sevens[2]}")
    val nodes = filled(2, a)                 // pointers: `new (@Node)[2]` would have no empty value
    io.log(nodes[1].value)
    val s = slots(3, a)
    io.log(s[0] != null)
    io.log(s[2] == null)
    ret 0
