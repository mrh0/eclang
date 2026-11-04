use "core:io.ec"
use "core:errors.ec"

// core:errors.ec: error types that extend Error, so `try` passes them on as an Error; APR statuses as errors.
fn digit(text: String, at: Size): Int throws ParseError do
    val c = text[at]
    throw parseError("expected a digit", text, at) if c < '0' or c > '9'
    ret (c - '0') as Int

fn checked(n: Int): Int throws RangeError do
    throw rangeError("${n} is more than 9") if n > 9
    ret n

// Both kinds of error are passed on as an Error.
fn sum(text: String): Int throws Error do
    val a = try digit(text, 0)
    val b = try digit(text, 1)
    ret try checked(a + b)

fn find(key: String): Int throws NotFoundError do
    throw notFoundError(key) if key != "answer"
    ret 42

fn main(): Int do
    io.log(sum("12") ?? -1)
    try sum("1x") catch e do
        io.log(e)
    try sum("99") catch e do
        io.log(e)
        io.log(e.code == ERR_RANGE)
    try digit("ab", 1) catch e do
        io.log(e.position)
        io.log(e.input)
    try find("question") catch e do
        io.log(e.name)
        io.log(describe(e))
    io.log(find("answer") ?? 0)

    val os = osError(APR_ENOENT, "open config.txt")
    io.log(os)
    io.log(os.status == APR_ENOENT)
    val ioe = ioError(APR_EACCES, "/etc/shadow")
    io.log(ioe.message)
    io.log(errorKind(ioe.code))
    io.log(errorKind(ioError(APR_EOF, "data.bin").code))
    try checkStatus(APR_EEXIST, "make the directory"c) catch e do
        io.log(e)
    try checkStatus(APR_SUCCESS, "nothing"c) catch e do
        io.log("not printed")
    io.log(argumentError("count", "must be positive").argument)
    io.log(stateError("closed").code == ERR_STATE)
    io.log(timeoutError("too slow"))
    ret 0
