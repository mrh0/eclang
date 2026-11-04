use "core:io.ec"

rec DigitError as
    code: Int
    message: CString

fn parseDigit(c: Char): Int throws DigitError do
    throw DigitError { 1, "not a digit"c } if c < '0' or c > '9'
    ret c - '0'

// `try` passes the error on to the caller.
fn twoDigits(a: Char, b: Char): Int throws DigitError do
    val high = try parseDigit(a)
    val low = try parseDigit(b)
    ret high * 10 + low

fn cleanup(): Int throws DigitError do
    defer io.log("cleanup runs on every exit"c)
    val n = try parseDigit('x')
    ret n

fn check(v: Int) throws Error do
    throw Error { 2, "negative" } if v < 0

fn main(): Int do
    io.log(twoDigits('4', '2') ?? -1)
    io.log(twoDigits('4', 'x') ?? -1)
    try twoDigits('?', '1') catch e do
        io.log(e.message)
    try cleanup() catch e do
        io.log(e.code)
    try check(-5) catch e do
        io.log(e.message)
    try check(5) catch e do
        io.log("not printed"c)
    ret 0
