use "core:io.ec"

fn check(ok: Bool) throws String do
    throw "bad" if !ok

fn value(ok: Bool): Int throws String do
    throw "bad" if !ok
    ret 5

fn find(ok: Bool): @Int? do
    ret new 1 if ok
    ret null

fn note(s: String) do
    io.log(s)

fn leaves(): Int do
    check(false) ?? ret 1
    ret 2

// After a jump, the variable isn't null.
fn narrowed(x: @Int?): Int do
    x ?? ret 0
    ret @x

fn panics(x: @Int?): Int do
    x ?? panic("none")
    ret @x

fn rethrows() throws String do
    check(false) ?? throw "wrapped"

fn loops() do
    for i in 0..<3 do
        check(i != 1) ?? break
        io.log("round ${i}")
    for i in 0..<3 do
        check(i != 1) ?? continue
        io.log("again ${i}")

fn deferred() do
    defer check(false) ?? note("deferred")
    note("body")

fn main() do
    check(true) ?? panic("not reached")
    check(false) ?? note("failed")
    value(true) ?? note("not reached")
    value(false) ?? note("no value")
    find(false) ?? note("nothing")
    find(true) ?? note("not reached")
    find(false) ?? find(true) ?? note("not reached")
    find(false) ?? find(false) ?? note("neither")
    io.log("${leaves()}")
    io.log("${narrowed(null)}")
    io.log("${narrowed(new 3)}")
    io.log("${panics(new 4)}")
    rethrows() ?? note("rethrown")
    loops()
    deferred()
