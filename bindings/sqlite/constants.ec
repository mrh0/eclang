/**
 * Constants of the SQLite bindings: SQLite 3.46.1.
 *
 * Generated from sqlite3.h by bindings/sqlite/generate.py; run it again rather than changing this file.
 */
use "sqlite3.h"

// The version of sqlite3.h (sqlite3_libversion() is that of the library)
declare val SQLITE_SOURCE_ID as CString
declare val SQLITE_VERSION as CString
declare val SQLITE_VERSION_NUMBER as Int

// Primary result codes: what most functions return (sqlite3_errstr says what each means)
declare val SQLITE_ABORT as Int
declare val SQLITE_AUTH as Int
declare val SQLITE_BUSY as Int
declare val SQLITE_CANTOPEN as Int
declare val SQLITE_CONSTRAINT as Int
declare val SQLITE_CORRUPT as Int
declare val SQLITE_DONE as Int
declare val SQLITE_EMPTY as Int
declare val SQLITE_ERROR as Int
declare val SQLITE_FORMAT as Int
declare val SQLITE_FULL as Int
declare val SQLITE_INTERNAL as Int
declare val SQLITE_INTERRUPT as Int
declare val SQLITE_IOERR as Int
declare val SQLITE_LOCKED as Int
declare val SQLITE_MISMATCH as Int
declare val SQLITE_MISUSE as Int
declare val SQLITE_NOLFS as Int
declare val SQLITE_NOMEM as Int
declare val SQLITE_NOTADB as Int
declare val SQLITE_NOTFOUND as Int
declare val SQLITE_NOTICE as Int
declare val SQLITE_OK as Int
declare val SQLITE_PERM as Int
declare val SQLITE_PROTOCOL as Int
declare val SQLITE_RANGE as Int
declare val SQLITE_READONLY as Int
declare val SQLITE_ROW as Int
declare val SQLITE_SCHEMA as Int
declare val SQLITE_TOOBIG as Int
declare val SQLITE_WARNING as Int

// Extended result codes, the primary code | (n << 8): sqlite3_extended_errcode, or every result after sqlite3_extended_result_codes(db, 1)
declare val SQLITE_ABORT_ROLLBACK as Int
declare val SQLITE_AUTH_USER as Int
declare val SQLITE_BUSY_RECOVERY as Int
declare val SQLITE_BUSY_SNAPSHOT as Int
declare val SQLITE_BUSY_TIMEOUT as Int
declare val SQLITE_CANTOPEN_CONVPATH as Int
declare val SQLITE_CANTOPEN_DIRTYWAL as Int
declare val SQLITE_CANTOPEN_FULLPATH as Int
declare val SQLITE_CANTOPEN_ISDIR as Int
declare val SQLITE_CANTOPEN_NOTEMPDIR as Int
declare val SQLITE_CANTOPEN_SYMLINK as Int
declare val SQLITE_CONSTRAINT_CHECK as Int
declare val SQLITE_CONSTRAINT_COMMITHOOK as Int
declare val SQLITE_CONSTRAINT_DATATYPE as Int
declare val SQLITE_CONSTRAINT_FOREIGNKEY as Int
declare val SQLITE_CONSTRAINT_FUNCTION as Int
declare val SQLITE_CONSTRAINT_NOTNULL as Int
declare val SQLITE_CONSTRAINT_PINNED as Int
declare val SQLITE_CONSTRAINT_PRIMARYKEY as Int
declare val SQLITE_CONSTRAINT_ROWID as Int
declare val SQLITE_CONSTRAINT_TRIGGER as Int
declare val SQLITE_CONSTRAINT_UNIQUE as Int
declare val SQLITE_CONSTRAINT_VTAB as Int
declare val SQLITE_CORRUPT_INDEX as Int
declare val SQLITE_CORRUPT_SEQUENCE as Int
declare val SQLITE_CORRUPT_VTAB as Int
declare val SQLITE_ERROR_MISSING_COLLSEQ as Int
declare val SQLITE_ERROR_RETRY as Int
declare val SQLITE_ERROR_SNAPSHOT as Int
declare val SQLITE_IOERR_ACCESS as Int
declare val SQLITE_IOERR_AUTH as Int
declare val SQLITE_IOERR_BEGIN_ATOMIC as Int
declare val SQLITE_IOERR_BLOCKED as Int
declare val SQLITE_IOERR_CHECKRESERVEDLOCK as Int
declare val SQLITE_IOERR_CLOSE as Int
declare val SQLITE_IOERR_COMMIT_ATOMIC as Int
declare val SQLITE_IOERR_CONVPATH as Int
declare val SQLITE_IOERR_CORRUPTFS as Int
declare val SQLITE_IOERR_DATA as Int
declare val SQLITE_IOERR_DELETE as Int
declare val SQLITE_IOERR_DELETE_NOENT as Int
declare val SQLITE_IOERR_DIR_CLOSE as Int
declare val SQLITE_IOERR_DIR_FSYNC as Int
declare val SQLITE_IOERR_FSTAT as Int
declare val SQLITE_IOERR_FSYNC as Int
declare val SQLITE_IOERR_GETTEMPPATH as Int
declare val SQLITE_IOERR_IN_PAGE as Int
declare val SQLITE_IOERR_LOCK as Int
declare val SQLITE_IOERR_MMAP as Int
declare val SQLITE_IOERR_NOMEM as Int
declare val SQLITE_IOERR_RDLOCK as Int
declare val SQLITE_IOERR_READ as Int
declare val SQLITE_IOERR_ROLLBACK_ATOMIC as Int
declare val SQLITE_IOERR_SEEK as Int
declare val SQLITE_IOERR_SHMLOCK as Int
declare val SQLITE_IOERR_SHMMAP as Int
declare val SQLITE_IOERR_SHMOPEN as Int
declare val SQLITE_IOERR_SHMSIZE as Int
declare val SQLITE_IOERR_SHORT_READ as Int
declare val SQLITE_IOERR_TRUNCATE as Int
declare val SQLITE_IOERR_UNLOCK as Int
declare val SQLITE_IOERR_VNODE as Int
declare val SQLITE_IOERR_WRITE as Int
declare val SQLITE_LOCKED_SHAREDCACHE as Int
declare val SQLITE_LOCKED_VTAB as Int
declare val SQLITE_NOTICE_RBU as Int
declare val SQLITE_NOTICE_RECOVER_ROLLBACK as Int
declare val SQLITE_NOTICE_RECOVER_WAL as Int
declare val SQLITE_OK_LOAD_PERMANENTLY as Int
declare val SQLITE_OK_SYMLINK as Int
declare val SQLITE_READONLY_CANTINIT as Int
declare val SQLITE_READONLY_CANTLOCK as Int
declare val SQLITE_READONLY_DBMOVED as Int
declare val SQLITE_READONLY_DIRECTORY as Int
declare val SQLITE_READONLY_RECOVERY as Int
declare val SQLITE_READONLY_ROLLBACK as Int
declare val SQLITE_WARNING_AUTOINDEX as Int

