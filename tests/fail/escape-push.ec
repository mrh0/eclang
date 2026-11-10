// error: 'push' keeps 'value' in what 'l' points to, but here 'value' points to memory from the pool of the `pool` block
use "core:io.ec"
use "core:structures.ec"

fn main() do
    val l: List<String> = list()
    for i in 0..<3 do
        pool do
            l.push("${i}")
