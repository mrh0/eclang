use "core:io.ec"

// `first` calls a function that reads a global written after it, so that global is set first.
val first: Int = readLater()
val later: @Int = new 41

fn readLater(): Int = @later + 1

// Through two functions and a lambda, along a chain of globals, some of which are only needed by a function.
val a: Int = viaLambda()
val b: Int = readC() + 1
val c: Int = readD() + 1
val d: Int = plusOne(1)

fn plusOne(x: Int): Int = x + 1
fn readC(): Int = c
fn readD(): Int = d
fn viaLambda(): Int = helper(fn (x: Int) = x + b)
fn helper(f: (Int) => Int): Int = f(100)

// Globals that don't depend on each other keep the order they are written in.
fn say(s: String): Int do
    io.log(s)
    ret 0
val x: Int = say("x")
val y: Int = say("y")
val z: Int = say("z")

// A function that writes a global runs after that global is set, or the value would be lost.
var counter: @Int = new 0
val bumped: Int = bump()
fn bump(): Int do
    @counter = 7
    ret 1

fn main(): Int do
    io.log(first)
    io.log(a)
    io.log(@counter)
    ret 0
