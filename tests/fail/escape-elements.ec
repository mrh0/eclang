// error: 'pushAll' keeps 'values' (or its elements) in what 'l' points to
use "core:io.ec"
use "core:structures.ec"

fn main() do
    val l: List<String> = list()
    val n: List<Int> = list()
    n.pushAll([1, 2, 3])
    pool do
        l.pushAll(["${1}"])
