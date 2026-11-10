// `keep(x)` copies x into the pool the function was called with, which outlives its `pool do` blocks: what a block makes
// can be kept after it in one word.

use "core:io.ec"
use "core:structures.ec"

rec Named as
    name: String
    tags: String[]

fn label(i: Int): String = "item-${i * 2}"

fn collect(n: Int): List<String> do
    val names: List<String> = list()
    for i in 0..<n do
        pool do
            val tmp = label(i)
            names.push(keep(tmp))
    ret names

fn named(): Named do
    pool do
        pool do
            ret keep(Named { "deep ${1}", ["a ${2}", "b ${3}"] })

fn main(): Int do
    for s in collect(3).items() do
        io.log(s)
    val n = named()
    io.log("${n.name} ${n.tags[0]} ${n.tags[1]}")
    var last = "start"
    pool do
        last = "in a block ${4}".keep()
    io.log(last)
    io.log(keep("outside ${5}"))
    ret 0
