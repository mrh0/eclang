// error: pool
use "core:structures.ec"
fn main() do
    var kept = ""
    pool do
        val words = listOf("${1}")
        for word in words do
            kept = word