// Flags of sqlite3_open_v2
declare val SQLITE_OPEN_AUTOPROXY as Int
declare val SQLITE_OPEN_CREATE as Int
declare val SQLITE_OPEN_DELETEONCLOSE as Int
declare val SQLITE_OPEN_EXCLUSIVE as Int
declare val SQLITE_OPEN_EXRESCODE as Int
declare val SQLITE_OPEN_FULLMUTEX as Int
declare val SQLITE_OPEN_MAIN_DB as Int
declare val SQLITE_OPEN_MAIN_JOURNAL as Int
declare val SQLITE_OPEN_MASTER_JOURNAL as Int
declare val SQLITE_OPEN_MEMORY as Int
declare val SQLITE_OPEN_NOFOLLOW as Int
declare val SQLITE_OPEN_NOMUTEX as Int
declare val SQLITE_OPEN_PRIVATECACHE as Int
declare val SQLITE_OPEN_READONLY as Int
declare val SQLITE_OPEN_READWRITE as Int
declare val SQLITE_OPEN_SHAREDCACHE as Int
declare val SQLITE_OPEN_SUBJOURNAL as Int
declare val SQLITE_OPEN_SUPER_JOURNAL as Int
declare val SQLITE_OPEN_TEMP_DB as Int
declare val SQLITE_OPEN_TEMP_JOURNAL as Int
declare val SQLITE_OPEN_TRANSIENT_DB as Int
declare val SQLITE_OPEN_URI as Int
declare val SQLITE_OPEN_WAL as Int

// Flags of sqlite3_prepare_v3
declare val SQLITE_PREPARE_NORMALIZE as Int
declare val SQLITE_PREPARE_NO_VTAB as Int
declare val SQLITE_PREPARE_PERSISTENT as Int

// Types of values: sqlite3_column_type, sqlite3_value_type
declare val SQLITE3_TEXT as Int
declare val SQLITE_BLOB as Int
declare val SQLITE_FLOAT as Int
declare val SQLITE_INTEGER as Int
declare val SQLITE_NULL as Int
declare val SQLITE_TEXT as Int

