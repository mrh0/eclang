/**
 * Bindings for SQLite 3, an SQL database in a library: a whole database in one file (or in memory), with no server.
 * https://sqlite.org/c3ref/intro.html
 *
 * Written for SQLite 3.46 (most of it works with 3.38 and later). The C API: sqlite3_open_v2 (or sqliteOpen) gives a
 * connection; sqlite3_prepare_v2 compiles one SQL statement; sqlite3_bind_* gives its `?` parameters values;
 * sqlite3_step runs it, returning SQLITE_ROW per row (read it with sqlite3_column_*) and SQLITE_DONE at the end;
 * sqlite3_reset runs it again; sqlite3_finalize frees it; sqlite3_close closes the connection.
 *
 * The helpers at the end (sqliteOpen, sqliteExec, sqlitePrepare, sqliteStep, sqliteBind, sqliteText, ...) turn result
 * codes into SqliteError values for `try`, and copy what SQLite returns into eclang Strings and arrays.
 */
use "sqlite3.h"
use "lib:sqlite3"
use "core:c/string.ec"
use "core:errors.ec"
use "core:string.ec"
use "constants.ec"

// ---------------------------------------------------------------------------------------------------------------------
// Types
// ---------------------------------------------------------------------------------------------------------------------

/** A database connection. Open it with sqlite3_open_v2 (or sqliteOpen), close it with sqlite3_close. */
declare rec sqlite3
/** A prepared statement: one SQL statement, compiled. Make it with sqlite3_prepare_v2, free it with sqlite3_finalize. */
declare rec sqlite3_stmt
/** A value of any SQL type: an argument of an SQL function, or sqlite3_column_value. */
declare rec sqlite3_value
/** What an SQL function written in eclang is given to set its result (sqlite3_result_*). */
declare rec sqlite3_context
/** An online backup, from sqlite3_backup_init. */
declare rec sqlite3_backup
/** An open BLOB, read and written in pieces: sqlite3_blob_open. */
declare rec sqlite3_blob

/**
 * What SQLite calls to free memory it was given (a bound value or a result). eclang passes SQLITE_TRANSIENT, which
 * makes SQLite copy the value first. SQLITE_STATIC, which would make it keep a pointer into eclang's memory without
 * a copy, is left out: eclang can't follow how long SQLite keeps it.
 */
declare type sqlite3_destructor_type as (p: @Any) => None
/** Makes SQLite copy a value before the call returns, so the value can be freed or changed afterwards. */
declare val SQLITE_TRANSIENT as sqlite3_destructor_type

/** A row of sqlite3_exec: arg as given, the number of columns, their values as text (null for NULL) and their names. Return 0 to go on, anything else to stop with SQLITE_ABORT. */
declare type sqlite3_callback as (arg: @Any?, columns: Int, values: CString?[]c, names: CString[]c) => Int
/** An SQL function (xFunc) or a step of an aggregate one (xStep): set the result with sqlite3_result_*. */
declare type sqlite3_function as (ctx: @sqlite3_context, argc: Int, argv: @sqlite3_value[]c) => None
/** The end of an aggregate SQL function (xFinal): set its result with sqlite3_result_*. */
declare type sqlite3_final as (ctx: @sqlite3_context) => None
/** Called with the argument it was registered with; return non-zero to interrupt (progress) or roll back (commit hook). */
declare type sqlite3_busy_callback as (arg: @Any?) => Int
/** Called with the argument it was registered with. */
declare type sqlite3_void_callback as (arg: @Any?) => None
/** sqlite3_trace_v2: the SQLITE_TRACE_* event, the context it was registered with, and two values that depend on the event. */
declare type sqlite3_trace_callback as (event: UInt, ctx: @Any?, p: @Any?, x: @Any?) => Int

// ---------------------------------------------------------------------------------------------------------------------
// The library
// ---------------------------------------------------------------------------------------------------------------------

/** The version of the library: "3.46.1". SQLITE_VERSION is that of the header. */
declare fn sqlite3_libversion as (): CString
/** The version of the library as a number: 3046001. */
declare fn sqlite3_libversion_number as (): Int
/** The check-in of the library's source code. */
declare fn sqlite3_sourceid as (): CString
/** 0 when the library was built without threads; otherwise connections may be used from other threads (one at a time, unless 1). */
declare fn sqlite3_threadsafe as (): Int
/** Whether the library was built with the option, named without the SQLITE_ prefix: "ENABLE_FTS5". */
declare fn sqlite3_compileoption_used as (zOptName: CString): Int
/** Sets the library up; the other functions do so when needed. */
declare fn sqlite3_initialize as (): Int
/** Undoes sqlite3_initialize, after every connection is closed. */
declare fn sqlite3_shutdown as (): Int
/** How many bytes SQLite has allocated now. */
declare fn sqlite3_memory_used as (): Long
/** Memory from SQLite's allocator; null when out of memory. Free it with sqlite3_free. */
declare fn sqlite3_malloc as (n: Int): @Any?
/** Frees what SQLite allocated for you: an error message of sqlite3_exec, sqlite3_expanded_sql, sqlite3_serialize. */
declare fn sqlite3_free as (p: @Any?)
/** 1 when sql ends with a complete SQL statement (a `;` outside strings, comments and triggers): for reading SQL a line at a time. */
declare fn sqlite3_complete as (sql: CString): Int
/** The number of SQL keywords. */
declare fn sqlite3_keyword_count as (): Int

