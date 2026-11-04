use "core:lib.ec"

rec ParseError as
    code: Int
    message: CString

fn parseDigit(c: Char): Int throws ParseError do
    throw ParseError { 1, "not a digit"c } if c < '0' or c > '9'
    ret c - '0'

// `try` passes the error on to the caller.
fn twoDigits(a: Char, b: Char): Int throws ParseError do
    val high = try parseDigit(a)
    val low = try parseDigit(b)
    ret high * 10 + low

fn cleanup(): Int throws ParseError do
    defer log("cleanup runs on every exit"c)
    val n = try parseDigit('x')
    ret n

fn check(v: Int) throws Error do
    throw Error { 2, "negative" } if v < 0

fn main(): Int do
    log(twoDigits('4', '2') ?? -1)
    log(twoDigits('4', 'x') ?? -1)
    try twoDigits('?', '1') catch e do
        log(e.message)
    try cleanup() catch e do
        log(e.code)
    try check(-5) catch e do
        log(e.message)
    try check(5) catch e do
        log("not printed"c)
    ret 0
