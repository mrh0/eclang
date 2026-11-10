// error: because it may be null
use "core:structures.ec"
fn main() do
    val l: List<Int>? = null
    for x in l do
        pass
