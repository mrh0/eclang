use "core:io.ec"
use "core:thread.ec"
use "core:time.ec"

var finished = thread.Atomic { 0 }

fn briefly() do
    time.sleep(time.millis(20))
    val x = new 1
    thread.add(mut finished, (@x) as UInt)

// Still allocating when the program ends.
fn forever() do
    while true do
        val x = new 1

fn work(n: @Int) do
    @n = 5

fn main(): Int throws Error do
    // The pool the thread was started in ends before the thread does.
    pool do
        val t = try thread.spawn(fn briefly)
        try thread.detach(t)
    time.sleep(time.millis(100))
    io.log(thread.load(mut finished))

    val n = new 0
    val t = try thread.spawn(fn work, n)
    try thread.join(t)
    io.log(@n)
    // A thread can only be joined or detached once.
    try thread.join(t) catch e do
        io.log("second join: ${e.message}")
    try thread.detach(t) catch e do
        io.log("detach after join: ${e.message}")

    val u = try thread.spawn(fn briefly)
    try thread.detach(u)
    try thread.join(u) catch e do
        io.log("join after detach: ${e.message}")

    try thread.detach(try thread.spawn(fn forever))
    ret 0
