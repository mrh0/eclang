// error: 'start' keeps 'job' in memory that outlives the call
use "core:thread.ec"

fn work(n: @Int) do
    @n = 5

/** Keeps `job` through thread.spawn, so its callers are checked as spawn's are. */
fn start(job: @Int): thread.Thread throws Error = try thread.spawn(fn work, job)

fn main(): Int throws Error do
    var n = 0
    val t = try start(addrof n)
    try thread.join(t)
    ret n