// ---------------------------------------------------------------------------------------------------------------------
// Connections
// ---------------------------------------------------------------------------------------------------------------------

/**
 * Opens (or creates) a database file; ":memory:" is a new database in memory, "" a temporary one on disk. Sets db
 * even when it fails, so that sqlite3_errmsg(db) can say why: close it either way.
 */
declare fn sqlite3_open as (filename: CString, out ppDb: @sqlite3): Int
/**
 * sqlite3_open with SQLITE_OPEN_* flags: SQLITE_OPEN_READONLY, or SQLITE_OPEN_READWRITE (| SQLITE_OPEN_CREATE to
 * make the file), and SQLITE_OPEN_URI for "file:" names. zVfs: the name of a VFS, or null for the usual one.
 */
declare fn sqlite3_open_v2 as (filename: CString, out ppDb: @sqlite3, flags: Int, zVfs: CString? = null): Int
/** Closes a connection; SQLITE_BUSY while statements of it aren't finalized. Null does nothing. */
declare fn sqlite3_close as (db: @sqlite3?): Int
/** Closes a connection once its statements and backups are finished, even if some are still open now. */
declare fn sqlite3_close_v2 as (db: @sqlite3?): Int
/** Waits up to ms milliseconds for a lock another connection holds, instead of failing with SQLITE_BUSY at once. */
declare fn sqlite3_busy_timeout as (db: @sqlite3, ms: Int): Int
/** Sets a limit (SQLITE_LIMIT_*) of the connection and returns the old one; a negative newVal only reads it. */
declare fn sqlite3_limit as (db: @sqlite3, id: Int, newVal: Int): Int
/** The file of a database of the connection ("main", "temp" or an attached one); "" for one in memory, null if it has no such database. */
declare fn sqlite3_db_filename as (db: @sqlite3, zDbName: CString = "main"c): CString?
/** The name of the connection's database number N: 0 is "main", 1 "temp", then attached ones; null past the end. */
declare fn sqlite3_db_name as (db: @sqlite3, n: Int): CString?
/** 1 if the database is read-only, 0 if not, -1 if the connection has no such database. */
declare fn sqlite3_db_readonly as (db: @sqlite3, zDbName: CString = "main"c): Int
/** SQLITE_TXN_NONE, SQLITE_TXN_READ or SQLITE_TXN_WRITE: the transaction the connection has on the database (null: on any). */
declare fn sqlite3_txn_state as (db: @sqlite3, zSchema: CString? = null): Int
/** 1 outside a transaction (each statement commits itself), 0 inside one (after BEGIN). */
declare fn sqlite3_get_autocommit as (db: @sqlite3): Int
/** Makes the statements running on the connection stop with SQLITE_INTERRUPT; another thread may call it. */
declare fn sqlite3_interrupt as (db: @sqlite3)
/** 1 while an interrupt is pending. */
declare fn sqlite3_is_interrupted as (db: @sqlite3): Int
/** Writes the pages the connection changed to disk, in a write transaction. */
declare fn sqlite3_db_cacheflush as (db: @sqlite3): Int
/** Frees as much of the connection's memory as it can. */
declare fn sqlite3_db_release_memory as (db: @sqlite3): Int
/** The rowid of the last row an INSERT on the connection added. */
declare fn sqlite3_last_insert_rowid as (db: @sqlite3): Long
/** Sets what sqlite3_last_insert_rowid returns. */
declare fn sqlite3_set_last_insert_rowid as (db: @sqlite3, rowid: Long)
/** How many rows the last INSERT, UPDATE or DELETE changed (not counting triggers). */
declare fn sqlite3_changes64 as (db: @sqlite3): Long
/** sqlite3_changes64, as an Int. */
declare fn sqlite3_changes as (db: @sqlite3): Int
/** How many rows the INSERTs, UPDATEs and DELETEs of the connection changed since it opened. */
declare fn sqlite3_total_changes64 as (db: @sqlite3): Long
/** The next prepared statement of the connection after stmt (the first for null); null at the end. */
declare fn sqlite3_next_stmt as (db: @sqlite3, stmt: @sqlite3_stmt?): @sqlite3_stmt?
/**
 * Checkpoints a database in WAL mode (zDb null: all of them), with SQLITE_CHECKPOINT_*. pnLog and pnCkpt, if not
 * null, get the size of the log and how much of it was copied, in frames.
 */
declare fn sqlite3_wal_checkpoint_v2 as (db: @sqlite3, zDb: CString?, eMode: Int, pnLog: @Int? = null, pnCkpt: @Int? = null): Int

// ---------------------------------------------------------------------------------------------------------------------
// Errors
// ---------------------------------------------------------------------------------------------------------------------

