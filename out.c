#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>
#include <limits.h>
#include <stdalign.h>
#include <apr_general.h>
#include <apr_pools.h>

// Uses
#include <stdarg.h>
#include <apr.h>
// use core:stdarg.ec
#include <sys/uio.h>
#include <apr_errno.h>
// use core:apr/apr.ec
// use core:apr/errno.ec
// use core:c/stdio.ec
// use core:c/stdlib.ec
#include <apr_want.h>
#include <apr_thread_mutex.h>
// use core:apr/pools.ec
#include <apr_allocator.h>
// use core:apr/thread_mutex.ec
// use core:apr/general.ec
// use core:apr/want.ec
// use core:apr/allocator.ec
#include <apr_strings.h>
// use core:errors.ec
#include <float.h>
// use core:c/string.ec
// use core:apr/strings.ec
// use core:math.ec
#include <time.h>
#include <apr_time.h>
// use core:c/time.ec
// use core:apr/time.ec
#include <apr_user.h>
#include <apr_tables.h>
#include <apr_file_info.h>
// use core:apr/user.ec
// use core:apr/tables.ec
#include <apr_inherit.h>
#include <apr_file_io.h>
// use core:apr/file_info.ec
// use core:apr/inherit.ec
// use core:string.ec
// use core:time.ec
// use core:apr/file_io.ec
// use core:io.ec

// Runtime
typedef struct { size_t len; char* data; } __ec_string_t;
typedef struct { int line; int position; char* path; } __ec_location_t;
_Noreturn static inline void __ec_panic(const char* at, const char* message) { fflush(stdout); fprintf(stderr, "%s: %s\n", at, message); abort(); }
static inline void* __ec_nn(void* p, const char* at) { if (p == NULL) __ec_panic(at, "null value used where a value was required"); return p; }
static inline void* __ec_c_nn(void* p, const char* at, const char* fn) { if (p == NULL) { fflush(stdout); fprintf(stderr, "%s: the C function %s returned NULL, but its `declare fn` says it never does. If it can, declare its result nullable (`: T?`) and check it.\n", at, fn); abort(); } return p; }
static inline bool __ec_str_eq(__ec_string_t a, __ec_string_t b) { return a.len == b.len && memcmp(a.data, b.data, a.len) == 0; }
static int __ec_argc; static char** __ec_argv; /* the command line, for io.args in core:io.ec */
static _Thread_local apr_pool_t* __ec_pool; /* the current pool (`pool`): the root pool, or the innermost `pool do` block's */
static int __ec_out_of_memory(int status) { (void)status; __ec_panic("eclang", "out of memory"); }
static inline apr_pool_t* __ec_pool_create(apr_pool_t* parent) { apr_pool_t* p = NULL; if (apr_pool_create_ex(&p, parent, __ec_out_of_memory, NULL) != APR_SUCCESS) __ec_panic("eclang", "can't create a pool"); return p; }
static inline void* __ec_palloc(apr_pool_t* pool, size_t size) { if (pool == NULL) __ec_panic("eclang", "this thread has no current pool: start its code with a `pool do` block"); return apr_palloc(pool, size); }
static inline char* __ec_str_cstr(apr_pool_t* pool, __ec_string_t s) { char* c = __ec_palloc(pool, s.len + 1); if (s.len) memcpy(c, s.data, s.len); c[s.len] = 0; return c; }
static inline __ec_string_t __ec_str_concat(apr_pool_t* pool, __ec_string_t a, __ec_string_t b) { char* s = __ec_palloc(pool, a.len + b.len + 1); if (a.len) memcpy(s, a.data, a.len); if (b.len) memcpy(s + a.len, b.data, b.len); s[a.len + b.len] = 0; return (__ec_string_t){a.len + b.len, s}; }
_Noreturn static inline void __ec_panic_index(const char* at, size_t i, size_t len) { fflush(stdout); fprintf(stderr, "%s: index %lld is out of bounds for length %zu\n", at, (long long)i, len); abort(); }
static inline size_t __ec_index(size_t i, size_t len, const char* at) { if (i >= len) __ec_panic_index(at, i, len); return i; }
static inline void* __ec_palloc_aligned(apr_pool_t* pool, size_t size, size_t align) { uintptr_t p = (uintptr_t)__ec_palloc(pool, size + align - 1); return (void*)((p + align - 1) & ~(uintptr_t)(align - 1)); }
_Noreturn static inline void __ec_panic_slice(const char* at, size_t from, size_t to, size_t len) { fflush(stdout); fprintf(stderr, "%s: slice %lld..<%lld is out of bounds for length %zu\n", at, (long long)from, (long long)to, len); abort(); }
// Types
typedef struct math__Random math__Random;
typedef struct __ec_array_AString __ec_array_AString;

