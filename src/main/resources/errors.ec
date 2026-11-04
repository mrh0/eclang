/**
 * Common error types, for `throws`. Each one extends {@link Error} (`code: Int`, `message: String`), so a
 * function that `throws Error` can pass any of them on with `try`, and `e.code` says what kind it is.
 *
 *     fn load(path: String): Config throws Error do
 *         val text = try readFile(path)            // throws IoError
 *         ret try parseConfig(text)                // throws ParseError
 *
 * Make them with the functions below (`throw parseError("expected a digit", text, i)`) or as records
 * (`throw RangeError { ERR_RANGE, "too big" }`).
 */
use "core:apr/errno.ec"
use "core:c/stdio.ec"
use "core:c/stdlib.ec"

/** The usual error type for `throws Error`. The other error types here extend it. */
rec Error as
    /** What kind of error it is: one of the `ERR_*` constants. */
    code: Int
    /** What went wrong, for people to read. */
    message: String

// What kind of error it is: the `code` of every error made here.
/** Error code: anything not covered by another `ERR_*` code. */
const ERR_FAILED = 1
/** Error code: text isn't in the format it should be in. */
const ERR_PARSE = 2
/** Error code: a number is too big or too small. */
const ERR_RANGE = 3
/** Error code: an argument isn't valid. */
const ERR_ARGUMENT = 4
/** Error code: a file, key or name doesn't exist. */
const ERR_NOT_FOUND = 5
/** Error code: it already exists. */
const ERR_EXISTS = 6
/** Error code: not allowed. */
const ERR_PERMISSION = 7
/** Error code: not possible now (closed, already started, ...). */
const ERR_STATE = 8
/** Error code: it took too long. */
const ERR_TIMEOUT = 9
/** Error code: not supported on this system. */
const ERR_UNSUPPORTED = 10
/** Error code: reading or writing failed. */
const ERR_IO = 11
/** Error code: out of memory. */
const ERR_MEMORY = 12

/** Text couldn't be parsed. Its code is {@link ERR_PARSE}; make one with {@link parseError}. */
rec ParseError extends Error as
    /** The text that couldn't be parsed. */
    input: String
    /** The byte of {@code input} where the problem is. */
    position: Size

/** A number is out of the range it must be in. Its code is {@link ERR_RANGE}; make one with {@link rangeError}. */
rec RangeError extends Error

/** An argument isn't valid. Its code is {@link ERR_ARGUMENT}; make one with {@link argumentError}. */
rec ArgumentError extends Error as
    /** The name of the argument that isn't valid. */
    argument: String

/** Something doesn't exist. Its code is {@link ERR_NOT_FOUND}; make one with {@link notFoundError}. */
rec NotFoundError extends Error as
    /** The name of what wasn't found: a key, a file, ... */
    name: String

/**
 * The operation isn't possible in the current state (on a closed channel, a finished thread, ...). Its code is
 * {@link ERR_STATE}; make one with {@link stateError}.
 */
rec StateError extends Error

/** Waiting took longer than allowed. Its code is {@link ERR_TIMEOUT}; make one with {@link timeoutError}. */
rec TimeoutError extends Error

/**
 * An operating system or APR call failed. Its code is the kind of the status ({@link statusKind}); make one
 * with {@link osError}.
 */
rec OsError extends Error as
    /** The apr_status_t the call returned (an errno value on Unix). */
    status: Int

/** Reading or writing a file failed. Make one with {@link ioError}. */
rec IoError extends OsError as
    /** The path of the file. */
    path: String

/**
 * A {@link ParseError}: `throw parseError("expected a digit", text, i)`.
 * @param message what is wrong
 * @param input the text that couldn't be parsed
 * @param position the byte of {@code input} where the problem is
 */
fn parseError(message: String, input: String, position: Size): ParseError = ParseError { ERR_PARSE, message, input, position }

/** A {@link RangeError} with the `message`. */
fn rangeError(message: String): RangeError = RangeError { ERR_RANGE, message }

/**
 * An {@link ArgumentError}.
 * @param argument the name of the argument that isn't valid
 * @param message what is wrong with it
 */
fn argumentError(argument: String, message: String): ArgumentError = ArgumentError { ERR_ARGUMENT, message, argument }