/** The primary result code of the connection's last call that failed (SQLITE_OK if it didn't). */
declare fn sqlite3_errcode as (db: @sqlite3): Int
/** The extended result code of the connection's last call that failed: SQLITE_CONSTRAINT_UNIQUE rather than SQLITE_CONSTRAINT. */
declare fn sqlite3_extended_errcode as (db: @sqlite3): Int
/** Why the connection's last call failed, in English: "no such table: books". Valid until the next call on the connection. */
declare fn sqlite3_errmsg as (db: @sqlite3?): CString
/** What a result code means, in English: "constraint failed". */
declare fn sqlite3_errstr as (rc: Int): CString
/** Where in the SQL text the error of the last call is, in bytes, or -1. */
declare fn sqlite3_error_offset as (db: @sqlite3): Int
/** onoff 1: functions on the connection return extended result codes rather than primary ones. */
declare fn sqlite3_extended_result_codes as (db: @sqlite3, onoff: Int): Int

// ---------------------------------------------------------------------------------------------------------------------
// Running SQL
// ---------------------------------------------------------------------------------------------------------------------

/**
 * Runs the SQL statements in sql, one after another, calling callback (if not null) with each row. errmsg gets an
 * error message, which you free with sqlite3_free; sqlite3_errmsg(db) has it too, so it can be left out.
 */
declare fn sqlite3_exec as (db: @sqlite3, sql: CString, callback: sqlite3_callback?, arg: @Any?, out errmsg: CString): Int
/** sqlite3_exec without an error message (read it with sqlite3_errmsg): `sqlite3_exec(db, "CREATE TABLE t (x)"c)`. */
declare fn sqlite3_exec as (db: @sqlite3, sql: CString, callback: sqlite3_callback? = null, arg: @Any? = null, errmsg: @CString? = null): Int

/**
 * Compiles the first SQL statement of zSql, nByte bytes long (or up to its 0 if negative), into stmt. stmt is null
 * when zSql has no statement (only spaces or comments). pzTail is set to where the next statement starts.
 */
declare fn sqlite3_prepare_v2 as (db: @sqlite3, zSql: Char[]c, nByte: Int, out ppStmt: @sqlite3_stmt, out pzTail: Char[]c): Int
/** sqlite3_prepare_v2 without the rest of the text: `sqlite3_prepare_v2(db, sql.data, sql.len as Int, mut stmt)`. */
declare fn sqlite3_prepare_v2 as (db: @sqlite3, zSql: Char[]c, nByte: Int, out ppStmt: @sqlite3_stmt, pzTail: @(Char[]c)? = null): Int
/** sqlite3_prepare_v2 with SQLITE_PREPARE_* flags: SQLITE_PREPARE_PERSISTENT for a statement that is kept and run often. */
declare fn sqlite3_prepare_v3 as (db: @sqlite3, zSql: Char[]c, nByte: Int, prepFlags: UInt, out ppStmt: @sqlite3_stmt, out pzTail: Char[]c): Int
/** sqlite3_prepare_v3 without the rest of the text. */
declare fn sqlite3_prepare_v3 as (db: @sqlite3, zSql: Char[]c, nByte: Int, prepFlags: UInt, out ppStmt: @sqlite3_stmt, pzTail: @(Char[]c)? = null): Int
/**
 * Runs the statement until its next row: SQLITE_ROW when there is one (read it with sqlite3_column_*), SQLITE_DONE
 * when it has finished, or an error code.
 */
declare fn sqlite3_step as (stmt: @sqlite3_stmt): Int
/** Makes the statement ready to run again from the start; it keeps the values bound to it. */
declare fn sqlite3_reset as (stmt: @sqlite3_stmt): Int
/** Frees a statement. Null does nothing. */
declare fn sqlite3_finalize as (stmt: @sqlite3_stmt?): Int
/** Sets every parameter of the statement to NULL. */
declare fn sqlite3_clear_bindings as (stmt: @sqlite3_stmt): Int
/** The connection of a statement. */
declare fn sqlite3_db_handle as (stmt: @sqlite3_stmt): @sqlite3
/** The SQL text of a statement; null when it has none. */
declare fn sqlite3_sql as (stmt: @sqlite3_stmt): CString?
/** The SQL text of a statement with its parameters replaced by their values; free it with sqlite3_free. Null when out of memory. */
declare fn sqlite3_expanded_sql as (stmt: @sqlite3_stmt): CString?
/** 1 when the statement doesn't write to the database (a SELECT). */
declare fn sqlite3_stmt_readonly as (stmt: @sqlite3_stmt): Int
/** 1 when the statement has run but not to its end (or reset). */
declare fn sqlite3_stmt_busy as (stmt: @sqlite3_stmt): Int
/** 1 for EXPLAIN, 2 for EXPLAIN QUERY PLAN, 0 for other statements. */
declare fn sqlite3_stmt_isexplain as (stmt: @sqlite3_stmt): Int

// ---------------------------------------------------------------------------------------------------------------------
// Parameters: `?`, `?NNN`, `:name`, `@name` and `$name` in the SQL, numbered from 1
// ---------------------------------------------------------------------------------------------------------------------

