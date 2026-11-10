// In an assignment the value runs first, then the target is read (calls in the target aside), and the
// default of a record field runs its arguments left to right.

use "core:io.ec"

var log = ""
var i = 0
var counter = 0

fn note(s: String): Int do
    log = "${log}${s}"
    ret 1

fn add(a: Int, b: Int): Int = a * 10 + b

fn first(): Int do
    counter += 1
    ret counter

rec R as
    x: Int = add(first(), first())

mut rec P as
    v: Int

var g: @P = new P { 0 }

fn bump(): Int do
    i = 2
    ret 7

fn swap(): Int do
    g = new P { 100 }
    ret 5

fn main() do
    val a = new Int[4]
    a[i] = bump()
    io.log("${a[0]} ${a[2]}")
    val old = g
    g.v = swap()
    io.log("${old.v} ${g.v}")
    i = 0
    a[i] += bump()
    io.log("${a[0]} ${a[2]}")
    val r = R {}
    io.log("${r.x}")
