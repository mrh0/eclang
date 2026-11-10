# SQLite bindings

eclang bindings for [SQLite](https://sqlite.org) 3, an SQL database in a library: a whole database in one file (or
in memory), with no server. The names are SQLite's own, so [its C API documentation](https://sqlite.org/c3ref/intro.html)
carries over line by line. Helpers on top (`sqliteOpen`, `sqlitePrepare`, `sqliteStep`, `sqliteBind`, `sqliteText`,
...) turn result codes into error values and copy what SQLite returns into eclang memory.

They aren't part of `core:`:

| File | What for |
| --- | --- |
| [sqlite.ec](sqlite.ec) | connections, statements, parameters, columns, SQL functions written in eclang, hooks, BLOBs, backups; the helpers |
| [constants.ec](constants.ec) | result codes, open flags, types, configuration options and limits, generated from `sqlite3.h` by [generate.py](generate.py) |

## Setup

Install SQLite's development files: `libsqlite3-dev` on Debian and Ubuntu, `sqlite-devel` on Fedora, `sqlite` on
Arch and with Homebrew. They are written for SQLite 3.46, and most of them work with 3.38 and later.
`sqlite.ec` says `use "lib:sqlite3"`, so `run` and `lib` link it.

```
java -jar target/eclang-0.1.0.jar run bindings/sqlite/examples/books.ec
```

## Usage

`use` [sqlite.ec](sqlite.ec) by its path, relative to your file or to the working directory:

```plaintext
use "bindings/sqlite/sqlite.ec"
use "core:io.ec"

fn main() throws SqliteError do
    val db = try sqliteOpen("shop.db")
    defer sqlite3_close(db)
    try sqliteExec(db, "CREATE TABLE IF NOT EXISTS fruit (name TEXT, price REAL)")

    val add = try sqlitePrepare(db, "INSERT INTO fruit VALUES (?, ?)")
    defer sqlite3_finalize(add)
    try sqliteBind(add, 1, "pear")
    try sqliteBind(add, 2, 0.75)
    try sqliteStep(add)

    val q = try sqlitePrepare(db, "SELECT name, price FROM fruit ORDER BY price")
    defer sqlite3_finalize(q)
    while true do
        break if not try sqliteStep(q)
        val name = sqliteText(q, 0) ?? "NULL"
        io.println("${name}: ${sqlite3_column_double(q, 1):.2f}")
```

[examples/](examples) has two programs:

- [books.ec](examples/books.ec): a catalog in memory. Tables, a transaction of inserts through prepared statements with
  named parameters, a join with `GROUP BY`, an SQL function written in eclang, NULLs, a BLOB, and a constraint error
  handled as a value.