/** How many parameters the statement has: the largest number of one. */
declare fn sqlite3_bind_parameter_count as (stmt: @sqlite3_stmt): Int
/** The number of the parameter called zName, with its `:`, `@` or `$`; 0 if it has none of that name. */
declare fn sqlite3_bind_parameter_index as (stmt: @sqlite3_stmt, zName: CString): Int
/** The name of parameter i, with its `:`, `@` or `$`; null for a `?` or a number out of range. */
declare fn sqlite3_bind_parameter_name as (stmt: @sqlite3_stmt, i: Int): CString?
/** Sets parameter i to NULL. */
declare fn sqlite3_bind_null as (stmt: @sqlite3_stmt, i: Int): Int
/** Sets parameter i to an integer. */
declare fn sqlite3_bind_int as (stmt: @sqlite3_stmt, i: Int, value: Int): Int
/** Sets parameter i to an integer. */
declare fn sqlite3_bind_int64 as (stmt: @sqlite3_stmt, i: Int, value: Long): Int
/** Sets parameter i to a floating-point number. */
declare fn sqlite3_bind_double as (stmt: @sqlite3_stmt, i: Int, value: Double): Int
/** Sets parameter i to n bytes of UTF-8 text (up to its 0 if n is negative). Pass SQLITE_TRANSIENT: sqliteBind does. */
declare fn sqlite3_bind_text as (stmt: @sqlite3_stmt, i: Int, text: Char[]c, n: Int, destructor: sqlite3_destructor_type): Int
/** Sets parameter i to a BLOB of n bytes. Pass SQLITE_TRANSIENT: sqliteBindBlob does. */
declare fn sqlite3_bind_blob as (stmt: @sqlite3_stmt, i: Int, data: @Any, n: Int, destructor: sqlite3_destructor_type): Int
/** Sets parameter i to a BLOB of n zero bytes, to fill in later with sqlite3_blob_write. */
declare fn sqlite3_bind_zeroblob as (stmt: @sqlite3_stmt, i: Int, n: Int): Int
/** Sets parameter i to a copy of a value. */
declare fn sqlite3_bind_value as (stmt: @sqlite3_stmt, i: Int, value: @sqlite3_value): Int

// ---------------------------------------------------------------------------------------------------------------------
// Columns of a result row, numbered from 0. Pointers SQLite returns are valid until the next step, reset or finalize
// of the statement, or another sqlite3_column_* of the same column: sqliteText and sqliteBlob copy them.
// ---------------------------------------------------------------------------------------------------------------------

/** How many columns the statement's rows have; 0 for a statement that returns none (an UPDATE). */
declare fn sqlite3_column_count as (stmt: @sqlite3_stmt): Int
/** How many columns the current row has: sqlite3_column_count after SQLITE_ROW, otherwise 0. */
declare fn sqlite3_data_count as (stmt: @sqlite3_stmt): Int
/** The name of column i: its `AS` name, or one SQLite makes up. Null when out of memory. */
declare fn sqlite3_column_name as (stmt: @sqlite3_stmt, i: Int): CString?
/** The type column i was declared with in its table ("INTEGER", "TEXT", ...); null for an expression. */
declare fn sqlite3_column_decltype as (stmt: @sqlite3_stmt, i: Int): CString?
/** The database of the table column i comes from; null for an expression. Needs a library built with SQLITE_ENABLE_COLUMN_METADATA. */
declare fn sqlite3_column_database_name as (stmt: @sqlite3_stmt, i: Int): CString?
/** The table column i comes from; null for an expression. Needs SQLITE_ENABLE_COLUMN_METADATA. */
declare fn sqlite3_column_table_name as (stmt: @sqlite3_stmt, i: Int): CString?
/** The name of column i in its table; null for an expression. Needs SQLITE_ENABLE_COLUMN_METADATA. */
declare fn sqlite3_column_origin_name as (stmt: @sqlite3_stmt, i: Int): CString?
/** The type of the value of column i in this row: SQLITE_INTEGER, SQLITE_FLOAT, SQLITE_TEXT, SQLITE_BLOB or SQLITE_NULL. */
declare fn sqlite3_column_type as (stmt: @sqlite3_stmt, i: Int): Int
/** Column i as an integer (converted if it isn't one; NULL is 0). */
declare fn sqlite3_column_int as (stmt: @sqlite3_stmt, i: Int): Int
/** Column i as an integer (converted if it isn't one; NULL is 0). */
declare fn sqlite3_column_int64 as (stmt: @sqlite3_stmt, i: Int): Long
/** Column i as a floating-point number (converted if it isn't one; NULL is 0.0). */
declare fn sqlite3_column_double as (stmt: @sqlite3_stmt, i: Int): Double
/** Column i as UTF-8 text with a 0 after it, of sqlite3_column_bytes bytes; null for NULL. sqliteText copies it into a String. */
declare fn sqlite3_column_text as (stmt: @sqlite3_stmt, i: Int): UChar[]c?
/** Column i as a BLOB of sqlite3_column_bytes bytes; null for NULL and for an empty BLOB. sqliteBlob copies it. */
declare fn sqlite3_column_blob as (stmt: @sqlite3_stmt, i: Int): @Any?
/** The size in bytes of the text or BLOB of column i, after sqlite3_column_text or sqlite3_column_blob. */
declare fn sqlite3_column_bytes as (stmt: @sqlite3_stmt, i: Int): Int
/** Column i as a value, for sqlite3_bind_value or sqlite3_value_dup. */
declare fn sqlite3_column_value as (stmt: @sqlite3_stmt, i: Int): @sqlite3_value

