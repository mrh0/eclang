// error: 'spawn' keeps 'body' in memory that outlives the call
use "core:thread.ec"
fn main() do
    var hits = 0
    val t = thread.spawn(fn () = hits + 1) ?? panic("can't start a thread")
    thread.join(t) ?? panic("can't join")