- [sql.ec](examples/sql.ec): runs SQL against a database file like the `sqlite3` shell: each statement of the text in
  turn (with `sqlite3_prepare_v2`'s `pzTail`), printing rows or how many rows changed.

## The helpers

| Helper | Does |
| --- | --- |
| `sqliteOpen(path, flags = SQLITE_OPEN_READWRITE \| SQLITE_OPEN_CREATE)` | opens a connection with extended result codes; `":memory:"` for a database in memory |
| `sqliteExec(db, sql)` | runs statements that return no rows you need: `CREATE`, `BEGIN`, `COMMIT`, `PRAGMA` |
| `sqlitePrepare(db, sql)` | compiles one statement; free it with `sqlite3_finalize` |
| `sqliteStep(stmt)` | `true` for a row, `false` at the end |
| `sqliteBind(stmt, i, value)` | sets parameter `i` (from 1) to an `Int`, `Long`, `Double`, `String` or `null`; `sqliteBindBlob` for bytes |
| `sqliteParameter(stmt, ":name")` | the number of a named parameter |
| `sqliteText(stmt, i)`, `sqliteBlob(stmt, i)` | column `i` (from 0) as a `String?` (null for NULL) or `UChar[]`, copied |
| `sqliteColumnName(stmt, i)` | the name of column `i` |
| `sqliteArg(argv, argc, i)`, `sqliteValueText(v)`, `sqliteResult(ctx, s)` | for SQL functions written in eclang |
| `sqliteCheck(db, rc, what)`, `sqliteError(db, rc, what)` | for the C functions: rc, or an error saying what failed |

Numbers are read with the C functions themselves: `sqlite3_column_int64`, `sqlite3_column_double`, and
`sqlite3_column_type` to tell a NULL from a 0.

**Errors.** A `SqliteError` is an `Error` (from `core:errors.ec`) whose `message` is "what: SQLite's text" (from
`sqlite3_errmsg`, as in "no such table: books"), whose `rc` is the extended result code
(`SQLITE_CONSTRAINT_UNIQUE`; `rc & 0xff` is the primary one), and whose `code` sorts it into an `ERR_*` kind:
`SQLITE_CANTOPEN` is `ERR_NOT_FOUND`, `SQLITE_CONSTRAINT` is `ERR_ARGUMENT`, `SQLITE_BUSY` is `ERR_STATE`, and so on.

**Loops.** `try` can't be a `while` condition, as it would run only once, so a loop over rows is
`while true do` with `break if not try sqliteStep(stmt)`.

## From C to eclang

| C | eclang |
| --- | --- |
| `sqlite3 *db; sqlite3_open_v2(path, &db, flags, NULL)` | `var db: @sqlite3? = null`, `sqlite3_open_v2(cstr(path), mut db, flags)`, or `try sqliteOpen(path)` |
| `sqlite3_prepare_v2(db, sql, -1, &stmt, NULL)` | `sqlite3_prepare_v2(db, sql.data, sql.len as Int, mut stmt)`: the last parameter has a default |
| `sqlite3_prepare_v2(db, sql, -1, &stmt, &tail)` | the same with `mut tail`, a `Char[]c?` variable: see [sql.ec](examples/sql.ec) |
| `sqlite3_exec(db, sql, NULL, NULL, NULL)` | `sqlite3_exec(db, "VACUUM"c)` |
| `sqlite3_bind_text(stmt, 1, s, -1, SQLITE_TRANSIENT)` | the same with `s.data, s.len as Int`, or `sqliteBind(stmt, 1, s)` |
| `(const char *)sqlite3_column_text(stmt, 0)` | `sqliteText(stmt, 0)`, a copy; `sqlite3_column_text` is a `UChar[]c?` |
| `sqlite3_int64` | `Long` |
| `int (*)(void*, int, char**, char**)` | `sqlite3_callback`: `fn name`, or a lambda that uses no local variables |
| `argv[0]` in an SQL function | `sqliteArg(argv, argc, 0)`, which checks the index |

**Memory.** Pointers SQLite returns (column text, BLOBs, error messages) are valid only until the statement steps,
resets or is finalized, so the helpers copy them into the current pool. Values given to SQLite are copied by it:
`SQLITE_TRANSIENT` is the only destructor bound. `SQLITE_STATIC`, which would make SQLite keep a pointer into
eclang's memory without a copy, is left out, because eclang can't check how long SQLite keeps it. Callbacks
(`sqlite3_create_function_v2`, the hooks, `sqlite3_trace_v2`) are C function pointers. What they are given to pass
on (`pApp`, `arg`) is `kept` by the connection.

**Threads.** A connection is used by one thread at a time (unless the library is built serialized, as most are:
`sqlite3_threadsafe()` is 1). Give each thread its own connection.

**Left out**: the authorizer, virtual tables, VFSes, mutexes, the session extension, the UTF-16 functions and `sqlite3_update_hook` (its callback takes a `long long`, which eclang can't write: see
[KNOWN_ISSUES.md](../../KNOWN_ISSUES.md)). `sqlite3_serialize`'s size is passed as `addrof` a `Long`, through an `@Any?`,
for the same reason.

## Regenerating and testing

```
bindings/sqlite/generate.py [/path/to/include] > bindings/sqlite/constants.ec
```

[constants.ec](constants.ec) has every `#define` of `sqlite3.h` with a prefix listed in `generate.py`, with the type
gcc gives it. [sqlite.ec](sqlite.ec) is written by hand: what C's types don't say (whether a pointer may be NULL,
whether the function fills it in, whether it keeps it) was decided per function from SQLite's documentation.

`tests/run.sh` (or `tests/run.sh sqlite`) checks the bindings when it finds `sqlite3.h`. It checks that every
`declare fn` matches its C declaration (`tools/check-bindings.py`), that `constants.ec` is what `generate.py` makes of
the header when that header is the version it was made from, and that the examples build with `-Werror` and print
what they should. Without the header it prints a `skip` line.
