// error: 'submit' may give 'job' to another thread (it is `flat`), so it must reach no memory but `sync` records, but the lambda captures 'name'
use "core:io.ec"
use "core:thread.ec"

fn main(): Int throws Error do
    val workers = try thread.workerPool(2)
    val name = "job ${1}"
    try thread.submit(workers, fn () = io.log(name))
    try thread.stop(workers)
    ret 0
