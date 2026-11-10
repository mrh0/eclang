// error: 'spawn' keeps 'data' in memory that outlives the call (its parameter is `escaping`
use "core:thread.ec"

fn work(n: @Int) do
    @n = 5

fn main(): Int throws Error do
    var n = 0
    val t = try thread.spawn(fn work, addrof(n))
    try thread.join(t)
    ret n
