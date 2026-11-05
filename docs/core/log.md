# Module `log`

Logging: levels, timestamps, and output to stderr or a file, from any thread.

    use "core:log.ec"
    log.info("started")                                 // 2026-03-14T09:26:53.589Z INFO  started
    log.warn("disk ${percent}% full")
    log.error(problem)                                  // a message, or an Error

    log.setLevel(:debug)                                // :debug, :info (the first setting), :warn, :error or :off
    try log.toFile("app.log")                           // adds to the end of the file, and makes it if needed
    log.toStderr()                                      // back to where it started
    log.setTimestamps(:local)                           // :utc (the first setting), :local or :none

    val db = log.logger("db")                           // named, with its own settings
    log.info(db, "connected")                           // ... INFO  db: connected
    val queries = log.child(db, "queries")              // writes where db does
    log.debug(queries, "SELECT 1")                      // ... DEBUG db.queries: SELECT 1
    log.debug("state: ${expensive()}") if log.enabled(:debug)

Each line is written whole, so the lines of several threads don't mix, also with other processes adding to the same
file. Logging can't fail: when a file can't be written, the line goes to stderr, to say so. Lines aren't held back,
so they are there if the program crashes right after.

The functions without a [`Logger`](#log.Logger) use one that lives as long as the program. Another one belongs to the pool
that was current when it was made, and so does its file. A message is made before it gets here, even when its level is
off, so one that takes work to make needs [`enabled`](#log.enabled).

## Types

### <a id="log.Level"></a>`Level`

```ec
declare type Level as :debug | :info | :warn | :error | :off
```

How much a message matters, from `:debug` to `:error`. A [`Logger`](#log.Logger) writes the messages of its level and above;
`:off` writes none.

### <a id="log.Timestamps"></a>`Timestamps`

```ec
declare type Timestamps as :utc | :local | :none
```

What is at the start of each line: the time in UTC, the local time, or nothing.

### <a id="log.Logger"></a>`Logger`

```ec
rec Logger
```

Writes messages somewhere: [`logger`](#log.logger).

**Fields**

- <a id="log.Logger.name"></a>`name: String` — What is before each message, or "" for nothing.
- <a id="log.Logger.level"></a>`level: Level` — The lowest level it writes. Change it with [`setLevel`](#log.setLevel).
- <a id="log.Logger.timestamps"></a>`timestamps: Timestamps` — What it writes at the start of each line. Change it with [`setTimestamps`](#log.setTimestamps).
- <a id="log.Logger.sink"></a>`sink: @Sink` — Where its lines go.

## Functions

### <a id="log.logger"></a>`logger`

```ec
fn logger(name: String = ""): @Logger
```

A new logger writing to stderr, with the level `:info`, which belongs to the current pool.

**Parameters**

- `name` — what is before each message; "" for nothing

### <a id="log.child"></a>`child`

```ec
fn child(parent: @Logger, name: String): @Logger
```

A logger that writes where `parent` does, with the same settings at first. Those are its own after that, except for the
output: [`toFile`](#log.toFile) on one of them changes it for both.

**Parameters**

- `parent` — the logger
- `name` — its name, which comes after the name of `parent`, with a dot

### <a id="log.parseLevel"></a>`parseLevel`

```ec
fn parseLevel(s: String): Level throws ParseError
```

The level called `s`, in any case, for reading it from a setting: `parseLevel("warn")`. "warning" is `:warn`.

**Throws**

- `ParseError` — when there is no such level

### <a id="log.setLevel"></a>`setLevel`

```ec
fn setLevel(l: @Logger, level: Level)
```

Sets the lowest level `l` writes.

### `setLevel`

```ec
fn setLevel(level: Level)
```

Sets the lowest level that is written.

### <a id="log.enabled"></a>`enabled`

```ec
fn enabled(l: @Logger, level: Level): Bool
```

Whether `l` would write a message of `level`, to skip making one that takes work.

### `enabled`

```ec
fn enabled(level: Level): Bool
```

Whether a message of `level` would be written, to skip making one that takes work.

### <a id="log.setTimestamps"></a>`setTimestamps`

```ec
fn setTimestamps(l: @Logger, timestamps: Timestamps)
```

Sets what `l` writes at the start of each line.

### `setTimestamps`

```ec
fn setTimestamps(timestamps: Timestamps)
```

Sets what is written at the start of each line.

### <a id="log.toFile"></a>`toFile`

```ec
fn toFile(l: @Logger, path: String) throws IoError
```

Makes `l`, and the loggers sharing its output ([`child`](#log.child)), add their lines to the end of a file, which is made if it
doesn't exist. Writing to one file from several processes is fine.

**Parameters**

- `l` — the logger
- `path` — where the file is

**Throws**

- `IoError` — when it can't be opened; lines then go where they went before

### `toFile`

```ec
fn toFile(path: String) throws IoError
```

Makes lines go to the end of a file; see the other `toFile`.

**Parameters**

- `path` — where the file is

**Throws**

- `IoError` — when it can't be opened; lines then go where they went before

### <a id="log.toStderr"></a>`toStderr`

```ec
fn toStderr(l: @Logger)
```

Makes `l`, and the loggers sharing its output, write to stderr. A file it wrote to is closed.

### `toStderr`

```ec
fn toStderr()
```

Makes lines go to stderr. A file they went to is closed.

### <a id="log.write"></a>`write`

```ec
fn write(l: @Logger, level: Level, message: String)
```

Writes a message, if `level` is one that `l` writes.

**Parameters**

- `l` — the logger
- `level` — how much it matters
- `message` — what to say

### <a id="log.debug"></a>`debug`

```ec
fn debug(message: String)
```

Writes a message of the level `:debug`: details that are only of use when looking for a problem.

### <a id="log.info"></a>`info`

```ec
fn info(message: String)
```

Writes a message of the level `:info`: what the program is doing.

### <a id="log.warn"></a>`warn`

```ec
fn warn(message: String)
```

Writes a message of the level `:warn`: something is wrong, but the program can go on.

### <a id="log.error"></a>`error`

```ec
fn error(message: String)
```

Writes a message of the level `:error`: something has failed.

### `error`

```ec
fn error(e: Error)
```

Writes an Error, of any kind, as a message of the level `:error`.

### `debug`

```ec
fn debug(l: @Logger, message: String)
```

Writes a message of the level `:debug` to `l`.

### `info`

```ec
fn info(l: @Logger, message: String)
```

Writes a message of the level `:info` to `l`.

### `warn`

```ec
fn warn(l: @Logger, message: String)
```

Writes a message of the level `:warn` to `l`.

### `error`

```ec
fn error(l: @Logger, message: String)
```

Writes a message of the level `:error` to `l`.

### `error`

```ec
fn error(l: @Logger, e: Error)
```

Writes an Error, of any kind, as a message of the level `:error` to `l`.
