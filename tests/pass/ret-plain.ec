use "core:io.ec"
use "core:errors.ec"

// A bare `ret` leaves a function that returns nothing, after its deferred statements.
fn guard(n: Int) do
    defer io.log("left ${n}")
    ret if n < 0
    if n == 0 do
        ret
    io.log("positive")

fn fails(n: Int) throws Error do
    ret if n == 0
    throw Error { 1, "bad" } if n == 1
    io.log("went on")

// `ret if ...` is still the start of an inline `if` when `do` follows.
fn pick(c: Bool): Int do
    ret if c do 1 else 2

fn main() do
    guard(-1)
    guard(0)
    guard(1)
    try fails(0) catch e do
        io.log("failed")
    try fails(1) catch e do
        io.log("failed")
    try fails(2) catch e do
        io.log("failed")
    io.log(pick(true))
    io.log(pick(false))
