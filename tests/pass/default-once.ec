use "core:io.ec"

var calls: Int = 0

fn next(): Int do
    calls += 1
    ret calls

// A default runs once, however often the body reads the parameter.
fn twice(a: Int = next()): Int = a + a

// Even when the body doesn't read it.
fn ignored(a: Int = next()): Int = 0

fn fails(ok: Bool): Int throws String do
    throw "no" if !ok
    ret 7

// A default can be read through its address, and can use the parameters before it.
fn read(a: Int, b: Int = a + 1): Int do
    val p = addrof b
    ret @p

// `0 > s.len` in C would be always false, which gcc rejects under -Wextra.
fn past(s: String, from: Size = 0): Bool do
    ret true if from > s.len
    ret false

fn numbers(values: Int[] = [1, 2, 3]): Int = values[0] + values[2]

fn safe(n: Int = fails(false) ?? 9): Int = n

fn main() do
    val a = twice()
    io.log("${a} ${calls}")
    val b = ignored()
    io.log("${b} ${calls}")
    val c = twice(5)
    io.log("${c} ${calls}")
    io.log("${read(1)} ${read(1, 5)}")
    val text = "abc"
    io.log("${past(text)} ${past(text, 9)}")
    io.log("${numbers()}")
    io.log("${safe()}")
