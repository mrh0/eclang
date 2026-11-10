use "core:io.ec"

// What `break` runs only frees what it frees: these must stay allowed after the loop.

fn main() do
    val p = new Pool
    var s: String = "a"
    for i in 0..<3 do
        defer clearPool(p)
        val inner = format(p, "x${i}")
        io.log(inner)
        break
    s = format(p, "again")
    io.log(s)
    val q = new Pool
    var t: String = "b"
    for i in 0..<3 do
        t = format(q, "y${i}")
        if i == 1 do
            break
    io.log(t)
    destroy q