// Text encodings
declare val SQLITE_ANY as Int
declare val SQLITE_UTF16 as Int
declare val SQLITE_UTF16BE as Int
declare val SQLITE_UTF16LE as Int
declare val SQLITE_UTF16_ALIGNED as Int
declare val SQLITE_UTF8 as Int

// Flags of sqlite3_create_function_v2, combined with a text encoding
declare val SQLITE_DETERMINISTIC as Int
declare val SQLITE_DIRECTONLY as Int
declare val SQLITE_INNOCUOUS as Int
declare val SQLITE_RESULT_SUBTYPE as Int
declare val SQLITE_SUBTYPE as Int

// Options of sqlite3_config
declare val SQLITE_CONFIG_COVERING_INDEX_SCAN as Int
declare val SQLITE_CONFIG_GETMALLOC as Int
declare val SQLITE_CONFIG_GETMUTEX as Int
declare val SQLITE_CONFIG_GETPCACHE as Int
declare val SQLITE_CONFIG_GETPCACHE2 as Int
declare val SQLITE_CONFIG_HEAP as Int
declare val SQLITE_CONFIG_LOG as Int
declare val SQLITE_CONFIG_LOOKASIDE as Int
declare val SQLITE_CONFIG_MALLOC as Int
declare val SQLITE_CONFIG_MEMDB_MAXSIZE as Int
declare val SQLITE_CONFIG_MEMSTATUS as Int
declare val SQLITE_CONFIG_MMAP_SIZE as Int
declare val SQLITE_CONFIG_MULTITHREAD as Int
declare val SQLITE_CONFIG_MUTEX as Int
declare val SQLITE_CONFIG_PAGECACHE as Int
declare val SQLITE_CONFIG_PCACHE as Int
declare val SQLITE_CONFIG_PCACHE2 as Int
declare val SQLITE_CONFIG_PCACHE_HDRSZ as Int
declare val SQLITE_CONFIG_PMASZ as Int
declare val SQLITE_CONFIG_ROWID_IN_VIEW as Int
declare val SQLITE_CONFIG_SCRATCH as Int
declare val SQLITE_CONFIG_SERIALIZED as Int
declare val SQLITE_CONFIG_SINGLETHREAD as Int
declare val SQLITE_CONFIG_SMALL_MALLOC as Int
declare val SQLITE_CONFIG_SORTERREF_SIZE as Int
declare val SQLITE_CONFIG_SQLLOG as Int
declare val SQLITE_CONFIG_STMTJRNL_SPILL as Int
declare val SQLITE_CONFIG_URI as Int
declare val SQLITE_CONFIG_WIN32_HEAPSIZE as Int

// Options of sqlite3_db_config
declare val SQLITE_DBCONFIG_DEFENSIVE as Int
declare val SQLITE_DBCONFIG_DQS_DDL as Int
declare val SQLITE_DBCONFIG_DQS_DML as Int
declare val SQLITE_DBCONFIG_ENABLE_FKEY as Int
declare val SQLITE_DBCONFIG_ENABLE_FTS3_TOKENIZER as Int
declare val SQLITE_DBCONFIG_ENABLE_LOAD_EXTENSION as Int
declare val SQLITE_DBCONFIG_ENABLE_QPSG as Int
declare val SQLITE_DBCONFIG_ENABLE_TRIGGER as Int
declare val SQLITE_DBCONFIG_ENABLE_VIEW as Int
declare val SQLITE_DBCONFIG_LEGACY_ALTER_TABLE as Int
declare val SQLITE_DBCONFIG_LEGACY_FILE_FORMAT as Int
declare val SQLITE_DBCONFIG_LOOKASIDE as Int
declare val SQLITE_DBCONFIG_MAINDBNAME as Int
declare val SQLITE_DBCONFIG_MAX as Int
declare val SQLITE_DBCONFIG_NO_CKPT_ON_CLOSE as Int
declare val SQLITE_DBCONFIG_RESET_DATABASE as Int
declare val SQLITE_DBCONFIG_REVERSE_SCANORDER as Int
declare val SQLITE_DBCONFIG_STMT_SCANSTATUS as Int
declare val SQLITE_DBCONFIG_TRIGGER_EQP as Int
declare val SQLITE_DBCONFIG_TRUSTED_SCHEMA as Int
declare val SQLITE_DBCONFIG_WRITABLE_SCHEMA as Int