/** A {@link NotFoundError} for `name`, with the message "'name' was not found". */
fn notFoundError(name: String): NotFoundError = NotFoundError { ERR_NOT_FOUND, "'${name}' was not found", name }

/** A {@link StateError} with the `message`. */
fn stateError(message: String): StateError = StateError { ERR_STATE, message }

/** A {@link TimeoutError} with the `message`. */
fn timeoutError(message: String): TimeoutError = TimeoutError { ERR_TIMEOUT, message }

/**
 * What an APR status means, in words ("No such file or directory").
 * @param status an apr_status_t, as an APR call returns it
 */
fn statusMessage(status: Int): String do
    val buffer = new Char[256]
    ret format("${apr_strerror(status, buffer.data !as CString, buffer.len)}")

/**
 * The `ERR_*` code for an APR status: {@link ERR_NOT_FOUND} for ENOENT, and so on.
 * @param status an apr_status_t, as an APR call returns it
 * @return the code, {@link ERR_FAILED} when no other fits
 */
fn statusKind(status: Int): Int do
    ret ERR_NOT_FOUND if APR_STATUS_IS_ENOENT(status) != 0
    ret ERR_EXISTS if APR_STATUS_IS_EEXIST(status) != 0
    ret ERR_PERMISSION if APR_STATUS_IS_EACCES(status) != 0
    ret ERR_ARGUMENT if APR_STATUS_IS_EINVAL(status) != 0
    ret ERR_MEMORY if APR_STATUS_IS_ENOMEM(status) != 0
    ret ERR_STATE if APR_STATUS_IS_EBUSY(status) != 0
    ret ERR_TIMEOUT if APR_STATUS_IS_TIMEUP(status) != 0 or APR_STATUS_IS_ETIMEDOUT(status) != 0
    ret ERR_UNSUPPORTED if APR_STATUS_IS_ENOTIMPL(status) != 0
    ret ERR_IO if APR_STATUS_IS_EOF(status) != 0
    ret ERR_FAILED

/**
 * The error for a failed OS or APR call, with the message "<what>: <what the status means>".
 * @param status the apr_status_t the call returned
 * @param what what was being done ("open the socket")
 */
fn osError(status: Int, what: String): OsError = OsError { statusKind(status), "${what}: ${statusMessage(status)}", status }

/**
 * The error for a failed read or write of a file, with the message "<path>: <what the status means>".
 * @param status the apr_status_t the call returned
 * @param path the path of the file
 */
fn ioError(status: Int, path: String): IoError do
    val kind = statusKind(status)
    ret IoError { if kind == ERR_FAILED do ERR_IO else kind, "${path}: ${statusMessage(status)}", status, path }

/**
 * Checks the result of an APR call: `try checkStatus(apr_dir_make(...), "make the directory"c)`.
 * @param status the apr_status_t the call returned
 * @param what what was being done, for the message
 * @throws when `status` isn't APR_SUCCESS (see {@link osError})
 */
fn checkStatus(status: Int, what: CString) throws OsError do
    throw osError(status, format("${what}")) if status != APR_SUCCESS

/**
 * The name of an `ERR_*` code, in words: "parse error" for {@link ERR_PARSE}.
 * @return the name, "error" for a code that isn't one of them
 */
fn errorKind(code: Int): String do
    when code do
        ERR_PARSE do ret "parse error"
        ERR_RANGE do ret "range error"
        ERR_ARGUMENT do ret "invalid argument"
        ERR_NOT_FOUND do ret "not found"
        ERR_EXISTS do ret "already exists"
        ERR_PERMISSION do ret "permission denied"
        ERR_STATE do ret "invalid state"
        ERR_TIMEOUT do ret "timeout"
        ERR_UNSUPPORTED do ret "not supported"
        ERR_IO do ret "I/O error"
        ERR_MEMORY do ret "out of memory"
    ret "error"

/** The error in words: "<kind>: <message>", e.g. "parse error: expected a digit". See {@link errorKind}. */
fn describe(e: Error): String = "${errorKind(e.code)}: ${e.message}"

/** Stops the program: flushes stdout, prints "panic: <message>" to stderr, and exits with status 1. */
fn panic(message: String): Never do
    fflush(stdout)
    fprintf(stderr, "panic: %.*s\n"c, message.len as Int, message.data)
    exit(1)