// ---------------------------------------------------------------------------------------------------------------------
// Values: the arguments of SQL functions
// ---------------------------------------------------------------------------------------------------------------------

/** The type of a value: SQLITE_INTEGER, SQLITE_FLOAT, SQLITE_TEXT, SQLITE_BLOB or SQLITE_NULL. */
declare fn sqlite3_value_type as (v: @sqlite3_value): Int
/** The type a value would have as a number: text that looks like one is SQLITE_INTEGER or SQLITE_FLOAT. */
declare fn sqlite3_value_numeric_type as (v: @sqlite3_value): Int
/** A value as an integer. */
declare fn sqlite3_value_int as (v: @sqlite3_value): Int
/** A value as an integer. */
declare fn sqlite3_value_int64 as (v: @sqlite3_value): Long
/** A value as a floating-point number. */
declare fn sqlite3_value_double as (v: @sqlite3_value): Double
/** A value as UTF-8 text with a 0 after it, of sqlite3_value_bytes bytes; null for NULL. sqliteValueText copies it. */
declare fn sqlite3_value_text as (v: @sqlite3_value): UChar[]c?
/** A value as a BLOB of sqlite3_value_bytes bytes; null for NULL and for an empty BLOB. */
declare fn sqlite3_value_blob as (v: @sqlite3_value): @Any?
/** The size in bytes of a value's text or BLOB, after sqlite3_value_text or sqlite3_value_blob. */
declare fn sqlite3_value_bytes as (v: @sqlite3_value): Int
/** A copy of a value, which outlives the call; free it with sqlite3_value_free. Null when out of memory. */
declare fn sqlite3_value_dup as (v: @sqlite3_value): @sqlite3_value?
/** Frees a copy made by sqlite3_value_dup. */
declare fn sqlite3_value_free as (v: @sqlite3_value?)

// ---------------------------------------------------------------------------------------------------------------------
// SQL functions written in eclang
// ---------------------------------------------------------------------------------------------------------------------

/**
 * Adds an SQL function to the connection: zFunctionName, taking nArg arguments (-1: any number), with eTextRep
 * SQLITE_UTF8, | SQLITE_DETERMINISTIC when it always gives the same result for the same arguments. A plain function
 * has xFunc; an aggregate one (like sum) has xStep and xFinal instead. They are C functions: `fn name`, or a lambda
 * that uses no local variables. pApp is what sqlite3_user_data gives them, and xDestroy frees it when the function is
 * replaced or the connection closes.
 */
declare fn sqlite3_create_function_v2 as (db: @sqlite3, zFunctionName: CString, nArg: Int, eTextRep: Int, kept pApp: @Any?, xFunc: sqlite3_function?, xStep: sqlite3_function?, xFinal: sqlite3_final?, xDestroy: sqlite3_void_callback? = null): Int
/** The pApp the function was added with. */
declare fn sqlite3_user_data as (ctx: @sqlite3_context): @Any?
/** The connection running the function. */
declare fn sqlite3_context_db_handle as (ctx: @sqlite3_context): @sqlite3
/** Memory of nBytes for an aggregate function's state, zeroed on its first call and the same on later calls of one group; freed after xFinal. Null when out of memory. */
declare fn sqlite3_aggregate_context as (ctx: @sqlite3_context, nBytes: Int): @Any?
/** The result is NULL. */
declare fn sqlite3_result_null as (ctx: @sqlite3_context)
/** The result is an integer. */
declare fn sqlite3_result_int as (ctx: @sqlite3_context, value: Int)
/** The result is an integer. */
declare fn sqlite3_result_int64 as (ctx: @sqlite3_context, value: Long)
/** The result is a floating-point number. */
declare fn sqlite3_result_double as (ctx: @sqlite3_context, value: Double)
/** The result is n bytes of UTF-8 text. Pass SQLITE_TRANSIENT: sqliteResult does. */
declare fn sqlite3_result_text as (ctx: @sqlite3_context, text: Char[]c, n: Int, destructor: sqlite3_destructor_type)
/** The result is a BLOB of n bytes. Pass SQLITE_TRANSIENT. */
declare fn sqlite3_result_blob as (ctx: @sqlite3_context, data: @Any, n: Int, destructor: sqlite3_destructor_type)
/** The result is a BLOB of n zero bytes. */
declare fn sqlite3_result_zeroblob as (ctx: @sqlite3_context, n: Int)
/** The result is a copy of a value. */
declare fn sqlite3_result_value as (ctx: @sqlite3_context, value: @sqlite3_value)
/** The function fails with this message (n bytes, or up to its 0 if negative); SQLite copies it. */
declare fn sqlite3_result_error as (ctx: @sqlite3_context, message: CString, n: Int = -1)
/** The function fails with this result code. */
declare fn sqlite3_result_error_code as (ctx: @sqlite3_context, rc: Int)
/** The function fails because it ran out of memory. */
declare fn sqlite3_result_error_nomem as (ctx: @sqlite3_context)
/** The function fails because a string or BLOB is too big. */
declare fn sqlite3_result_error_toobig as (ctx: @sqlite3_context)

