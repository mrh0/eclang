// error: 'send' may give 'item' to another thread (it is `flat`), so it must reach no memory but `sync` records, but 'item' is a String, which points to its text
use "core:thread.ec"

// A Queue copies its items, but a String's text isn't copied with it: the thread taking it would point into this one's memory.
fn main(): Int throws Error do
    val q: thread.Queue<String> = try thread.queue(4)
    try thread.send(q, "hello ${1}")
    ret 0
