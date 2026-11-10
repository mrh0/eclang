// error: holds a tag next to its value
declare fn fill extern "memset" as (p: @Long, c: Int, n: Size): @Any

fn main() do
    var x: Long? = 1
    fill(addrof x!, 0, 8)