// ---------------------------------------------------------------------------------------------------------------------
// Hooks: C functions (`fn name`, or a lambda that uses no local variables) the connection calls
// ---------------------------------------------------------------------------------------------------------------------

/** Calls xProgress(arg) every nOps virtual machine steps while a statement runs; a non-zero result interrupts it. Null removes it. */
declare fn sqlite3_progress_handler as (db: @sqlite3, nOps: Int, xProgress: sqlite3_busy_callback?, kept arg: @Any?)
/** Calls xCallback(arg) before each commit; a non-zero result makes it a rollback. Returns the arg of the hook it replaces. */
declare fn sqlite3_commit_hook as (db: @sqlite3, xCallback: sqlite3_busy_callback?, kept arg: @Any?): @Any?
/** Calls xCallback(arg) after each rollback. Returns the arg of the hook it replaces. */
declare fn sqlite3_rollback_hook as (db: @sqlite3, xCallback: sqlite3_void_callback?, kept arg: @Any?): @Any?
/**
 * Calls xCallback on the SQLITE_TRACE_* events in uMask. For SQLITE_TRACE_STMT, p is the statement (a
 * `@sqlite3_stmt`) and x the SQL text (a CString); for SQLITE_TRACE_PROFILE, x points to the nanoseconds it took (a Long).
 */
declare fn sqlite3_trace_v2 as (db: @sqlite3, uMask: UInt, xCallback: sqlite3_trace_callback?, kept ctx: @Any?): Int

// ---------------------------------------------------------------------------------------------------------------------
// BLOBs in pieces, backups and serialization
// ---------------------------------------------------------------------------------------------------------------------

/** Opens the BLOB in column zColumn of row iRow of a table, to read (flags 0) or write (1) it. */
declare fn sqlite3_blob_open as (db: @sqlite3, zDb: CString, zTable: CString, zColumn: CString, iRow: Long, flags: Int, out ppBlob: @sqlite3_blob): Int
/** Moves an open BLOB to another row of the same table. */
declare fn sqlite3_blob_reopen as (blob: @sqlite3_blob, iRow: Long): Int
/** The size of an open BLOB in bytes. */
declare fn sqlite3_blob_bytes as (blob: @sqlite3_blob): Int
/** Reads n bytes at offset into buffer. */
declare fn sqlite3_blob_read as (blob: @sqlite3_blob, buffer: @Any, n: Int, offset: Int): Int
/** Writes n bytes of data at offset; a BLOB doesn't grow (make it with zeroblob(n)). */
declare fn sqlite3_blob_write as (blob: @sqlite3_blob, data: @Any, n: Int, offset: Int): Int
/** Closes an open BLOB. Null does nothing. */
declare fn sqlite3_blob_close as (blob: @sqlite3_blob?): Int

/**
 * Starts copying database zSourceName ("main") of pSource into zDestName of pDest; null when it can't (read why with
 * sqlite3_errmsg(pDest)). Then sqlite3_backup_step until it returns SQLITE_DONE, and sqlite3_backup_finish.
 */
declare fn sqlite3_backup_init as (pDest: @sqlite3, zDestName: CString, pSource: @sqlite3, zSourceName: CString): @sqlite3_backup?
/** Copies up to nPage pages (-1: all of them): SQLITE_OK while there are more, SQLITE_DONE at the end. */
declare fn sqlite3_backup_step as (p: @sqlite3_backup, nPage: Int): Int
/** Ends a backup and frees it; returns the error of a step that failed, or SQLITE_OK. */
declare fn sqlite3_backup_finish as (p: @sqlite3_backup): Int
/** How many pages are left to copy, as of the last step. */
declare fn sqlite3_backup_remaining as (p: @sqlite3_backup): Int
/** How many pages the source has, as of the last step. */
declare fn sqlite3_backup_pagecount as (p: @sqlite3_backup): Int

/**
 * The bytes of database zSchema ("main", or null) as they would be in a file; free them with sqlite3_free. piSize,
 * if not null, is `addrof` a Long that gets their size (C's type for it is `long long`, which eclang writes as Long).
 * Null when out of memory.
 */
declare fn sqlite3_serialize as (db: @sqlite3, zSchema: CString?, piSize: @Any?, mFlags: UInt = 0): UChar[]c?
/**
 * Makes database zSchema the szDb bytes of pData, the contents of a database file, of a buffer of szBuf bytes. With
 * SQLITE_DESERIALIZE_FREEONCLOSE, pData is from sqlite3_malloc and SQLite frees it; otherwise it must outlive the
 * connection, so it is `escaping`.
 */
