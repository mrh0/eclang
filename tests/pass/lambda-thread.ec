use "core:io.ec"
use "core:thread.ec"

// A thread can run a lambda that uses the `val`s around it: they are copied into the closure. What it captures must be `flat`
// (a String points to text that this thread could free), so the text is a global, which outlives every thread.
val greeting = "hello from a thread"

fn main() do
    val times = 2
    val t = thread.spawn(fn () = io.log("${greeting} x${times}")) ?? panic("can't start a thread")
    thread.join(t) ?? panic("can't join")
    // Where a function returning nothing is expected, a lambda's value is dropped.
    val u = thread.spawn(fn () = times + 1) ?? panic("can't start a thread")
    thread.join(u) ?? panic("can't join")
