# `core:errors.ec`

Common error types, for `throws`. Each one extends [`Error`](#Error) (`code: Int`, `message: String`), so a
function that `throws Error` can pass any of them on with `try`, and `e.code` says what kind it is.

    fn load(path: String): Config throws Error do
        val text = try readFile(path)            // throws IoError
        ret try parseConfig(text)                // throws ParseError

Make them with the functions below (`throw parseError("expected a digit", text, i)`) or as records
(`throw RangeError { ERR_RANGE, "too big" }`).

## Types

### <a id="Error"></a>`Error`

```ec
rec Error
```

The usual error type for `throws Error`. The other error types here extend it.

**Fields**

- <a id="Error.code"></a>`code: Int` — What kind of error it is: one of the `ERR_*` constants.
- <a id="Error.message"></a>`message: String` — What went wrong, for people to read.

### <a id="ParseError"></a>`ParseError`

```ec
rec ParseError extends Error
```

Text couldn't be parsed. Its code is [`ERR_PARSE`](#ERR_PARSE); make one with [`parseError`](#parseError).

**Fields**

- <a id="ParseError.input"></a>`input: String` — The text that couldn't be parsed.
- <a id="ParseError.position"></a>`position: Size` — The byte of `input` where the problem is.

### <a id="RangeError"></a>`RangeError`

```ec
rec RangeError extends Error
```

A number is out of the range it must be in. Its code is [`ERR_RANGE`](#ERR_RANGE); make one with [`rangeError`](#rangeError).

### <a id="ArgumentError"></a>`ArgumentError`

```ec
rec ArgumentError extends Error
```

An argument isn't valid. Its code is [`ERR_ARGUMENT`](#ERR_ARGUMENT); make one with [`argumentError`](#argumentError).

**Fields**

- <a id="ArgumentError.argument"></a>`argument: String` — The name of the argument that isn't valid.

### <a id="NotFoundError"></a>`NotFoundError`

```ec
rec NotFoundError extends Error
```

Something doesn't exist. Its code is [`ERR_NOT_FOUND`](#ERR_NOT_FOUND); make one with [`notFoundError`](#notFoundError).

**Fields**

- <a id="NotFoundError.name"></a>`name: String` — The name of what wasn't found: a key, a file, ...

### <a id="StateError"></a>`StateError`

```ec
rec StateError extends Error
```

The operation isn't possible in the current state (on a closed channel, a finished thread, ...). Its code is
[`ERR_STATE`](#ERR_STATE); make one with [`stateError`](#stateError).

### <a id="TimeoutError"></a>`TimeoutError`

```ec
rec TimeoutError extends Error
```

Waiting took longer than allowed. Its code is [`ERR_TIMEOUT`](#ERR_TIMEOUT); make one with [`timeoutError`](#timeoutError).

### <a id="OsError"></a>`OsError`

```ec
rec OsError extends Error
```

An operating system or APR call failed. Its code is the kind of the status ([`statusKind`](#statusKind)); make one
with [`osError`](#osError).

**Fields**

- <a id="OsError.status"></a>`status: Int` — The apr_status_t the call returned (an errno value on Unix).

### <a id="IoError"></a>`IoError`

```ec
rec IoError extends OsError
```

Reading or writing a file failed. Make one with [`ioError`](#ioError).

**Fields**

- <a id="IoError.path"></a>`path: String` — The path of the file.

### <a id="Location"></a>`Location`

```ec
rec Location
```

A place in the source: what `here` is, and what a parameter with the default `caller` is given, the place of the call.

    fn check(ok: Bool, at: Location = caller) do
        io.log("failed at ${at}") if not ok             // failed at main.ec:12

**Fields**

- <a id="Location.file"></a>`file: String` — The name of the file, without its directory: "main.ec".
- <a id="Location.line"></a>`line: Int` — The line, from 1.
- <a id="Location.column"></a>`column: Int` — The column, from 1.
- <a id="Location.function"></a>`function: String` — The function it is in, or "" outside functions.

## Values

### <a id="ERR_FAILED"></a>`ERR_FAILED`

```ec
const ERR_FAILED = 1
```

Error code: anything not covered by another `ERR_*` code.

### <a id="ERR_PARSE"></a>`ERR_PARSE`

```ec
const ERR_PARSE = 2
```

Error code: text isn't in the format it should be in.

### <a id="ERR_RANGE"></a>`ERR_RANGE`

```ec
const ERR_RANGE = 3
```

Error code: a number is too big or too small.

### <a id="ERR_ARGUMENT"></a>`ERR_ARGUMENT`

```ec
const ERR_ARGUMENT = 4
```

Error code: an argument isn't valid.

### <a id="ERR_NOT_FOUND"></a>`ERR_NOT_FOUND`

```ec
const ERR_NOT_FOUND = 5
```

Error code: a file, key or name doesn't exist.

### <a id="ERR_EXISTS"></a>`ERR_EXISTS`

```ec
const ERR_EXISTS = 6
```

Error code: it already exists.

### <a id="ERR_PERMISSION"></a>`ERR_PERMISSION`

```ec
const ERR_PERMISSION = 7
```

Error code: not allowed.

### <a id="ERR_STATE"></a>`ERR_STATE`

```ec
const ERR_STATE = 8
```

Error code: not possible now (closed, already started, ...).

### <a id="ERR_TIMEOUT"></a>`ERR_TIMEOUT`

```ec
const ERR_TIMEOUT = 9
```

Error code: it took too long.

### <a id="ERR_UNSUPPORTED"></a>`ERR_UNSUPPORTED`

```ec
const ERR_UNSUPPORTED = 10
```

Error code: not supported on this system.

### <a id="ERR_IO"></a>`ERR_IO`

```ec
const ERR_IO = 11
```

Error code: reading or writing failed.

### <a id="ERR_MEMORY"></a>`ERR_MEMORY`

```ec
const ERR_MEMORY = 12
```

Error code: out of memory.

## Functions

### <a id="parseError"></a>`parseError`

```ec
fn parseError(message: String, input: String, position: Size): ParseError
```

A [`ParseError`](#ParseError): `throw parseError("expected a digit", text, i)`.

**Parameters**

- `message` — what is wrong
- `input` — the text that couldn't be parsed
- `position` — the byte of `input` where the problem is

### <a id="rangeError"></a>`rangeError`

```ec
fn rangeError(message: String): RangeError
```

A [`RangeError`](#RangeError) with the `message`.

### <a id="argumentError"></a>`argumentError`

```ec
fn argumentError(argument: String, message: String): ArgumentError
```

An [`ArgumentError`](#ArgumentError).

**Parameters**

- `argument` — the name of the argument that isn't valid
- `message` — what is wrong with it

### <a id="notFoundError"></a>`notFoundError`

```ec
fn notFoundError(name: String): NotFoundError
```

A [`NotFoundError`](#NotFoundError) for `name`, with the message "'name' was not found".

### <a id="stateError"></a>`stateError`

```ec
fn stateError(message: String): StateError
```

A [`StateError`](#StateError) with the `message`.

### <a id="timeoutError"></a>`timeoutError`

```ec
fn timeoutError(message: String): TimeoutError
```

A [`TimeoutError`](#TimeoutError) with the `message`.

### <a id="statusMessage"></a>`statusMessage`

```ec
fn statusMessage(status: Int): String
```

What an APR status means, in words ("No such file or directory").

**Parameters**

- `status` — an apr_status_t, as an APR call returns it

### <a id="statusKind"></a>`statusKind`

```ec
fn statusKind(status: Int): Int
```

The `ERR_*` code for an APR status: [`ERR_NOT_FOUND`](#ERR_NOT_FOUND) for ENOENT, and so on.

**Parameters**

- `status` — an apr_status_t, as an APR call returns it

**Returns** the code, [`ERR_FAILED`](#ERR_FAILED) when no other fits

### <a id="osError"></a>`osError`

```ec
fn osError(status: Int, what: String): OsError
```

The error for a failed OS or APR call, with the message "<what>: <what the status means>".

**Parameters**

- `status` — the apr_status_t the call returned
- `what` — what was being done ("open the socket")

### <a id="ioError"></a>`ioError`

```ec
fn ioError(status: Int, path: String): IoError
```

The error for a failed read or write of a file, with the message "<path>: <what the status means>".

**Parameters**

- `status` — the apr_status_t the call returned
- `path` — the path of the file

### <a id="checkStatus"></a>`checkStatus`

```ec
fn checkStatus(status: Int, what: CString) throws OsError
```

Checks the result of an APR call: `try checkStatus(apr_dir_make(...), "make the directory"c)`.

**Parameters**

- `status` — the apr_status_t the call returned
- `what` — what was being done, for the message

**Throws**

- `OsError` — when `status` isn't APR_SUCCESS (see [`osError`](#osError))

### <a id="errorKind"></a>`errorKind`

```ec
fn errorKind(code: Int): String
```

The name of an `ERR_*` code, in words: "parse error" for [`ERR_PARSE`](#ERR_PARSE).

**Returns** the name, "error" for a code that isn't one of them

### <a id="describe"></a>`describe`

```ec
fn describe(e: Error): String
```

The error in words: "<kind>: <message>", e.g. "parse error: expected a digit". See [`errorKind`](#errorKind).

### <a id="str"></a>`str`

```ec
fn str(l: Location): String
```

"file.ec:12".

### <a id="panic"></a>`panic`

```ec
fn panic(message: String, at: Location = caller): Never
```

Stops the program: flushes stdout, prints "panic: <message>" and where it was called from to stderr, and exits with status 1.

**Parameters**

- `message` — what went wrong
- `at` — where: the call, unless it is given
