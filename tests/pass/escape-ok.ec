// What a function keeps of its arguments is checked at each call; a copy in a pool that outlives the block, or a
// list made in the block, is fine, and `unsafe` takes the programmer's word.

use "core:io.ec"
use "core:structures.ec"

fn add(l: List<String>, s: String) do
    l.push(s)
fn main() do
    val l: List<String> = list()
    val outer = pool
    pool do
        val inner: List<String> = list()
        inner.push("${2}")
        l.push(clone("${1}", outer))
        add(l, clone("${3}", outer))
        add(inner, "${4}")
        unsafe do
            l.push("${5}")
    l.push("${6}")
    io.log(l.len)