struct math__Random {
	unsigned long state;
	unsigned long increment;
};
struct __ec_array_AString { size_t len; __ec_string_t* data; };

// Atoms
static char* const __ec_atom_read = "read";
static char* const __ec_atom_write = "write";
static char* const __ec_atom_append = "append";
static char* const __ec_atom_update = "update";

// Helpers

// Declarations
void io__log_6(int value);
int fib__main_0(void);

// Globals
#define ERR_FAILED (1)
#define ERR_PARSE (2)
#define ERR_RANGE (3)
#define ERR_ARGUMENT (4)
#define ERR_NOT_FOUND (5)
#define ERR_EXISTS (6)
#define ERR_PERMISSION (7)
#define ERR_STATE (8)
#define ERR_TIMEOUT (9)
#define ERR_UNSUPPORTED (10)
#define ERR_IO (11)
#define ERR_MEMORY (12)
#define math__PI (3.141592653589793)
#define math__TAU (6.283185307179586)
#define math__E (2.718281828459045)
#define math__SQRT2 (1.4142135623730951)
#define math__LN2 (0.6931471805599453)
#define math__LN10 (2.302585092994046)
#define math__PHI (1.618033988749895)
#define math__INT_MIN (-2147483648)
#define math__INT_MAX (2147483647)
#define math__LONG_MIN ((-9223372036854775807L - 1))
#define math__LONG_MAX (9223372036854775807L)
#define math__PCG_MULTIPLIER (6364136223846793005L)
#define math__defaultSeed (853146490016488653L)
#define time__MICROS_PER_MILLI (1000)
#define time__MICROS_PER_SECOND (1000000)
#define time__MICROS_PER_MINUTE (60000000)
#define time__MICROS_PER_HOUR (3600000000L)
#define time__MICROS_PER_DAY (86400000000L)
// declare val APR_SIZE_MAX
// declare val APR_INT64_MAX
// declare val APR_SUCCESS
// declare val APR_EGENERAL
// declare val APR_ENOMEM
// declare val APR_EINVAL
// declare val APR_ENOENT
// declare val APR_EEXIST
// declare val APR_EAGAIN
// declare val APR_TIMEUP
// declare val APR_INCOMPLETE
// declare val APR_EOF
// declare val APR_NOTFOUND
// declare val APR_EACCES
// declare val APR_EBUSY
// declare val APR_ENOTIMPL
// declare val APR_ETIMEDOUT
// declare val stdin
// declare val stdout
// declare val stderr
// declare val EOF
// declare val BUFSIZ
// declare val EXIT_SUCCESS
// declare val EXIT_FAILURE
// declare val RAND_MAX
// declare val APR_ASCII_BLANK
// declare val APR_ASCII_CR
// declare val APR_ASCII_LF
// declare val APR_ASCII_TAB
// declare val APR_THREAD_MUTEX_DEFAULT
// declare val APR_THREAD_MUTEX_NESTED
// declare val APR_THREAD_MUTEX_UNNESTED
// declare val APR_MEMNODE_T_SIZE
// declare val APR_ALLOCATOR_MAX_FREE_UNLIMITED
// declare val APR_POOL_DEBUG
// module math
// declare val INFINITY
// declare val NAN
// declare val DBL_EPSILON
// declare val DBL_MAX
// declare val DBL_MIN
// declare val CLOCKS_PER_SEC
// declare val CLOCK_REALTIME
// declare val CLOCK_MONOTONIC
// declare val CLOCK_PROCESS_CPUTIME_ID
// declare val apr_month_snames
// declare val apr_day_snames
// module time
// declare val APR_OVERLAP_TABLES_SET
// declare val APR_OVERLAP_TABLES_MERGE
// declare val APR_FPROT_USETID
// declare val APR_FPROT_UREAD
// declare val APR_FPROT_UWRITE
// declare val APR_FPROT_UEXECUTE
// declare val APR_FPROT_GSETID
// declare val APR_FPROT_GREAD
// declare val APR_FPROT_GWRITE
// declare val APR_FPROT_GEXECUTE
// declare val APR_FPROT_WSTICKY
// declare val APR_FPROT_WREAD
// declare val APR_FPROT_WWRITE
// declare val APR_FPROT_WEXECUTE
// declare val APR_FPROT_OS_DEFAULT
// declare val APR_FPROT_FILE_SOURCE_PERMS
// declare val APR_USETID
// declare val APR_UREAD
// declare val APR_UWRITE
// declare val APR_UEXECUTE
// declare val APR_GSETID
// declare val APR_GREAD
// declare val APR_GWRITE
// declare val APR_GEXECUTE
// declare val APR_WSTICKY
// declare val APR_WREAD
// declare val APR_WWRITE
// declare val APR_WEXECUTE
// declare val APR_OS_DEFAULT
// declare val APR_FILE_SOURCE_PERMS
// declare val APR_FINFO_LINK
// declare val APR_FINFO_MTIME
// declare val APR_FINFO_CTIME
// declare val APR_FINFO_ATIME
// declare val APR_FINFO_SIZE
// declare val APR_FINFO_CSIZE
// declare val APR_FINFO_DEV
// declare val APR_FINFO_INODE
// declare val APR_FINFO_NLINK
// declare val APR_FINFO_TYPE
// declare val APR_FINFO_USER
// declare val APR_FINFO_GROUP
// declare val APR_FINFO_UPROT
// declare val APR_FINFO_GPROT
// declare val APR_FINFO_WPROT
// declare val APR_FINFO_ICASE
// declare val APR_FINFO_NAME
// declare val APR_FINFO_MIN
// declare val APR_FINFO_IDENT
// declare val APR_FINFO_OWNER
// declare val APR_FINFO_PROT
// declare val APR_FINFO_NORM
// declare val APR_FINFO_DIRENT
// declare val APR_FILEPATH_NOTABOVEROOT
// declare val APR_FILEPATH_SECUREROOTTEST
// declare val APR_FILEPATH_SECUREROOT
// declare val APR_FILEPATH_NOTRELATIVE
// declare val APR_FILEPATH_NOTABSOLUTE
// declare val APR_FILEPATH_NATIVE
// declare val APR_FILEPATH_TRUENAME
// declare val APR_FILEPATH_ENCODING_UNKNOWN
// declare val APR_FILEPATH_ENCODING_LOCALE
// declare val APR_FILEPATH_ENCODING_UTF8
// declare val APR_NOFILE
// declare val APR_REG
// declare val APR_DIR
// declare val APR_CHR
// declare val APR_BLK
// declare val APR_PIPE
// declare val APR_LNK
// declare val APR_SOCK
// declare val APR_UNKFILE
// declare val APR_WANT_STDI
// declare val APR_WANT_IOVE
// declare val APR_FOPEN_READ
// declare val APR_FOPEN_WRITE
// declare val APR_FOPEN_CREATE
// declare val APR_FOPEN_APPEND
// declare val APR_FOPEN_TRUNCATE
// declare val APR_FOPEN_BINARY
// declare val APR_FOPEN_EXCL
// declare val APR_FOPEN_BUFFERED
// declare val APR_FOPEN_DELONCLOSE
// declare val APR_FOPEN_XTHREAD
// declare val APR_FOPEN_SHARELOCK
// declare val APR_FOPEN_NOCLEANUP
// declare val APR_FOPEN_SENDFILE_ENABLED
// declare val APR_FOPEN_LARGEFILE
// declare val APR_FOPEN_SPARSE
// declare val APR_READ
// declare val APR_WRITE
// declare val APR_CREATE
// declare val APR_APPEND
// declare val APR_TRUNCATE
// declare val APR_BINARY
// declare val APR_EXCL
// declare val APR_BUFFERED
// declare val APR_DELONCLOSE
// declare val APR_XTHREAD
// declare val APR_SHARELOCK
// declare val APR_FILE_NOCLEANUP
// declare val APR_SENDFILE_ENABLED
// declare val APR_LARGEFILE
// declare val APR_SET
// declare val APR_CUR
// declare val APR_END
// declare val APR_FILE_ATTR_READONLY
// declare val APR_FILE_ATTR_EXECUTABLE
// declare val APR_FILE_ATTR_HIDDEN
// declare val APR_MAX_IOVEC_SIZE
// declare val APR_FLOCK_SHARED
// declare val APR_FLOCK_EXCLUSIVE
// declare val APR_FLOCK_TYPEMASK
// declare val APR_FLOCK_NONBLOCK
// module io
// declare val commandLineLength
// declare val commandLine
// module fib

// Code
void io__log_6(int value) {
	printf("%d\n", value);
}

int fib__main_0(void) {
	int a = 0;
	int b = 1;
	for (int i=0;i<40;i++) {
		io__log_6(a);
		int next = a+b;
		a = b;
		b = next;
	}
	return 0;
}

// Main Entry
int main(int argc, char** argv) {
	__ec_argc = argc;
	__ec_argv = argv;
	apr_initialize();
	__ec_pool = __ec_pool_create(NULL);
	int __ec_ret;
	__ec_ret = fib__main_0();
	apr_pool_destroy(__ec_pool);
	apr_terminate();
	return __ec_ret;
}
