// error: pool
use "core:structures.ec"
fn main() do
    var kept = ""
    pool do
        val words: Map<String, Int> = map()
        for word, n in words do
            kept = word
