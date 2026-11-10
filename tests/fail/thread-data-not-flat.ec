// error: in what 'data' points to (a Job), its field 'names' is a List<String>, a pointer to a record that isn't `sync`
use "core:thread.ec"
use "core:structures.ec"

// A List isn't `sync`: two threads changing one would race, and its memory is this thread's to free.
rec Job as
    id: Int
    names: List<String>

fn work(job: @Job) do
    job.names.push("worker")

fn main(): Int throws Error do
    val job = new Job { 1, list() }
    val t = try thread.spawn(fn work, job)
    try thread.join(t)
    ret 0