// Limits of sqlite3_limit
declare val SQLITE_LIMIT_ATTACHED as Int
declare val SQLITE_LIMIT_COLUMN as Int
declare val SQLITE_LIMIT_COMPOUND_SELECT as Int
declare val SQLITE_LIMIT_EXPR_DEPTH as Int
declare val SQLITE_LIMIT_FUNCTION_ARG as Int
declare val SQLITE_LIMIT_LENGTH as Int
declare val SQLITE_LIMIT_LIKE_PATTERN_LENGTH as Int
declare val SQLITE_LIMIT_SQL_LENGTH as Int
declare val SQLITE_LIMIT_TRIGGER_DEPTH as Int
declare val SQLITE_LIMIT_VARIABLE_NUMBER as Int
declare val SQLITE_LIMIT_VDBE_OP as Int
declare val SQLITE_LIMIT_WORKER_THREADS as Int

// Counters of sqlite3_status64
declare val SQLITE_STATUS_MALLOC_COUNT as Int
declare val SQLITE_STATUS_MALLOC_SIZE as Int
declare val SQLITE_STATUS_MEMORY_USED as Int
declare val SQLITE_STATUS_PAGECACHE_OVERFLOW as Int
declare val SQLITE_STATUS_PAGECACHE_SIZE as Int
declare val SQLITE_STATUS_PAGECACHE_USED as Int
declare val SQLITE_STATUS_PARSER_STACK as Int
declare val SQLITE_STATUS_SCRATCH_OVERFLOW as Int
declare val SQLITE_STATUS_SCRATCH_SIZE as Int
declare val SQLITE_STATUS_SCRATCH_USED as Int

// Counters of sqlite3_db_status
declare val SQLITE_DBSTATUS_CACHE_HIT as Int
declare val SQLITE_DBSTATUS_CACHE_MISS as Int
declare val SQLITE_DBSTATUS_CACHE_SPILL as Int
declare val SQLITE_DBSTATUS_CACHE_USED as Int
declare val SQLITE_DBSTATUS_CACHE_USED_SHARED as Int
declare val SQLITE_DBSTATUS_CACHE_WRITE as Int
declare val SQLITE_DBSTATUS_DEFERRED_FKS as Int
declare val SQLITE_DBSTATUS_LOOKASIDE_HIT as Int
declare val SQLITE_DBSTATUS_LOOKASIDE_MISS_FULL as Int
declare val SQLITE_DBSTATUS_LOOKASIDE_MISS_SIZE as Int
declare val SQLITE_DBSTATUS_LOOKASIDE_USED as Int
declare val SQLITE_DBSTATUS_MAX as Int
declare val SQLITE_DBSTATUS_SCHEMA_USED as Int
declare val SQLITE_DBSTATUS_STMT_USED as Int

// Counters of sqlite3_stmt_status
declare val SQLITE_STMTSTATUS_AUTOINDEX as Int
declare val SQLITE_STMTSTATUS_FILTER_HIT as Int
declare val SQLITE_STMTSTATUS_FILTER_MISS as Int
declare val SQLITE_STMTSTATUS_FULLSCAN_STEP as Int
declare val SQLITE_STMTSTATUS_MEMUSED as Int
declare val SQLITE_STMTSTATUS_REPREPARE as Int
declare val SQLITE_STMTSTATUS_RUN as Int
declare val SQLITE_STMTSTATUS_SORT as Int
declare val SQLITE_STMTSTATUS_VM_STEP as Int

// Modes of sqlite3_wal_checkpoint_v2
declare val SQLITE_CHECKPOINT_FULL as Int
declare val SQLITE_CHECKPOINT_PASSIVE as Int
declare val SQLITE_CHECKPOINT_RESTART as Int
declare val SQLITE_CHECKPOINT_TRUNCATE as Int

// Results of sqlite3_txn_state
declare val SQLITE_TXN_NONE as Int
declare val SQLITE_TXN_READ as Int
declare val SQLITE_TXN_WRITE as Int

// Events of sqlite3_trace_v2
declare val SQLITE_TRACE_CLOSE as Int
declare val SQLITE_TRACE_PROFILE as Int
declare val SQLITE_TRACE_ROW as Int
declare val SQLITE_TRACE_STMT as Int

// Flags of sqlite3_serialize
declare val SQLITE_SERIALIZE_NOCOPY as Int

// Flags of sqlite3_deserialize
declare val SQLITE_DESERIALIZE_FREEONCLOSE as Int
declare val SQLITE_DESERIALIZE_READONLY as Int
declare val SQLITE_DESERIALIZE_RESIZEABLE as Int