declare fn sqlite3_deserialize as (db: @sqlite3, zSchema: CString?, escaping pData: UChar[]c, szDb: Long, szBuf: Long, mFlags: UInt): Int

// ---------------------------------------------------------------------------------------------------------------------
// Helpers: errors as values, and copies into eclang memory
// ---------------------------------------------------------------------------------------------------------------------

/**
 * An SQLite error, from {@link sqliteCheck} and the helpers below. `message` says what failed and SQLite's text for
 * it, `rc` is the (extended) result code, and `code` sorts it into one of the ERR_* kinds of core:errors.ec.
 */
rec SqliteError extends Error as
    /** The result code: SQLITE_CONSTRAINT_UNIQUE, SQLITE_BUSY, ... Its primary code is `rc & 0xff`. */
    rc: Int

/** The ERR_* kind of an SQLite result code, for {@link SqliteError}. */
fn sqliteErrorKind(rc: Int): Int do
    val primary = rc & 0xff
    ret ERR_NOT_FOUND if primary == SQLITE_CANTOPEN or primary == SQLITE_NOTFOUND
    ret ERR_PARSE if primary == SQLITE_CORRUPT or primary == SQLITE_NOTADB or primary == SQLITE_FORMAT
    ret ERR_MEMORY if primary == SQLITE_NOMEM
    ret ERR_PERMISSION if primary == SQLITE_PERM or primary == SQLITE_READONLY or primary == SQLITE_AUTH
    ret ERR_ARGUMENT if primary == SQLITE_MISUSE or primary == SQLITE_RANGE or primary == SQLITE_MISMATCH or primary == SQLITE_CONSTRAINT or primary == SQLITE_TOOBIG
    ret ERR_STATE if primary == SQLITE_BUSY or primary == SQLITE_LOCKED or primary == SQLITE_SCHEMA or primary == SQLITE_INTERRUPT or primary == SQLITE_ABORT
    ret ERR_IO if primary == SQLITE_IOERR or primary == SQLITE_FULL
    ret ERR_FAILED

/**
 * A SqliteError for result code rc: "what: SQLite's text". With a connection, the text is sqlite3_errmsg's, which
 * says more ("no such table: books"); without one (or when it has no error), sqlite3_errstr's ("SQL logic error").
 */
fn sqliteError(db: @sqlite3?, rc: Int, what: String): SqliteError do
    var text = fromC(sqlite3_errstr(rc))
    if val d = db do
        if sqlite3_errcode(d) != SQLITE_OK do
            text = fromC(sqlite3_errmsg(d))
    ret SqliteError { code = sqliteErrorKind(rc), message = "${what}: ${text}", rc = rc }

/**
 * rc when it means success (SQLITE_OK, SQLITE_ROW or SQLITE_DONE), or else a {@link SqliteError} saying what failed:
 * `try sqliteCheck(db, sqlite3_exec(db, "VACUUM"c), "can't vacuum")`.
 */
fn sqliteCheck(db: @sqlite3?, rc: Int, what: String): Int throws SqliteError do
    val primary = rc & 0xff
    throw sqliteError(db, rc, what) if primary != SQLITE_OK and primary != SQLITE_ROW and primary != SQLITE_DONE
    ret rc

/**
 * Opens (or with SQLITE_OPEN_CREATE, creates) the database file at path, ":memory:" for a new one in memory. Result
 * codes of the connection are extended ones. Close it with sqlite3_close (`defer sqlite3_close(db)`).
 */
fn sqliteOpen(path: String, flags: Int = SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE): @sqlite3 throws SqliteError do
    var opened: @sqlite3? = null
    val rc = sqlite3_open_v2(cstr(path), mut opened, flags | SQLITE_OPEN_EXRESCODE)
    if rc != SQLITE_OK do
        val e = sqliteError(opened, rc, "can't open ${path}")
        sqlite3_close(opened)
        throw e
    ret opened ?? throw sqliteError(null, SQLITE_NOMEM, "can't open ${path}")

/** Runs the SQL statements in sql, one after another, ignoring the rows they return: tables, transactions, PRAGMAs. */
fn sqliteExec(db: @sqlite3, sql: String) throws SqliteError do
    try sqliteCheck(db, sqlite3_exec(db, cstr(sql)), "can't run SQL")

/**
 * Compiles the SQL statement sql (one statement; for several, see sqlite3_prepare_v2's pzTail). Free it with
 * sqlite3_finalize (`defer sqlite3_finalize(stmt)`).
 */
fn sqlitePrepare(db: @sqlite3, sql: String): @sqlite3_stmt throws SqliteError do
    var stmt: @sqlite3_stmt? = null
    try sqliteCheck(db, sqlite3_prepare_v2(db, sql.data, sql.len as Int, mut stmt), "can't prepare \"${sql}\"")
    ret stmt ?? throw SqliteError { code = ERR_ARGUMENT, message = "can't prepare \"${sql}\": it holds no SQL statement", rc = SQLITE_MISUSE }

/**
 * Runs the statement until its next row: true when there is one (read it with sqlite3_column_*, sqliteText, ...),
 * false when it has finished:
 *
 *     while true do
 *         break if not try sqliteStep(stmt)
 *         io.println(sqliteText(stmt, 0) ?? "NULL")
 */
fn sqliteStep(stmt: @sqlite3_stmt): Bool throws SqliteError do
    val rc = sqlite3_step(stmt)
    ret true if rc == SQLITE_ROW
    ret false if rc == SQLITE_DONE
    val sql = sqlite3_sql(stmt) ?? "a statement"c
    throw sqliteError(sqlite3_db_handle(stmt), rc, "can't run \"${sql}\"")

/** The number of the parameter called name (with its `:`, `@` or `$`), or an error if the statement has none. */
fn sqliteParameter(stmt: @sqlite3_stmt, name: String): Int throws SqliteError do
    val i = sqlite3_bind_parameter_index(stmt, cstr(name))
    throw SqliteError { code = ERR_NOT_FOUND, message = "the statement has no parameter ${name}", rc = SQLITE_RANGE } if i == 0
    ret i

/** Sets parameter i (from 1) of the statement to an integer. */
fn sqliteBind(stmt: @sqlite3_stmt, i: Int, value: Int) throws SqliteError do
    try sqliteCheck(sqlite3_db_handle(stmt), sqlite3_bind_int64(stmt, i, value), "can't set parameter ${i}")

/** Sets parameter i (from 1) of the statement to an integer. */
fn sqliteBind(stmt: @sqlite3_stmt, i: Int, value: Long) throws SqliteError do
    try sqliteCheck(sqlite3_db_handle(stmt), sqlite3_bind_int64(stmt, i, value), "can't set parameter ${i}")

/** Sets parameter i (from 1) of the statement to a floating-point number. */
fn sqliteBind(stmt: @sqlite3_stmt, i: Int, value: Double) throws SqliteError do
    try sqliteCheck(sqlite3_db_handle(stmt), sqlite3_bind_double(stmt, i, value), "can't set parameter ${i}")

/** Sets parameter i (from 1) of the statement to (a copy of) text, or to NULL. */
fn sqliteBind(stmt: @sqlite3_stmt, i: Int, value: String?) throws SqliteError do
    var rc = SQLITE_OK
    if val s = value do
        rc = sqlite3_bind_text(stmt, i, s.data, s.len as Int, SQLITE_TRANSIENT)
    else
        rc = sqlite3_bind_null(stmt, i)
    try sqliteCheck(sqlite3_db_handle(stmt), rc, "can't set parameter ${i}")

/** Sets parameter i (from 1) of the statement to (a copy of) the bytes of data, as a BLOB. */
fn sqliteBindBlob(stmt: @sqlite3_stmt, i: Int, data: UChar[]) throws SqliteError do
    val rc = if data.len == 0 do sqlite3_bind_zeroblob(stmt, i, 0) else sqlite3_bind_blob(stmt, i, data.data, data.len as Int, SQLITE_TRANSIENT)
    try sqliteCheck(sqlite3_db_handle(stmt), rc, "can't set parameter ${i}")

/** A String of n bytes copied from p. */
priv fn sqliteCopyText(p: UChar[]c, n: Int): String do
    val text = new Char[n as Size]
    memcpy(text.data, p, n as Size)
    ret text !as String

/** Column i (from 0) of the current row as a String (a copy), or null if it is NULL. Numbers are converted to text. */
fn sqliteText(stmt: @sqlite3_stmt, i: Int): String? do
    val p = sqlite3_column_text(stmt, i) ?? ret null
    ret sqliteCopyText(p, sqlite3_column_bytes(stmt, i))

/** Column i (from 0) of the current row as bytes (a copy); empty for NULL. */
fn sqliteBlob(stmt: @sqlite3_stmt, i: Int): UChar[] do
    val p = sqlite3_column_blob(stmt, i)
    val n = sqlite3_column_bytes(stmt, i)
    val bytes = new UChar[n as Size]
    if val data = p do
        memcpy(bytes.data, data, n as Size)
    ret bytes

/** The name of column i (from 0) of the statement's rows. */
fn sqliteColumnName(stmt: @sqlite3_stmt, i: Int): String = fromC(sqlite3_column_name(stmt, i) ?? "?"c)

/** Argument i (from 0) of an SQL function, given its argc and argv: checks that i is less than argc. */
fn sqliteArg(argv: @sqlite3_value[]c, argc: Int, i: Int): @sqlite3_value requires i >= 0 and i < argc = unsafe argv[i]

/** An argument of an SQL function as a String (a copy), or null if it is NULL. */
fn sqliteValueText(v: @sqlite3_value): String? do
    val p = sqlite3_value_text(v) ?? ret null
    ret sqliteCopyText(p, sqlite3_value_bytes(v))

/** The result of an SQL function is (a copy of) text, or NULL. */
fn sqliteResult(ctx: @sqlite3_context, value: String?) do
    if val s = value do
        sqlite3_result_text(ctx, s.data, s.len as Int, SQLITE_TRANSIENT)
    else
        sqlite3_result_null(ctx)
