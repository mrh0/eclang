#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>
#include <limits.h>
#include <stdalign.h>
#include <signal.h>
#include <sys/resource.h>
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
#include <unistd.h>
#include <termios.h>
#include <sys/ioctl.h>
#include <apr_env.h>
#include <apr_network_io.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <apr_poll.h>
// use core:apr/network_io.ec
#include <apr_signal.h>
// use core:unicode.ec
// use core:c/unistd.ec
// use core:c/termios.ec
// use core:c/ioctl.ec
// use core:c/signal.ec
// use core:apr/env.ec
// use core:apr/poll.ec
// use core:apr/signal.ec
// use core:io.ec
// use core:term.ec

// Runtime
typedef struct { size_t len; char* data; } __ec_string_t;
typedef struct { int line; int position; char* path; } __ec_location_t;
_Noreturn static inline void __ec_panic(const char* at, const char* message) { fflush(stdout); fprintf(stderr, "%s: %s\n", at, message); abort(); }
static inline void* __ec_nn(void* p, const char* at) { if (p == NULL) __ec_panic(at, "null value used where a value was required"); return p; }
static inline void* __ec_c_nn(void* p, const char* at, const char* fn) { if (p == NULL) { fflush(stdout); fprintf(stderr, "%s: the C function %s returned NULL, but its `declare fn` says it never does. If it can, declare its result nullable (`: T?`) and check it.\n", at, fn); abort(); } return p; }
static inline bool __ec_str_eq(__ec_string_t a, __ec_string_t b) { return a.len == b.len && memcmp(a.data, b.data, a.len) == 0; }
static int __ec_argc; static char** __ec_argv; /* the command line, for io.args in core:io.ec */
extern char** __ec_environ __asm__("environ"); /* the environment, for os.environment in core:os.ec (C headers only declare `environ` with _GNU_SOURCE) */
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
extern long __ec_write(int file, const void* data, size_t size) __asm__("write");
static uintptr_t __ec_stack_low; /* a stack pointer below this is at the end of the main thread's stack */
static uintptr_t __ec_stack_pointer(void* context) {
#if defined(__linux__) && defined(__x86_64__)
    return (uintptr_t)((ucontext_t*)context)->uc_mcontext.gregs[15];
#elif defined(__linux__) && defined(__aarch64__)
    return (uintptr_t)((ucontext_t*)context)->uc_mcontext.sp;
#else
    (void)context; return UINTPTR_MAX;
#endif
}
static void __ec_on_fault(int sig, siginfo_t* info, void* context) {
    uintptr_t at = (uintptr_t)info->si_addr, sp = __ec_stack_pointer(context), near = 1 << 16;
    if (sp < __ec_stack_low && at + near > sp && at < sp + near) {
        if (ftrylockfile(stdout) == 0) { fflush(stdout); funlockfile(stdout); }
        static const char message[] = "eclang: out of stack: the calls went too deep, or a local variable is too large. Check for recursion that never ends, and put large arrays in a pool with `new`.\n";
        (void)__ec_write(2, message, sizeof message - 1);
        abort();
    }
    signal(sig, SIG_DFL);
}
static void __ec_watch_stack(void) {
    static char stack[1 << 16];
    struct rlimit limit;
    char here;
    if (getrlimit(RLIMIT_STACK, &limit) != 0 || limit.rlim_cur == RLIM_INFINITY || limit.rlim_cur > (uintptr_t)&here) return;
    __ec_stack_low = (uintptr_t)&here - limit.rlim_cur + limit.rlim_cur / 4 + (1 << 16);
    stack_t alternate = { .ss_sp = stack, .ss_size = sizeof stack, .ss_flags = 0 };
    if (sigaltstack(&alternate, NULL) != 0) return;
    struct sigaction action;
    memset(&action, 0, sizeof action);
    action.sa_sigaction = __ec_on_fault;
    action.sa_flags = SA_SIGINFO | SA_ONSTACK;
    sigemptyset(&action.sa_mask);
    sigaction(SIGSEGV, &action, NULL);
    sigaction(SIGBUS, &action, NULL);
}
// Types
typedef struct __ec_array_AChar __ec_array_AChar;
typedef struct IoError IoError;
typedef struct __ec_result_Long_Error __ec_result_Long_Error;
typedef struct Error Error;
typedef struct StringBuilder StringBuilder;
typedef struct __ec_tuple_UInt_Size __ec_tuple_UInt_Size;
typedef struct term__Dimensions term__Dimensions;
typedef struct __ec_result_None_IoError __ec_result_None_IoError;
typedef struct term__Key term__Key;
typedef struct term__Decoded term__Decoded;
typedef struct __ec_union_t234 __ec_union_t234;
typedef struct __ec_union_t216 __ec_union_t216;
typedef struct term__Screen term__Screen;
typedef struct __ec_array_Aterm__Cell __ec_array_Aterm__Cell;
typedef struct term__Cell term__Cell;
typedef struct term__Style term__Style;
typedef struct __ec_union_Null_String __ec_union_Null_String;
typedef struct term__Color term__Color;
typedef struct term__Mouse term__Mouse;
typedef struct term__Resize term__Resize;
typedef struct term__Eof term__Eof;
typedef struct __ec_array_ATask __ec_array_ATask;
typedef struct __ec_result_Int_Error __ec_result_Int_Error;
typedef struct Task Task;
typedef struct __ec_array_AInt __ec_array_AInt;
typedef struct math__Random math__Random;
typedef struct __ec_array_AString __ec_array_AString;
typedef struct __ec_union_term__Mouse_term__Resize_term__Eof __ec_union_term__Mouse_term__Resize_term__Eof;
typedef struct __ec_union_term__Resize_term__Eof __ec_union_term__Resize_term__Eof;

struct __ec_array_AChar { size_t len; char* data; };
struct IoError {
	int __ec_tid;
	int code;
	__ec_string_t message;
	int status;
	__ec_string_t path;
};
struct Error {
	int __ec_tid;
	int code;
	__ec_string_t message;
};
struct __ec_result_Long_Error { int code; union { long value; Error error; }; }; /* Long throws Error */
struct StringBuilder {
	__ec_array_AChar buffer;
	size_t len;
	apr_pool_t* mem;
};
struct __ec_tuple_UInt_Size { unsigned int first; size_t second; };
struct term__Dimensions {
	int cols;
	int rows;
};
struct __ec_result_None_IoError { int code; union { IoError error; }; }; /* None throws IoError */
struct term__Key {
	char* name;
	unsigned int ch;
	bool ctrl;
	bool alt;
	bool shift;
};
struct term__Mouse {
	char* action;
	char* button;
	int col;
	int row;
	bool ctrl;
	bool alt;
	bool shift;
};
struct term__Resize {
	term__Dimensions size;
};
struct term__Eof {
	bool ended;
};
struct __ec_union_t234 { int tag; union { term__Key m1; term__Mouse m2; term__Resize m3; term__Eof m4; } v; }; /* Null | Key | Mouse | Resize | Eof */
struct term__Decoded {
	__ec_union_t234 event;
	size_t used;
};
struct __ec_union_t216 { int tag; union { term__Key m0; term__Mouse m1; term__Resize m2; term__Eof m3; } v; }; /* Key | Mouse | Resize | Eof */
struct __ec_array_Aterm__Cell { size_t len; term__Cell* data; };
struct term__Screen {
	int cols;
	int rows;
	__ec_array_Aterm__Cell cells;
	__ec_array_Aterm__Cell shown;
	bool stale;
	int cursorCol;
	int cursorRow;
	bool cursorShown;
};
struct term__Color {
	int code;
};
struct term__Style {
	term__Color fg;
	term__Color bg;
	bool bold;
	bool dim;
	bool italic;
	bool underline;
	bool reverse;
	bool strike;
};
struct term__Cell {
	unsigned int ch;
	term__Style style;
};
struct __ec_union_Null_String { int tag; union { __ec_string_t m1; } v; }; /* String? */
typedef void (*__ec_fn_Int_to_None)(int);
struct __ec_array_ATask { size_t len; Task* data; };
struct __ec_result_Int_Error { int code; union { int value; Error error; }; }; /* Int throws Error */
struct Task {
	__ec_string_t name;
	bool done;
};
struct __ec_array_AInt { size_t len; int* data; };
struct math__Random {
	unsigned long state;
	unsigned long increment;
};
struct __ec_array_AString { size_t len; __ec_string_t* data; };
struct __ec_union_term__Mouse_term__Resize_term__Eof { int tag; union { term__Mouse m0; term__Resize m1; term__Eof m2; } v; }; /* Mouse | Resize | Eof */
struct __ec_union_term__Resize_term__Eof { int tag; union { term__Resize m0; term__Eof m1; } v; }; /* Resize | Eof */

// Atoms
static char* const __ec_atom_read = "read";
static char* const __ec_atom_write = "write";
static char* const __ec_atom_append = "append";
static char* const __ec_atom_update = "update";
static char* const __ec_atom_auto = "auto";
static char* const __ec_atom_always = "always";
static char* const __ec_atom_never = "never";
static char* const __ec_atom_char = "char";
static char* const __ec_atom_enter = "enter";
static char* const __ec_atom_tab = "tab";
static char* const __ec_atom_backspace = "backspace";
static char* const __ec_atom_escape = "escape";
static char* const __ec_atom_up = "up";
static char* const __ec_atom_down = "down";
static char* const __ec_atom_left = "left";
static char* const __ec_atom_right = "right";
static char* const __ec_atom_home = "home";
static char* const __ec_atom_end = "end";
static char* const __ec_atom_pageUp = "pageUp";
static char* const __ec_atom_pageDown = "pageDown";
static char* const __ec_atom_insert = "insert";
static char* const __ec_atom_delete = "delete";
static char* const __ec_atom_f1 = "f1";
static char* const __ec_atom_f2 = "f2";
static char* const __ec_atom_f3 = "f3";
static char* const __ec_atom_f4 = "f4";
static char* const __ec_atom_f5 = "f5";
static char* const __ec_atom_f6 = "f6";
static char* const __ec_atom_f7 = "f7";
static char* const __ec_atom_f8 = "f8";
static char* const __ec_atom_f9 = "f9";
static char* const __ec_atom_f10 = "f10";
static char* const __ec_atom_f11 = "f11";
static char* const __ec_atom_f12 = "f12";
static char* const __ec_atom_press = "press";
static char* const __ec_atom_release = "release";
static char* const __ec_atom_drag = "drag";
static char* const __ec_atom_scrollUp = "scrollUp";
static char* const __ec_atom_scrollDown = "scrollDown";
static char* const __ec_atom_middle = "middle";
static char* const __ec_atom_none = "none";
static char* const __ec_atom_single = "single";
static char* const __ec_atom_double = "double";
static char* const __ec_atom_rounded = "rounded";
static char* const __ec_atom_heavy = "heavy";

// Helpers
static inline __ec_array_AChar __ec_alloc_AChar(apr_pool_t* pool, size_t n);
static __ec_string_t __ec_format_0(apr_pool_t* pool, char* a0);
static __ec_string_t __ec_format_1(apr_pool_t* pool, __ec_string_t a0, __ec_string_t a1);
static inline char* __ec_at_String(__ec_string_t a, size_t i, const char* at);
static inline __ec_string_t __ec_slice_String(__ec_string_t a, size_t from, size_t to, const char* at);
static inline __ec_array_AChar __ec_slice_AChar(__ec_array_AChar a, size_t from, size_t to, const char* at);
static inline __ec_string_t __ec_view_AChar_to_String(__ec_array_AChar a);
static inline StringBuilder* __ec_new_StringBuilder(apr_pool_t* pool, StringBuilder v);
static inline char* __ec_at_AChar(__ec_array_AChar a, size_t i, const char* at);
static __ec_string_t __ec_format_2(apr_pool_t* pool, long a0);
static inline __ec_union_t216 __ec_cv_t234__t216(__ec_union_t234 v, const char* at);
static inline term__Screen* __ec_new_term__Screen(apr_pool_t* pool, term__Screen v);
static inline term__Cell* __ec_at_Aterm__Cell(__ec_array_Aterm__Cell a, size_t i, const char* at);
static __ec_string_t __ec_format_3(apr_pool_t* pool, __ec_string_t a0);
static inline bool __ec_eq_term__Style(term__Style a, term__Style b);
static inline bool __ec_eq_term__Cell(term__Cell a, term__Cell b);
static __ec_string_t __ec_format_4(apr_pool_t* pool, int a0, int a1);
static inline __ec_union_t234 __ec_cv_t216__t234(__ec_union_t216 v, const char* at);
static inline __ec_array_Aterm__Cell __ec_alloc_Aterm__Cell(apr_pool_t* pool, size_t n);
static inline Task* __ec_at_ATask(__ec_array_ATask a, size_t i, const char* at);
static inline Error __ec_up_IoError__Error(IoError v);
static inline __ec_union_term__Mouse_term__Resize_term__Eof __ec_cv_t216__term__Mouse_term__Resize_term__Eof(__ec_union_t216 v, const char* at);
static inline __ec_union_term__Resize_term__Eof __ec_cv_t216__term__Resize_term__Eof(__ec_union_t216 v, const char* at);
static inline bool __ec_eq_term__Color(term__Color a, term__Color b);
static inline __ec_array_AChar __ec_alloc_AChar(apr_pool_t* pool, size_t n) {
	if (n > SIZE_MAX / sizeof(char)) __ec_panic("eclang", "`new T[n]` is too large: n elements don't fit in memory (is n negative?)");
	__ec_array_AChar a = {n, __ec_palloc(pool, n * sizeof(char))};
	for (size_t i = 0; i < n; i++) a.data[i] = 0;
	return a;
}
static __ec_string_t __ec_format_0(apr_pool_t* pool, char* a0) {
	int n = snprintf(NULL, 0, "%s", a0);
	char* s = __ec_palloc(pool, (size_t)n + 1);
	snprintf(s, (size_t)n + 1, "%s", a0);
	return (__ec_string_t){(size_t)n, s};
}
static __ec_string_t __ec_format_1(apr_pool_t* pool, __ec_string_t a0, __ec_string_t a1) {
	int n = snprintf(NULL, 0, "%.*s: %.*s", (int)a0.len, a0.data, (int)a1.len, a1.data);
	char* s = __ec_palloc(pool, (size_t)n + 1);
	snprintf(s, (size_t)n + 1, "%.*s: %.*s", (int)a0.len, a0.data, (int)a1.len, a1.data);
	return (__ec_string_t){(size_t)n, s};
}
static inline char* __ec_at_String(__ec_string_t a, size_t i, const char* at) {
	if (i >= a.len) __ec_panic_index(at, i, a.len);
	return &a.data[i];
}
static inline __ec_string_t __ec_slice_String(__ec_string_t a, size_t from, size_t to, const char* at) {
	if (from > to || to > a.len) __ec_panic_slice(at, from, to, a.len);
	return (__ec_string_t){to - from, a.data + from};
}
static inline __ec_array_AChar __ec_slice_AChar(__ec_array_AChar a, size_t from, size_t to, const char* at) {
	if (from > to || to > a.len) __ec_panic_slice(at, from, to, a.len);
	return (__ec_array_AChar){to - from, a.data + from};
}
static inline __ec_string_t __ec_view_AChar_to_String(__ec_array_AChar a) { return (__ec_string_t){a.len, (char*)a.data}; }
static inline StringBuilder* __ec_new_StringBuilder(apr_pool_t* pool, StringBuilder v) {
	StringBuilder* p = __ec_palloc(pool, sizeof(StringBuilder));
	*p = v;
	return p;
}
static inline char* __ec_at_AChar(__ec_array_AChar a, size_t i, const char* at) {
	if (i >= a.len) __ec_panic_index(at, i, a.len);
	return &a.data[i];
}
static __ec_string_t __ec_format_2(apr_pool_t* pool, long a0) {
	int n = snprintf(NULL, 0, "%ld", a0);
	char* s = __ec_palloc(pool, (size_t)n + 1);
	snprintf(s, (size_t)n + 1, "%ld", a0);
	return (__ec_string_t){(size_t)n, s};
}
static inline __ec_union_t216 __ec_cv_t234__t216(__ec_union_t234 v, const char* at) {
	switch (v.tag) {
		case 0: __ec_panic(at, "null value used where a value was required");
		case 1: return ((__ec_union_t216){.tag = 0, .v.m0 = v.v.m1});
		case 2: return ((__ec_union_t216){.tag = 1, .v.m1 = v.v.m2});
		case 3: return ((__ec_union_t216){.tag = 2, .v.m2 = v.v.m3});
		case 4: return ((__ec_union_t216){.tag = 3, .v.m3 = v.v.m4});
	}
	__ec_panic(at, "invalid union tag");
	return *(__ec_union_t216*)0;
}
static inline term__Screen* __ec_new_term__Screen(apr_pool_t* pool, term__Screen v) {
	term__Screen* p = __ec_palloc(pool, sizeof(term__Screen));
	*p = v;
	return p;
}
static inline term__Cell* __ec_at_Aterm__Cell(__ec_array_Aterm__Cell a, size_t i, const char* at) {
	if (i >= a.len) __ec_panic_index(at, i, a.len);
	return &a.data[i];
}
static __ec_string_t __ec_format_3(apr_pool_t* pool, __ec_string_t a0) {
	int n = snprintf(NULL, 0, " %.*s ", (int)a0.len, a0.data);
	char* s = __ec_palloc(pool, (size_t)n + 1);
	snprintf(s, (size_t)n + 1, " %.*s ", (int)a0.len, a0.data);
	return (__ec_string_t){(size_t)n, s};
}
static inline bool __ec_eq_term__Style(term__Style a, term__Style b) {
	return __ec_eq_term__Color(a.fg, b.fg)
		&& __ec_eq_term__Color(a.bg, b.bg)
		&& a.bold == b.bold
		&& a.dim == b.dim
		&& a.italic == b.italic
		&& a.underline == b.underline
		&& a.reverse == b.reverse
		&& a.strike == b.strike;
}
static inline bool __ec_eq_term__Cell(term__Cell a, term__Cell b) {
	return a.ch == b.ch
		&& __ec_eq_term__Style(a.style, b.style);
}
static __ec_string_t __ec_format_4(apr_pool_t* pool, int a0, int a1) {
	int n = snprintf(NULL, 0, "\033[%d;%dH", a0, a1);
	char* s = __ec_palloc(pool, (size_t)n + 1);
	snprintf(s, (size_t)n + 1, "\033[%d;%dH", a0, a1);
	return (__ec_string_t){(size_t)n, s};
}
static inline __ec_union_t234 __ec_cv_t216__t234(__ec_union_t216 v, const char* at) {
	switch (v.tag) {
		case 0: return ((__ec_union_t234){.tag = 1, .v.m1 = v.v.m0});
		case 1: return ((__ec_union_t234){.tag = 2, .v.m2 = v.v.m1});
		case 2: return ((__ec_union_t234){.tag = 3, .v.m3 = v.v.m2});
		case 3: return ((__ec_union_t234){.tag = 4, .v.m4 = v.v.m3});
	}
	__ec_panic(at, "invalid union tag");
	return *(__ec_union_t234*)0;
}
static inline __ec_array_Aterm__Cell __ec_alloc_Aterm__Cell(apr_pool_t* pool, size_t n) {
	if (n > SIZE_MAX / sizeof(term__Cell)) __ec_panic("eclang", "`new T[n]` is too large: n elements don't fit in memory (is n negative?)");
	__ec_array_Aterm__Cell a = {n, __ec_palloc(pool, n * sizeof(term__Cell))};
	for (size_t i = 0; i < n; i++) a.data[i] = (term__Cell){0};
	return a;
}
static inline Task* __ec_at_ATask(__ec_array_ATask a, size_t i, const char* at) {
	if (i >= a.len) __ec_panic_index(at, i, a.len);
	return &a.data[i];
}
static inline Error __ec_up_IoError__Error(IoError v) { return (Error){.code = v.code, .message = v.message, .__ec_tid = 65}; }
static inline __ec_union_term__Mouse_term__Resize_term__Eof __ec_cv_t216__term__Mouse_term__Resize_term__Eof(__ec_union_t216 v, const char* at) {
	switch (v.tag) {
		case 0: __ec_panic(at, "value is Key, not Mouse | Resize | Eof");
		case 1: return ((__ec_union_term__Mouse_term__Resize_term__Eof){.tag = 0, .v.m0 = v.v.m1});
		case 2: return ((__ec_union_term__Mouse_term__Resize_term__Eof){.tag = 1, .v.m1 = v.v.m2});
		case 3: return ((__ec_union_term__Mouse_term__Resize_term__Eof){.tag = 2, .v.m2 = v.v.m3});
	}
	__ec_panic(at, "invalid union tag");
	return *(__ec_union_term__Mouse_term__Resize_term__Eof*)0;
}
static inline __ec_union_term__Resize_term__Eof __ec_cv_t216__term__Resize_term__Eof(__ec_union_t216 v, const char* at) {
	switch (v.tag) {
		case 0: __ec_panic(at, "value is Key, not Resize | Eof");
		case 1: __ec_panic(at, "value is Mouse, not Resize | Eof");
		case 2: return ((__ec_union_term__Resize_term__Eof){.tag = 0, .v.m0 = v.v.m2});
		case 3: return ((__ec_union_term__Resize_term__Eof){.tag = 1, .v.m1 = v.v.m3});
	}
	__ec_panic(at, "invalid union tag");
	return *(__ec_union_term__Resize_term__Eof*)0;
}
static inline bool __ec_eq_term__Color(term__Color a, term__Color b) {
	return a.code == b.code;
}

// Declarations
__ec_string_t statusMessage_0(int status);
int statusKind_0(int status);
IoError ioError_0(int status, __ec_string_t path);
bool isSpace_0(char c);
__ec_string_t trim_0(__ec_string_t s);
__ec_result_Long_Error parseInt_0(__ec_string_t s);
bool isDigit_0(char c);
__ec_string_t fromC_0(char* cs);
__ec_string_t repeat_0(__ec_string_t s, size_t n);
__ec_string_t trimEnd_0(__ec_string_t s);
StringBuilder* stringBuilder_0(size_t capacity);
void reserve_0(StringBuilder* b, size_t n);
void append_0(StringBuilder* b, __ec_string_t s);
void append_1(StringBuilder* b, char c);
void append_3(StringBuilder* b, long n);
__ec_string_t build_0(StringBuilder* b);
bool unicode__isScalar_0(unsigned int cp);
bool unicode__isSurrogate_0(unsigned int cp);
int unicode__width_0(unsigned int cp);
__ec_tuple_UInt_Size unicode__decode_0(__ec_string_t s, size_t i);
void unicode__appendCodePoint_0(StringBuilder* b, unsigned int cp);
term__Dimensions term__size_0(void);
int term__charWidth_0(unsigned int cp);
size_t term__displayWidth_0(__ec_string_t s);
__ec_string_t term__fit_0(__ec_string_t s, size_t width);
__ec_result_None_IoError term__enterRaw_0(bool keepSignals);
void term__leaveRaw_0(void);
void term__enableMouse_0(void);
void term__disableMouse_0(void);
__ec_result_None_IoError term__enterFullscreen_1(void);
void term__leaveFullscreen_0(void);
bool term__isChar_0(term__Key k, char c);
bool term__isCtrl_0(term__Key k, char c);
term__Decoded term__decode_0(__ec_string_t s, bool final);
__ec_union_t216 term__readEvent_0(void);
term__Screen* term__screen_0(int cols, int rows);
term__Screen* term__screen_1(void);
void term__resize_0(term__Screen* s, term__Dimensions d);
void term__clear_0(term__Screen* s);
void term__put_0(term__Screen* s, int col, int row, unsigned int ch, term__Style style);
int term__text_0(term__Screen* s, int col, int row, __ec_string_t value, term__Style style);
void term__fill_0(term__Screen* s, int col, int row, int cols, int rows, unsigned int ch, term__Style style);
void term__box_0(term__Screen* s, int col, int row, int cols, int rows, char* kind, __ec_string_t title, term__Style style);
__ec_string_t term__frame_0(term__Screen* s);
void term__render_0(term__Screen* s);
apr_file_t* term__openStdin_0(void);
apr_file_t* term__openStdout_0(void);
__ec_union_Null_String term__envValue_0(char* name);
int term__envInt_0(char* name, int otherwise);
void term__emit_0(__ec_string_t s);
void term__appendColor_0(StringBuilder* b, term__Color c, int base);
void term__appendStyle_0(StringBuilder* b, term__Style style);
size_t term__skipEscape_0(__ec_string_t s, size_t i);
struct termios term__noSettings_0(void);
void term__writeDirect_0(__ec_string_t s);
void term__undoModes_0(void);
void term__redoModes_0(void);
void term__onFatalSignal_0(int signo);
void term__onStop_0(int signo);
void term__onResize_0(int signo);
void term__atExit_0(void);
void term__installHandlers_0(void);
void term__releaseHandlers_0(void);
void term__applyRaw_0(struct termios* settings, bool keepSignals);
term__Decoded term__plainKey_0(char* name, size_t used);
term__Decoded term__decodeByte_0(__ec_string_t s, bool final);
term__Decoded term__withModifiers_0(char* name, int mods, size_t used);
char* term__tildeKey_0(int n);
char* term__letterKey_0(char c);
term__Decoded term__mouseEvent_0(int code, int col, int row, bool released, size_t used);
term__Decoded term__decodeCsi_0(__ec_string_t s, bool final);
int term__waitForInput_0(long micros);
void term__fillPending_0(void);
void term__consume_0(size_t n);
__ec_union_t234 term__nextEvent_0(long timeout);
__ec_array_Aterm__Cell term__blankCells_0(size_t count);
size_t term__indexOf_0(term__Screen* s, int col, int row);
bool term__inside_0(term__Screen* s, int col, int row);
void term__breakWide_0(term__Screen* s, int col, int row);
__ec_string_t term__boxChars_0(char* kind);
unsigned int term__boxChar_0(__ec_string_t chars, size_t n);
void draw_0(term__Screen* s, __ec_array_ATask tasks, size_t current);
__ec_result_Int_Error main_0(void);

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
#define unicode__REPLACEMENT (65533)
#define unicode__MAX (1114111)
#define term__RGB_FLAG (16777216)
#define term__ESC (27)
#define term__ESCAPE_WAIT (30000)
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
// declare val APR_CHILD_DONE
// declare val APR_CHILD_NOTDONE
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
// declare val _IOFBF
// declare val _IONBF
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
// module unicode
// declare val _SC_NPROCESSORS_ONLN
// declare val _SC_PAGESIZE
// declare val _SC_PHYS_PAGES
// declare val _SC_AVPHYS_PAGES
// declare val X_OK
// declare val STDIN_FILENO
// declare val STDOUT_FILENO
// declare val STDERR_FILENO
// declare val ECHO
// declare val ICANON
// declare val ISIG
// declare val OPOST
// declare val TCSANOW
// declare val TCSADRAIN
// declare val TCSAFLUSH
// declare val TIOCGWINSZ
// declare val SIGABRT
// declare val SIGALRM
// declare val SIGBUS
// declare val SIGCHLD
// declare val SIGCONT
// declare val SIGFPE
// declare val SIGHUP
// declare val SIGILL
// declare val SIGINT
// declare val SIGKILL
// declare val SIGPIPE
// declare val SIGQUIT
// declare val SIGSEGV
// declare val SIGSTOP
// declare val SIGTERM
// declare val SIGTSTP
// declare val SIGUSR1
// declare val SIGUSR2
// declare val SIGWINCH
// declare val SIG_DFL
// declare val SIG_IGN
// declare val APR_MAX_SECS_TO_LINGER
// declare val APRMAXHOSTLEN
// declare val APR_ANYADDR
// declare val APR_SO_LINGER
// declare val APR_SO_KEEPALIVE
// declare val APR_SO_DEBUG
// declare val APR_SO_NONBLOCK
// declare val APR_SO_REUSEADDR
// declare val APR_SO_SNDBUF
// declare val APR_SO_RCVBUF
// declare val APR_SO_DISCONNECTED
// declare val APR_TCP_NODELAY
// declare val APR_TCP_NOPUSH
// declare val APR_RESET_NODELAY
// declare val APR_INCOMPLETE_READ
// declare val APR_INCOMPLETE_WRITE
// declare val APR_IPV6_V6ONLY
// declare val APR_TCP_DEFER_ACCEPT
// declare val APR_IPV4_ADDR_OK
// declare val APR_IPV6_ADDR_OK
// declare val APR_INADDR_NONE
// declare val APR_INET
// declare val APR_UNSPEC
// declare val APR_INET6
// declare val APR_PROTO_TCP
// declare val APR_PROTO_UDP
// declare val APR_PROTO_SCTP
// declare val APR_SENDFILE_DISCONNECT_SOCKET
// declare val SOCK_STREAM
// declare val SOCK_DGRAM
// declare val APR_POLLIN
// declare val APR_POLLPRI
// declare val APR_POLLOUT
// declare val APR_POLLERR
// declare val APR_POLLHUP
// declare val APR_POLLNVAL
// declare val APR_POLLSET_THREADSAFE
// declare val APR_POLLSET_NOCOPY
// declare val APR_POLLSET_WAKEABLE
// declare val APR_POLLSET_NODEFAULT
// declare val APR_NO_DESC
// declare val APR_POLL_SOCKET
// declare val APR_POLL_FILE
// module term
static term__Color term__CYAN = ((term__Color){.code = 7});
static term__Color term__GRAY = ((term__Color){.code = 9});
static term__Style term__PLAIN = ((term__Style){.bold = false, .fg = ((term__Color){.code = 0}), .bg = ((term__Color){.code = 0}), .dim = false, .italic = false, .underline = false, .reverse = false, .strike = false});
static apr_file_t* term__input;
static apr_file_t* term__output;
static bool term__cursorHidden = false;
static struct termios term__original;
static bool term__rawActive = false;
static bool term__fullscreenActive = false;
static bool term__mouseActive = false;
static bool term__handlersActive = false;
static bool term__exitHookSet = false;
static bool term__resized = false;
static char* term__heldErrors = NULL;
static __ec_array_AInt term__HANDLED;
static bool term__rawKeepsSignals = true;
static __ec_array_AChar term__pending;
static size_t term__pendingLen = 0;
static bool term__inputClosed = false;

// Code
__ec_string_t statusMessage_0(int status) {
	__ec_array_AChar buffer = __ec_alloc_AChar(__ec_pool, 256);
	return __ec_format_0(__ec_pool, ((char*)__ec_c_nn((void*)(apr_strerror(status, ((char*)(buffer.data)), buffer.len)), "core:errors.ec:124", "apr_strerror")));
}

int statusKind_0(int status) {
	if (APR_STATUS_IS_ENOENT(status)!=0) {
		return ERR_NOT_FOUND;
	}
	if (APR_STATUS_IS_EEXIST(status)!=0) {
		return ERR_EXISTS;
	}
	if (APR_STATUS_IS_EACCES(status)!=0) {
		return ERR_PERMISSION;
	}
	if (APR_STATUS_IS_EINVAL(status)!=0) {
		return ERR_ARGUMENT;
	}
	if (APR_STATUS_IS_ENOMEM(status)!=0) {
		return ERR_MEMORY;
	}
	if (APR_STATUS_IS_EBUSY(status)!=0) {
		return ERR_STATE;
	}
	if (APR_STATUS_IS_TIMEUP(status)!=0||APR_STATUS_IS_ETIMEDOUT(status)!=0) {
		return ERR_TIMEOUT;
	}
	if (APR_STATUS_IS_ENOTIMPL(status)!=0) {
		return ERR_UNSUPPORTED;
	}
	if (APR_STATUS_IS_EOF(status)!=0) {
		return ERR_IO;
	}
	return ERR_FAILED;
}

IoError ioError_0(int status, __ec_string_t path) {
	int kind = statusKind_0(status);
	int __ec_tmp1 = (kind==ERR_FAILED) ? (ERR_IO) : (kind);
	return ((IoError){.__ec_tid = 116, .code = __ec_tmp1, .message = __ec_format_1(__ec_pool, path, statusMessage_0(status)), .status = status, .path = path});
}

bool isSpace_0(char c) {
	return c==' '||c=='\t'||c=='\n'||c=='\r';
}

__ec_string_t trim_0(__ec_string_t s) {
	size_t start = 0;
	size_t end = s.len;
	while (start<end&&isSpace_0((*__ec_at_String(s, start, "core:string.ec:53")))) {
		start += 1;
	}
	while (end>start&&isSpace_0((*__ec_at_String(s, end-1, "core:string.ec:55")))) {
		end -= 1;
	}
	return __ec_slice_String(s, start, end, "core:string.ec:57");
}

__ec_result_Long_Error parseInt_0(__ec_string_t s) {
	__ec_string_t t = trim_0(s);
	if (t.len==0) {
		return ((__ec_result_Long_Error){.code = 1, .error = ((Error){.__ec_tid = 65, .code = ERR_PARSE, .message = (__ec_string_t){25,"not a number: it is empty"}})});
	}
	bool negative = (*__ec_at_String(t, 0, "core:string.ec:85"))=='-';
	size_t i = (negative||(*__ec_at_String(t, 0, "core:string.ec:86"))=='+') ? (1) : (0);
	if (i==t.len) {
		return ((__ec_result_Long_Error){.code = 1, .error = ((Error){.__ec_tid = 65, .code = ERR_PARSE, .message = (__ec_string_t){23,"not a number: no digits"}})});
	}
	long value = 0;
	long limit = (-9223372036854775807L - 1);
	while (i<t.len) {
		char c = (*__ec_at_String(t, i, "core:string.ec:92"));
		if (c<'0'||c>'9') {
			return ((__ec_result_Long_Error){.code = 1, .error = ((Error){.__ec_tid = 65, .code = ERR_PARSE, .message = (__ec_string_t){49,"not a number: it has other characters than digits"}})});
		}
		long digit = ((long)((c-'0')));
		if (value<(limit+digit)/10) {
			return ((__ec_result_Long_Error){.code = 1, .error = ((Error){.__ec_tid = 65, .code = ERR_RANGE, .message = (__ec_string_t){32,"the number is too big for a Long"}})});
		}
		value = value*10-digit;
		i += 1;
	}
	if (negative) {
		return ((__ec_result_Long_Error){.code = 0, .value = value});
	}
	if (value==limit) {
		return ((__ec_result_Long_Error){.code = 1, .error = ((Error){.__ec_tid = 65, .code = ERR_RANGE, .message = (__ec_string_t){32,"the number is too big for a Long"}})});
	}
	return ((__ec_result_Long_Error){.code = 0, .value = -value});
}

bool isDigit_0(char c) {
	return c>='0'&&c<='9';
}

__ec_string_t fromC_0(char* cs) {
	return __ec_format_0(__ec_pool, cs);
}

__ec_string_t repeat_0(__ec_string_t s, size_t n) {
	__ec_array_AChar buffer = __ec_alloc_AChar(__ec_pool, s.len*n);
	for (size_t i=0;i<n;i++) {
		((void*)__ec_c_nn((void*)(memcpy(__ec_slice_AChar(buffer, i*s.len, buffer.len, "core:string.ec:161").data, s.data, s.len)), "core:string.ec:161", "memcpy"));
	}
	return __ec_view_AChar_to_String(buffer);
}

__ec_string_t trimEnd_0(__ec_string_t s) {
	size_t end = s.len;
	while (end>0&&isSpace_0((*__ec_at_String(s, end-1, "core:string.ec:298")))) {
		end -= 1;
	}
	return __ec_slice_String(s, 0, end, "core:string.ec:300");
}

StringBuilder* stringBuilder_0(size_t capacity) {
	return __ec_new_StringBuilder(__ec_pool, ((StringBuilder){.buffer = __ec_alloc_AChar(__ec_pool, (capacity==0) ? (16) : (capacity)), .len = 0, .mem = __ec_pool}));
}

void reserve_0(StringBuilder* b, size_t n) {
	if (b->len+n>b->buffer.len) {
		size_t capacity = b->buffer.len*2;
		while (capacity<b->len+n) {
			capacity *= 2;
		}
		__ec_array_AChar grown = __ec_alloc_AChar(b->mem, capacity);
		((void*)__ec_c_nn((void*)(memcpy(grown.data, b->buffer.data, b->len)), "core:string.ec:698", "memcpy"));
		b->buffer = grown;
	}
}

void append_0(StringBuilder* b, __ec_string_t s) {
	reserve_0(b, s.len);
	((void*)__ec_c_nn((void*)(memcpy(__ec_slice_AChar(b->buffer, b->len, b->buffer.len, "core:string.ec:704").data, s.data, s.len)), "core:string.ec:704", "memcpy"));
	b->len += s.len;
}

void append_1(StringBuilder* b, char c) {
	reserve_0(b, 1);
	(*__ec_at_AChar(b->buffer, b->len, "core:string.ec:710")) = c;
	b->len += 1;
}

void append_3(StringBuilder* b, long n) {
	append_0(b, __ec_format_2(b->mem, n));
}

__ec_string_t build_0(StringBuilder* b) {
	return __ec_view_AChar_to_String(__ec_slice_AChar(b->buffer, 0, b->len, "core:string.ec:739"));
}

bool unicode__isScalar_0(unsigned int cp) {
	return cp<=unicode__MAX&&!unicode__isSurrogate_0(cp);
}

bool unicode__isSurrogate_0(unsigned int cp) {
	return cp>=55296&&cp<=57343;
}

int unicode__width_0(unsigned int cp) {
	if (cp<32||(cp>=127&&cp<160)) {
		return 0;
	}
	if (cp<768) {
		return 1;
	}
	if (cp<880||(cp>=8203&&cp<=8207)||(cp>=65024&&cp<=65039)||(cp>=8400&&cp<=8447)) {
		return 0;
	}
	if (cp>=4352&&cp<=4447) {
		return 2;
	}
	if (cp>=11904&&cp<=42191&&cp!=12351) {
		return 2;
	}
	if ((cp>=44032&&cp<=55203)||(cp>=63744&&cp<=64255)||(cp>=65072&&cp<=65135)) {
		return 2;
	}
	if ((cp>=65280&&cp<=65376)||(cp>=65504&&cp<=65510)) {
		return 2;
	}
	if ((cp>=127744&&cp<=128591)||(cp>=129280&&cp<=129535)||(cp>=131072&&cp<=262141)) {
		return 2;
	}
	return 1;
}

__ec_tuple_UInt_Size unicode__decode_0(__ec_string_t s, size_t i) {
	unsigned int c = ((unsigned int)((((unsigned char)((*__ec_at_String(s, i, "core:unicode.ec:89")))))));
	if (c<128) {
		return ((__ec_tuple_UInt_Size){.first = c, .second = 1});
	}
	size_t n = 0;
	unsigned int cp = 0;
	unsigned int low = 128;
	unsigned int high = 191;
	if (c>=194&&c<=223) {
		n = 2;
		cp = (c & 31);
	}
 else if (c>=224&&c<=239) {
		n = 3;
		cp = (c & 15);
		low = (c==224) ? (160) : (128);
		high = (c==237) ? (159) : (191);
	}
 else if (c>=240&&c<=244) {
		n = 4;
		cp = (c & 7);
		low = (c==240) ? (144) : (128);
		high = (c==244) ? (143) : (191);
	}
	if (n==0||i+n>s.len) {
		return ((__ec_tuple_UInt_Size){.first = unicode__REPLACEMENT, .second = 1});
	}
	for (size_t k=1;k<n;k++) {
		unsigned int next = ((unsigned int)((((unsigned char)((*__ec_at_String(s, i+k, "core:unicode.ec:111")))))));
		if (next<low||next>high) {
			return ((__ec_tuple_UInt_Size){.first = unicode__REPLACEMENT, .second = 1});
		}
		cp = (((cp << 6)) | ((next & 63)));
		low = 128;
		high = 191;
	}
	return ((__ec_tuple_UInt_Size){.first = cp, .second = n});
}

void unicode__appendCodePoint_0(StringBuilder* b, unsigned int cp) {
	unsigned int c = (unicode__isScalar_0(cp)) ? (cp) : (unicode__REPLACEMENT);
	if (c<128) {
		append_1(b, ((char)(c)));
	}
	else if (c<2048) {
		append_1(b, ((char)(((192 | ((c >> 6)))))));
		append_1(b, ((char)(((128 | ((c & 63)))))));
	}
	else if (c<65536) {
		append_1(b, ((char)(((224 | ((c >> 12)))))));
		append_1(b, ((char)(((128 | ((((c >> 6)) & 63)))))));
		append_1(b, ((char)(((128 | ((c & 63)))))));
	}
	else {
		append_1(b, ((char)(((240 | ((c >> 18)))))));
		append_1(b, ((char)(((128 | ((((c >> 12)) & 63)))))));
		append_1(b, ((char)(((128 | ((((c >> 6)) & 63)))))));
		append_1(b, ((char)(((128 | ((c & 63)))))));
	}
}

term__Dimensions term__size_0(void) {
	struct winsize w = {0};
	if (ioctl(STDOUT_FILENO, TIOCGWINSZ, &(w))==0&&w.ws_col>0&&w.ws_row>0) {
		return ((term__Dimensions){.cols = ((int)(w.ws_col)), .rows = ((int)(w.ws_row))});
	}
	int __ec_tmp116 = term__envInt_0("COLUMNS", 80);
	return ((term__Dimensions){.cols = __ec_tmp116, .rows = term__envInt_0("LINES", 24)});
}

int term__charWidth_0(unsigned int cp) {
	return unicode__width_0(cp);
}

size_t term__displayWidth_0(__ec_string_t s) {
	size_t width = 0;
	size_t i = 0;
	while (i<s.len) {
		if ((*__ec_at_String(s, i, "core:term.ec:305"))==term__ESC) {
			i = term__skipEscape_0(s, i);
		}
		else {
			__ec_tuple_UInt_Size __ec_tmp118 = unicode__decode_0(s, i);
			unsigned int cp = __ec_tmp118.first;
			size_t n = __ec_tmp118.second;
			width += ((size_t)(term__charWidth_0(cp)));
			i += n;
		}
	}
	return width;
}

__ec_string_t term__fit_0(__ec_string_t s, size_t width) {
	size_t have = term__displayWidth_0(s);
	if (have<=width) {
		return __ec_str_concat(__ec_pool, s, repeat_0((__ec_string_t){1," "}, width-have));
	}
	if (width==0) {
		return (__ec_string_t){0,""};
	}
	StringBuilder* b = stringBuilder_0(s.len+4);
	size_t used = 0;
	size_t i = 0;
	while (i<s.len) {
		__ec_tuple_UInt_Size __ec_tmp119 = unicode__decode_0(s, i);
		unsigned int cp = __ec_tmp119.first;
		size_t n = __ec_tmp119.second;
		size_t w = ((size_t)(term__charWidth_0(cp)));
		if (used+w>width-1) {
			break;
		}
		append_0(b, __ec_slice_String(s, i, i+n, "core:term.ec:328"));
		used += w;
		i += n;
	}
	append_0(b, (__ec_string_t){3,"…"});
	append_0(b, repeat_0((__ec_string_t){1," "}, width-1-used));
	return build_0(b);
}

__ec_result_None_IoError term__enterRaw_0(bool keepSignals) {
	if (!term__rawActive) {
		if (tcgetattr(STDIN_FILENO, &(term__original))!=0) {
			return ((__ec_result_None_IoError){.code = 1, .error = ioError_0(apr_get_os_error(), (__ec_string_t){5,"stdin"})});
		}
		struct termios settings = term__original;
		term__applyRaw_0(&(settings), keepSignals);
		if (tcsetattr(STDIN_FILENO, TCSADRAIN, &(settings))!=0) {
			return ((__ec_result_None_IoError){.code = 1, .error = ioError_0(apr_get_os_error(), (__ec_string_t){5,"stdin"})});
		}
		term__rawKeepsSignals = keepSignals;
		term__rawActive = true;
		term__installHandlers_0();
	}
	return ((__ec_result_None_IoError){.code = 0});
}

void term__leaveRaw_0(void) {
	if (term__rawActive) {
		fflush(stdout);
		tcsetattr(STDIN_FILENO, TCSADRAIN, &(term__original));
		term__rawActive = false;
		term__releaseHandlers_0();
	}
}

void term__enableMouse_0(void) {
	term__installHandlers_0();
	term__mouseActive = true;
	term__emit_0((__ec_string_t){16,"\033[?1002h\033[?1006h"});
}

void term__disableMouse_0(void) {
	term__mouseActive = false;
	term__emit_0((__ec_string_t){16,"\033[?1006l\033[?1002l"});
	term__releaseHandlers_0();
}

__ec_result_None_IoError term__enterFullscreen_1(void) {
	bool keepSignals = true;
	if (!term__fullscreenActive) {
		__ec_result_None_IoError __ec_tmp121 = term__enterRaw_0(keepSignals);
		if (__ec_tmp121.code != 0) {
			return ((__ec_result_None_IoError){.code = 1, .error = __ec_tmp121.error});
		}
		term__fullscreenActive = true;
		if (((term__heldErrors) == NULL)) {
			term__heldErrors = ((char*)(((void*)(malloc(65536)))));
		}
		fflush(stderr);
		setvbuf(stderr, term__heldErrors, _IOFBF, 65536);
		term__emit_0((__ec_string_t){18,"\033[?1049h\033[?25l\033[2J"});
	}
	return ((__ec_result_None_IoError){.code = 0});
}

void term__leaveFullscreen_0(void) {
	if (term__fullscreenActive) {
		if (term__mouseActive) {
			term__disableMouse_0();
		}
		term__emit_0((__ec_string_t){18,"\033[0m\033[?1049l\033[?25h"});
		term__cursorHidden = false;
		term__fullscreenActive = false;
		term__leaveRaw_0();
		fflush(stderr);
		setvbuf(stderr, NULL, _IONBF, 0);
	}
}

bool term__isChar_0(term__Key k, char c) {
	return k.name==__ec_atom_char&&k.ch==((unsigned int)((((unsigned char)(c)))))&&!k.ctrl&&!k.alt;
}

bool term__isCtrl_0(term__Key k, char c) {
	return k.name==__ec_atom_char&&k.ch==((unsigned int)((((unsigned char)(c)))))&&k.ctrl;
}

term__Decoded term__decode_0(__ec_string_t s, bool final) {
	if (s.len==0) {
		return ((term__Decoded){.event = ((__ec_union_t234){.tag = 0}), .used = 0});
	}
	if ((*__ec_at_String(s, 0, "core:term.ec:769"))!=term__ESC) {
		return term__decodeByte_0(s, final);
	}
	if (s.len==1) {
		return (final) ? (term__plainKey_0(__ec_atom_escape, 1)) : (((term__Decoded){.event = ((__ec_union_t234){.tag = 0}), .used = 0}));
	}
	if ((*__ec_at_String(s, 1, "core:term.ec:772"))=='[') {
		return term__decodeCsi_0(s, final);
	}
	if ((*__ec_at_String(s, 1, "core:term.ec:773"))=='O') {
		if (s.len==2) {
			return (final) ? (((term__Decoded){.event = ((__ec_union_t234){.tag = 1, .v.m1 = ((term__Key){.name = __ec_atom_char, .ch = ((unsigned int)('o')), .alt = true, .ctrl = false, .shift = false})}), .used = 2})) : (((term__Decoded){.event = ((__ec_union_t234){.tag = 0}), .used = 0}));
		}
		char* __ec_tmp122 = term__letterKey_0((*__ec_at_String(s, 2, "core:term.ec:776")));
		if (!((__ec_tmp122) == NULL)) {
			char* name = __ec_tmp122;
			return term__plainKey_0(name, 3);
		}
		return ((term__Decoded){.event = ((__ec_union_t234){.tag = 0}), .used = 3});
	}
	if ((*__ec_at_String(s, 1, "core:term.ec:779"))==term__ESC) {
		return term__plainKey_0(__ec_atom_escape, 1);
	}
	term__Decoded inner = term__decodeByte_0(__ec_slice_String(s, 1, s.len, "core:term.ec:781"), final);
	if (inner.used==0) {
		return ((term__Decoded){.event = ((__ec_union_t234){.tag = 0}), .used = 0});
	}
	__ec_union_t234 __ec_tmp123 = inner.event;
	if (!((__ec_tmp123).tag == 0)) {
		__ec_union_t216 e = __ec_cv_t234__t216(__ec_tmp123, "core:term.ec:783");
		if (((e).tag == 0)) {
			return ((term__Decoded){.event = ((__ec_union_t234){.tag = 1, .v.m1 = ((term__Key){.alt = true, .name = (e).v.m0.name, .ch = (e).v.m0.ch, .ctrl = (e).v.m0.ctrl, .shift = (e).v.m0.shift})}), .used = inner.used+1});
		}
		else {
			return ((term__Decoded){.event = ((__ec_union_t234){.tag = 0}), .used = inner.used+1});
		}
	}
	return ((term__Decoded){.event = ((__ec_union_t234){.tag = 0}), .used = inner.used+1});
}

__ec_union_t216 term__readEvent_0(void) {
	while (true) {
		__ec_union_t234 __ec_tmp126 = term__nextEvent_0(-1);
		if (!((__ec_tmp126).tag == 0)) {
			__ec_union_t216 e = __ec_cv_t234__t216(__ec_tmp126, "core:term.ec:862");
			return e;
		}
	}
}

term__Screen* term__screen_0(int cols, int rows) {
	size_t count = ((size_t)((cols*rows)));
	__ec_array_Aterm__Cell __ec_tmp127 = term__blankCells_0(count);
	return __ec_new_term__Screen(__ec_pool, ((term__Screen){.cols = cols, .rows = rows, .cells = __ec_tmp127, .shown = term__blankCells_0(count), .stale = true, .cursorCol = -1, .cursorRow = 0, .cursorShown = false}));
}

term__Screen* term__screen_1(void) {
	term__Dimensions d = term__size_0();
	return term__screen_0(d.cols, d.rows);
}

void term__resize_0(term__Screen* s, term__Dimensions d) {
	if (d.cols!=s->cols||d.rows!=s->rows) {
		size_t count = ((size_t)((d.cols*d.rows)));
		s->cols = d.cols;
		s->rows = d.rows;
		s->cells = term__blankCells_0(count);
		s->shown = term__blankCells_0(count);
	}
	s->stale = true;
}

void term__clear_0(term__Screen* s) {
	for (size_t i=0;i<s->cells.len;i++) {
		(*__ec_at_Aterm__Cell(s->cells, i, "core:term.ec:947")) = ((term__Cell){.ch = 32, .style = term__PLAIN});
	}
}

void term__put_0(term__Screen* s, int col, int row, unsigned int ch, term__Style style) {
	int width = term__charWidth_0(ch);
	if (term__inside_0(s, col, row)&&width>0) {
		term__breakWide_0(s, col, row);
		if (width==2&&col+1>=s->cols) {
			(*__ec_at_Aterm__Cell(s->cells, term__indexOf_0(s, col, row), "core:term.ec:969")) = ((term__Cell){.ch = 32, .style = style});
		}
		else {
			(*__ec_at_Aterm__Cell(s->cells, term__indexOf_0(s, col, row), "core:term.ec:971")) = ((term__Cell){.ch = ch, .style = style});
			if (width==2) {
				term__breakWide_0(s, col+1, row);
				(*__ec_at_Aterm__Cell(s->cells, term__indexOf_0(s, col+1, row), "core:term.ec:974")) = ((term__Cell){.ch = 0, .style = style});
			}
		}
	}
}

int term__text_0(term__Screen* s, int col, int row, __ec_string_t value, term__Style style) {
	int at = col;
	size_t i = 0;
	while (i<value.len&&at<s->cols) {
		__ec_tuple_UInt_Size __ec_tmp128 = unicode__decode_0(value, i);
		unsigned int cp = __ec_tmp128.first;
		size_t n = __ec_tmp128.second;
		term__put_0(s, at, row, cp, style);
		at += term__charWidth_0(cp);
		i += n;
	}
	return at;
}

void term__fill_0(term__Screen* s, int col, int row, int cols, int rows, unsigned int ch, term__Style style) {
	for (int y=row;y<row+rows;y++) {
		for (int x=col;x<col+cols;x++) {
			term__put_0(s, x, y, ch, style);
		}
	}
}

void term__box_0(term__Screen* s, int col, int row, int cols, int rows, char* kind, __ec_string_t title, term__Style style) {
	if (!(cols<2||rows<2)) {
		__ec_string_t chars = term__boxChars_0(kind);
		int right = col+cols-1;
		int bottom = row+rows-1;
		int __ec_tmp130 = col+1;
		int __ec_tmp131 = cols-2;
		term__fill_0(s, __ec_tmp130, row, __ec_tmp131, 1, term__boxChar_0(chars, 0), style);
		int __ec_tmp132 = col+1;
		int __ec_tmp133 = cols-2;
		term__fill_0(s, __ec_tmp132, bottom, __ec_tmp133, 1, term__boxChar_0(chars, 0), style);
		int __ec_tmp134 = row+1;
		int __ec_tmp135 = rows-2;
		term__fill_0(s, col, __ec_tmp134, 1, __ec_tmp135, term__boxChar_0(chars, 1), style);
		int __ec_tmp136 = row+1;
		int __ec_tmp137 = rows-2;
		term__fill_0(s, right, __ec_tmp136, 1, __ec_tmp137, term__boxChar_0(chars, 1), style);
		term__put_0(s, col, row, term__boxChar_0(chars, 2), style);
		term__put_0(s, right, row, term__boxChar_0(chars, 3), style);
		term__put_0(s, col, bottom, term__boxChar_0(chars, 4), style);
		term__put_0(s, right, bottom, term__boxChar_0(chars, 5), style);
		if (title.len>0&&cols>6) {
			size_t room = ((size_t)((cols-6)));
			__ec_string_t shown = (term__displayWidth_0(title)>room) ? (trimEnd_0(term__fit_0(title, room))) : (title);
			term__text_0(s, col+2, row, __ec_format_3(__ec_pool, shown), style);
		}
	}
}

__ec_string_t term__frame_0(term__Screen* s) {
	StringBuilder* b = stringBuilder_0(256);
	if (s->stale) {
		append_0(b, (__ec_string_t){8,"\033[0m\033[2J"});
	}
	term__Style style = term__PLAIN;
	bool styleKnown = false;
	int atCol = -1;
	int atRow = -1;
	for (int row=0;row<s->rows;row++) {
		for (int col=0;col<s->cols;col++) {
			size_t i = term__indexOf_0(s, col, row);
			term__Cell cell = (*__ec_at_Aterm__Cell(s->cells, i, "core:term.ec:1069"));
			bool blank = cell.ch==32&&__ec_eq_term__Style(cell.style, term__PLAIN);
			if (cell.ch!=0&&((s->stale) ? (!blank) : (!__ec_eq_term__Cell(cell, (*__ec_at_Aterm__Cell(s->shown, i, "core:term.ec:1071")))))) {
				if (atCol!=col||atRow!=row) {
					append_0(b, __ec_format_4(__ec_pool, row+1, col+1));
				}
				if (!styleKnown||!__ec_eq_term__Style(cell.style, style)) {
					term__appendStyle_0(b, cell.style);
					style = cell.style;
					styleKnown = true;
				}
				unicode__appendCodePoint_0(b, cell.ch);
				atCol = col+term__charWidth_0(cell.ch);
				atRow = row;
			}
			(*__ec_at_Aterm__Cell(s->shown, i, "core:term.ec:1081")) = cell;
		}
	}
	if (styleKnown&&!__ec_eq_term__Style(style, term__PLAIN)) {
		append_0(b, (__ec_string_t){4,"\033[0m"});
	}
	s->stale = false;
	if (s->cursorCol>=0) {
		append_0(b, __ec_format_4(__ec_pool, s->cursorRow+1, s->cursorCol+1));
		if (!s->cursorShown) {
			append_0(b, (__ec_string_t){6,"\033[?25h"});
		}
		s->cursorShown = true;
	}
 else if (s->cursorShown) {
		append_0(b, (__ec_string_t){6,"\033[?25l"});
		s->cursorShown = false;
	}
	return build_0(b);
}

void term__render_0(term__Screen* s) {
	{
		apr_pool_t* __ec_parent194 = __ec_pool;
		__ec_pool = __ec_pool_create(__ec_parent194);
		term__emit_0(term__frame_0(s));
		apr_pool_destroy(__ec_pool);
		__ec_pool = __ec_parent194;
	}
}

apr_file_t* term__openStdin_0(void) {
	apr_file_t* f = NULL;
	apr_file_open_stdin(((void*)(&f)), __ec_pool);
	return ((apr_file_t*)__ec_nn((void*)(f), "core:term.ec:69"));
}

apr_file_t* term__openStdout_0(void) {
	apr_file_t* f = NULL;
	apr_file_open_stdout(((void*)(&f)), __ec_pool);
	return ((apr_file_t*)__ec_nn((void*)(f), "core:term.ec:75"));
}

__ec_union_Null_String term__envValue_0(char* name) {
	char* value = "";
	if (apr_env_get(((void*)(&(value))), name, __ec_pool)!=APR_SUCCESS) {
		return ((__ec_union_Null_String){.tag = 0});
	}
	return ((__ec_union_Null_String){.tag = 1, .v.m1 = fromC_0(value)});
}

int term__envInt_0(char* name, int otherwise) {
	__ec_union_Null_String __ec_tmp199 = term__envValue_0(name);
	if (!((__ec_tmp199).tag == 0)) {
		__ec_string_t text = (__ec_tmp199).v.m1;
		__ec_result_Long_Error __ec_tmp200 = parseInt_0(text);
		long n = (__ec_tmp200.code == 0) ? (__ec_tmp200.value) : (0);
		return (n>0&&n<100000) ? (((int)(n))) : (otherwise);
	}
	return otherwise;
}

void term__emit_0(__ec_string_t s) {
	fflush(stdout);
	size_t written = 0;
	apr_file_write_full(term__output, s.data, s.len, &(written));
}

void term__appendColor_0(StringBuilder* b, term__Color c, int base) {
	if (c.code>=term__RGB_FLAG) {
		append_0(b, (__ec_string_t){1,";"});
		append_3(b, ((long)((base+8))));
		append_0(b, (__ec_string_t){3,";2;"});
		append_3(b, ((long)(((((c.code >> 16)) & 255)))));
		append_0(b, (__ec_string_t){1,";"});
		append_3(b, ((long)(((((c.code >> 8)) & 255)))));
		append_0(b, (__ec_string_t){1,";"});
		append_3(b, ((long)(((c.code & 255)))));
	}
 else if (c.code>16) {
		append_0(b, (__ec_string_t){1,";"});
		append_3(b, ((long)((base+8))));
		append_0(b, (__ec_string_t){3,";5;"});
		append_3(b, ((long)((c.code-1))));
	}
 else if (c.code>8) {
		append_0(b, (__ec_string_t){1,";"});
		append_3(b, ((long)((base+60+c.code-9))));
	}
 else if (c.code>0) {
		append_0(b, (__ec_string_t){1,";"});
		append_3(b, ((long)((base+c.code-1))));
	}
}

void term__appendStyle_0(StringBuilder* b, term__Style style) {
	append_0(b, (__ec_string_t){3,"\033[0"});
	if (style.bold) {
		append_0(b, (__ec_string_t){2,";1"});
	}
	if (style.dim) {
		append_0(b, (__ec_string_t){2,";2"});
	}
	if (style.italic) {
		append_0(b, (__ec_string_t){2,";3"});
	}
	if (style.underline) {
		append_0(b, (__ec_string_t){2,";4"});
	}
	if (style.reverse) {
		append_0(b, (__ec_string_t){2,";7"});
	}
	if (style.strike) {
		append_0(b, (__ec_string_t){2,";9"});
	}
	term__appendColor_0(b, style.fg, 30);
	term__appendColor_0(b, style.bg, 40);
	append_0(b, (__ec_string_t){1,"m"});
}

size_t term__skipEscape_0(__ec_string_t s, size_t i) {
	size_t at = i+1;
	if (at>=s.len||(*__ec_at_String(s, at, "core:term.ec:282"))!='[') {
		return at;
	}
	at += 1;
	while (at<s.len&&((*__ec_at_String(s, at, "core:term.ec:284"))<'@'||(*__ec_at_String(s, at, "core:term.ec:284"))>'~')) {
		at += 1;
	}
	return (at<s.len) ? (at+1) : (at);
}

struct termios term__noSettings_0(void) {
	struct termios settings = {0};
	return settings;
}

void term__writeDirect_0(__ec_string_t s) {
	write(STDOUT_FILENO, s.data, s.len);
}

void term__undoModes_0(void) {
	if (term__mouseActive) {
		term__writeDirect_0((__ec_string_t){16,"\033[?1006l\033[?1002l"});
	}
	if (term__fullscreenActive) {
		term__writeDirect_0((__ec_string_t){4,"\033[0m"});
	}
	if (term__fullscreenActive) {
		term__writeDirect_0((__ec_string_t){8,"\033[?1049l"});
	}
	if (term__cursorHidden||term__fullscreenActive) {
		term__writeDirect_0((__ec_string_t){6,"\033[?25h"});
	}
	if (term__rawActive) {
		tcsetattr(STDIN_FILENO, TCSADRAIN, &(term__original));
	}
}

void term__redoModes_0(void) {
	struct termios settings = term__original;
	if (term__rawActive) {
		term__applyRaw_0(&(settings), term__rawKeepsSignals);
		tcsetattr(STDIN_FILENO, TCSADRAIN, &(settings));
	}
	if (term__fullscreenActive) {
		term__writeDirect_0((__ec_string_t){8,"\033[?1049h"});
	}
	if (term__cursorHidden||term__fullscreenActive) {
		term__writeDirect_0((__ec_string_t){6,"\033[?25l"});
	}
	if (term__mouseActive) {
		term__writeDirect_0((__ec_string_t){16,"\033[?1002h\033[?1006h"});
	}
}

void term__onFatalSignal_0(int signo) {
	term__undoModes_0();
	if (signo==SIGABRT&&!((term__heldErrors) == NULL)) {
		fflush(stderr);
	}
	((__ec_fn_Int_to_None)(apr_signal(signo, ((void*)(SIG_DFL)))));
	raise(signo);
}

void term__onStop_0(int signo) {
	(void)signo;
	term__undoModes_0();
	raise(SIGSTOP);
	term__redoModes_0();
	term__resized = true;
}

void term__onResize_0(int signo) {
	(void)signo;
	term__resized = true;
}

void term__atExit_0(void) {
	term__undoModes_0();
	if (!((term__heldErrors) == NULL)) {
		fflush(stderr);
		setvbuf(stderr, NULL, _IONBF, 0);
	}
	term__rawActive = false;
	term__fullscreenActive = false;
	term__mouseActive = false;
}

void term__installHandlers_0(void) {
	if (!term__exitHookSet) {
		term__exitHookSet = true;
		atexit(((void*)(term__atExit_0)));
	}
	if (!term__handlersActive) {
		term__handlersActive = true;
		for (size_t __ec_tmp201=0UL;__ec_tmp201<term__HANDLED.len;__ec_tmp201++) {
			int signo = term__HANDLED.data[__ec_tmp201];
			((__ec_fn_Int_to_None)(apr_signal(signo, ((void*)(term__onFatalSignal_0)))));
		}
		((__ec_fn_Int_to_None)(apr_signal(SIGTSTP, ((void*)(term__onStop_0)))));
		((__ec_fn_Int_to_None)(apr_signal(SIGWINCH, ((void*)(term__onResize_0)))));
	}
}

void term__releaseHandlers_0(void) {
	if (term__handlersActive&&!term__rawActive&&!term__fullscreenActive&&!term__cursorHidden&&!term__mouseActive) {
		term__handlersActive = false;
		for (size_t __ec_tmp202=0UL;__ec_tmp202<term__HANDLED.len;__ec_tmp202++) {
			int signo = term__HANDLED.data[__ec_tmp202];
			((__ec_fn_Int_to_None)(apr_signal(signo, ((void*)(SIG_DFL)))));
		}
		((__ec_fn_Int_to_None)(apr_signal(SIGTSTP, ((void*)(SIG_DFL)))));
		((__ec_fn_Int_to_None)(apr_signal(SIGWINCH, ((void*)(SIG_DFL)))));
	}
}

void term__applyRaw_0(struct termios* settings, bool keepSignals) {
	cfmakeraw(settings);
	settings->c_oflag = (settings->c_oflag | OPOST);
	if (keepSignals) {
		settings->c_lflag = (settings->c_lflag | ISIG);
	}
}

term__Decoded term__plainKey_0(char* name, size_t used) {
	return ((term__Decoded){.event = ((__ec_union_t234){.tag = 1, .v.m1 = ((term__Key){.name = name, .ch = 0, .ctrl = false, .alt = false, .shift = false})}), .used = used});
}

term__Decoded term__decodeByte_0(__ec_string_t s, bool final) {
	unsigned int c = ((unsigned int)((((unsigned char)((*__ec_at_String(s, 0, "core:term.ec:646")))))));
	if (c==13||c==10) {
		return term__plainKey_0(__ec_atom_enter, 1);
	}
	if (c==9) {
		return term__plainKey_0(__ec_atom_tab, 1);
	}
	if (c==127||c==8) {
		return term__plainKey_0(__ec_atom_backspace, 1);
	}
	if (c==0) {
		return ((term__Decoded){.event = ((__ec_union_t234){.tag = 1, .v.m1 = ((term__Key){.name = __ec_atom_char, .ch = ((unsigned int)(' ')), .ctrl = true, .alt = false, .shift = false})}), .used = 1});
	}
	if (c<27) {
		return ((term__Decoded){.event = ((__ec_union_t234){.tag = 1, .v.m1 = ((term__Key){.name = __ec_atom_char, .ch = c+96, .ctrl = true, .alt = false, .shift = false})}), .used = 1});
	}
	if (c<32) {
		return ((term__Decoded){.event = ((__ec_union_t234){.tag = 1, .v.m1 = ((term__Key){.name = __ec_atom_char, .ch = c+64, .ctrl = true, .alt = false, .shift = false})}), .used = 1});
	}
	if (c<128) {
		return ((term__Decoded){.event = ((__ec_union_t234){.tag = 1, .v.m1 = ((term__Key){.name = __ec_atom_char, .ch = c, .ctrl = false, .alt = false, .shift = false})}), .used = 1});
	}
	size_t need = (c>=240) ? (4) : ((c>=224) ? (3) : (2));
	if (c>=194&&s.len<need&&!final) {
		return ((term__Decoded){.event = ((__ec_union_t234){.tag = 0}), .used = 0});
	}
	__ec_tuple_UInt_Size __ec_tmp203 = unicode__decode_0(s, 0);
	unsigned int cp = __ec_tmp203.first;
	size_t n = __ec_tmp203.second;
	if (cp==65533&&n==1) {
		return ((term__Decoded){.event = ((__ec_union_t234){.tag = 0}), .used = n});
	}
	return ((term__Decoded){.event = ((__ec_union_t234){.tag = 1, .v.m1 = ((term__Key){.name = __ec_atom_char, .ch = cp, .ctrl = false, .alt = false, .shift = false})}), .used = n});
}

term__Decoded term__withModifiers_0(char* name, int mods, size_t used) {
	int m = (mods>0) ? (mods-1) : (0);
	return ((term__Decoded){.event = ((__ec_union_t234){.tag = 1, .v.m1 = ((term__Key){.name = name, .shift = (m & 1)!=0, .alt = (m & 2)!=0, .ctrl = (m & 4)!=0, .ch = 0})}), .used = used});
}

char* term__tildeKey_0(int n) {
	if (n==1||n==7) {
		return __ec_atom_home;
	}
	else if (n==2) {
		return __ec_atom_insert;
	}
	else if (n==3) {
		return __ec_atom_delete;
	}
	else if (n==4||n==8) {
		return __ec_atom_end;
	}
	else if (n==5) {
		return __ec_atom_pageUp;
	}
	else if (n==6) {
		return __ec_atom_pageDown;
	}
	else if (n==11) {
		return __ec_atom_f1;
	}
	else if (n==12) {
		return __ec_atom_f2;
	}
	else if (n==13) {
		return __ec_atom_f3;
	}
	else if (n==14) {
		return __ec_atom_f4;
	}
	else if (n==15) {
		return __ec_atom_f5;
	}
	else if (n==17) {
		return __ec_atom_f6;
	}
	else if (n==18) {
		return __ec_atom_f7;
	}
	else if (n==19) {
		return __ec_atom_f8;
	}
	else if (n==20) {
		return __ec_atom_f9;
	}
	else if (n==21) {
		return __ec_atom_f10;
	}
	else if (n==23) {
		return __ec_atom_f11;
	}
	else if (n==24) {
		return __ec_atom_f12;
	}
	else {
		return NULL;
	}
}

char* term__letterKey_0(char c) {
	if (c=='A') {
		return __ec_atom_up;
	}
	else if (c=='B') {
		return __ec_atom_down;
	}
	else if (c=='C') {
		return __ec_atom_right;
	}
	else if (c=='D') {
		return __ec_atom_left;
	}
	else if (c=='H') {
		return __ec_atom_home;
	}
	else if (c=='F') {
		return __ec_atom_end;
	}
	else if (c=='P') {
		return __ec_atom_f1;
	}
	else if (c=='Q') {
		return __ec_atom_f2;
	}
	else if (c=='R') {
		return __ec_atom_f3;
	}
	else if (c=='S') {
		return __ec_atom_f4;
	}
	else {
		return NULL;
	}
}

term__Decoded term__mouseEvent_0(int code, int col, int row, bool released, size_t used) {
	int low = (code & 3);
	char* button = (low==0) ? (__ec_atom_left) : ((low==1) ? (__ec_atom_middle) : ((low==2) ? (__ec_atom_right) : (__ec_atom_none)));
	bool shift = (code & 4)!=0;
	bool alt = (code & 8)!=0;
	bool ctrl = (code & 16)!=0;
	char* action = __ec_atom_press;
	if ((code & 64)!=0) {
		action = (low==0) ? (__ec_atom_scrollUp) : (__ec_atom_scrollDown);
	}
 else if (released) {
		action = __ec_atom_release;
	}
 else if ((code & 32)!=0) {
		action = __ec_atom_drag;
	}
	char* b = ((code & 64)!=0) ? (__ec_atom_none) : (button);
	return ((term__Decoded){.event = ((__ec_union_t234){.tag = 2, .v.m2 = ((term__Mouse){.action = action, .button = b, .col = col-1, .row = row-1, .ctrl = ctrl, .alt = alt, .shift = shift})}), .used = used});
}

term__Decoded term__decodeCsi_0(__ec_string_t s, bool final) {
	size_t i = 2;
	bool isMouse = i<s.len&&(*__ec_at_String(s, i, "core:term.ec:724"))=='<';
	if (isMouse) {
		i += 1;
	}
	int first = 0;
	int second = 0;
	int third = 0;
	int __ec_v_index = 0;
	while (i<s.len&&((*__ec_at_String(s, i, "core:term.ec:731"))<'@'||(*__ec_at_String(s, i, "core:term.ec:731"))>'~')) {
		char c = (*__ec_at_String(s, i, "core:term.ec:732"));
		if (c==';') {
			__ec_v_index += 1;
		}
 else if (isDigit_0(c)) {
			int digit = ((int)((c-'0')));
			if (__ec_v_index==0) {
				first = first*10+digit;
			}
 else if (__ec_v_index==1) {
				second = second*10+digit;
			}
 else if (__ec_v_index==2) {
				third = third*10+digit;
			}
		}
		i += 1;
	}
	if (i>=s.len) {
		return ((term__Decoded){.event = ((__ec_union_t234){.tag = 0}), .used = (final) ? (s.len) : (0)});
	}
	size_t used = i+1;
	char last = (*__ec_at_String(s, i, "core:term.ec:748"));
	if (isMouse) {
		return term__mouseEvent_0(first, second, third, last=='m', used);
	}
	if (last=='~') {
		char* __ec_tmp204 = term__tildeKey_0(first);
		if (!((__ec_tmp204) == NULL)) {
			char* name = __ec_tmp204;
			return term__withModifiers_0(name, second, used);
		}
		return ((term__Decoded){.event = ((__ec_union_t234){.tag = 0}), .used = used});
	}
	if (last=='Z') {
		return ((term__Decoded){.event = ((__ec_union_t234){.tag = 1, .v.m1 = ((term__Key){.name = __ec_atom_tab, .shift = true, .ch = 0, .ctrl = false, .alt = false})}), .used = used});
	}
	char* __ec_tmp205 = term__letterKey_0(last);
	if (!((__ec_tmp205) == NULL)) {
		char* name = __ec_tmp205;
		return term__withModifiers_0(name, second, used);
	}
	return ((term__Decoded){.event = ((__ec_union_t234){.tag = 0}), .used = used});
}

int term__waitForInput_0(long micros) {
	struct apr_pollfd_t fd = {0};
	fd.desc_type = APR_POLL_FILE;
	fd.reqevents = ((short)(APR_POLLIN));
	fd.desc.f = term__input;
	int ready = 0;
	return apr_poll(&(fd), 1, &(ready), micros);
}

void term__fillPending_0(void) {
	size_t got = term__pending.len-term__pendingLen;
	int status = apr_file_read(term__input, &((*__ec_at_AChar(term__pending, term__pendingLen, "core:term.ec:808"))), &(got));
	if (APR_STATUS_IS_EINTR(status)==0) {
		if (status!=APR_SUCCESS) {
			term__inputClosed = true;
		}
		else {
			term__pendingLen += got;
		}
	}
}

void term__consume_0(size_t n) {
	for (size_t i=n;i<term__pendingLen;i++) {
		(*__ec_at_AChar(term__pending, i-n, "core:term.ec:818")) = (*__ec_at_AChar(term__pending, i, "core:term.ec:818"));
	}
	term__pendingLen -= n;
}

__ec_union_t234 term__nextEvent_0(long timeout) {
	fflush(stdout);
	long start = apr_time_now();
	bool flushPartial = false;
	while (true) {
		if (term__resized) {
			term__resized = false;
			return ((__ec_union_t234){.tag = 3, .v.m3 = ((term__Resize){.size = term__size_0()})});
		}
		term__Decoded d = term__decode_0(__ec_view_AChar_to_String(__ec_slice_AChar(term__pending, 0, term__pendingLen, "core:term.ec:830")), term__inputClosed||flushPartial);
		flushPartial = false;
		if (d.used>0) {
			term__consume_0(d.used);
			__ec_union_t234 __ec_tmp206 = d.event;
			if (!((__ec_tmp206).tag == 0)) {
				__ec_union_t216 e = __ec_cv_t234__t216(__ec_tmp206, "core:term.ec:834");
				return __ec_cv_t216__t234(e, "core:term.ec:835");
			}
			continue;
		}
		if (term__inputClosed) {
			return ((__ec_union_t234){.tag = 4, .v.m4 = ((term__Eof){.ended = true})});
		}
		long __ec_v_wait = timeout;
		if (timeout>=0) {
			__ec_v_wait = timeout-(apr_time_now()-start);
			if (__ec_v_wait<0) {
				__ec_v_wait = 0;
			}
		}
		if (term__pendingLen>0&&(__ec_v_wait<0||__ec_v_wait>term__ESCAPE_WAIT)) {
			__ec_v_wait = term__ESCAPE_WAIT;
		}
		int status = term__waitForInput_0(__ec_v_wait);
		if (status==APR_SUCCESS) {
			term__fillPending_0();
		}
 else if (APR_STATUS_IS_TIMEUP(status)!=0) {
			if (term__pendingLen>0) {
				flushPartial = true;
			}
			else {
				return ((__ec_union_t234){.tag = 0});
			}
		}
 else if (APR_STATUS_IS_EINTR(status)==0) {
			term__inputClosed = true;
		}
	}
}

__ec_array_Aterm__Cell term__blankCells_0(size_t count) {
	__ec_array_Aterm__Cell cells = __ec_alloc_Aterm__Cell(__ec_pool, count);
	for (size_t i=0;i<count;i++) {
		(*__ec_at_Aterm__Cell(cells, i, "core:term.ec:917")) = ((term__Cell){.ch = 32, .style = term__PLAIN});
	}
	return cells;
}

size_t term__indexOf_0(term__Screen* s, int col, int row) {
	return ((size_t)((row*s->cols+col)));
}

bool term__inside_0(term__Screen* s, int col, int row) {
	return col>=0&&row>=0&&col<s->cols&&row<s->rows;
}

void term__breakWide_0(term__Screen* s, int col, int row) {
	size_t i = term__indexOf_0(s, col, row);
	if ((*__ec_at_Aterm__Cell(s->cells, i, "core:term.ec:958")).ch==0&&col>0) {
		(*__ec_at_Aterm__Cell(s->cells, i-1, "core:term.ec:959")) = ((term__Cell){.ch = 32, .style = (*__ec_at_Aterm__Cell(s->cells, i-1, "core:term.ec:959")).style});
	}
	if (col+1<s->cols&&(*__ec_at_Aterm__Cell(s->cells, i+1, "core:term.ec:960")).ch==0) {
		(*__ec_at_Aterm__Cell(s->cells, i+1, "core:term.ec:961")) = ((term__Cell){.ch = 32, .style = (*__ec_at_Aterm__Cell(s->cells, i+1, "core:term.ec:961")).style});
	}
}

__ec_string_t term__boxChars_0(char* kind) {
	if (kind==__ec_atom_single) {
		return (__ec_string_t){18,"─│┌┐└┘"};
	}
	else if (kind==__ec_atom_double) {
		return (__ec_string_t){18,"═║╔╗╚╝"};
	}
	else if (kind==__ec_atom_rounded) {
		return (__ec_string_t){18,"─│╭╮╰╯"};
	}
	else {
		return (__ec_string_t){18,"━┃┏┓┗┛"};
	}
}

unsigned int term__boxChar_0(__ec_string_t chars, size_t n) {
	__ec_tuple_UInt_Size __ec_tmp207 = unicode__decode_0(chars, n*3);
	unsigned int cp = __ec_tmp207.first;
	size_t used = __ec_tmp207.second;
	return cp;
}

void draw_0(term__Screen* s, __ec_array_ATask tasks, size_t current) {
	term__clear_0(s);
	term__box_0(s, 0, 0, s->cols, s->rows, __ec_atom_rounded, (__ec_string_t){5,"Tasks"}, ((term__Style){.fg = term__CYAN, .bg = ((term__Color){.code = 0}), .bold = false, .dim = false, .italic = false, .underline = false, .reverse = false, .strike = false}));
	for (size_t i=0;i<tasks.len;i++) {
		__ec_string_t mark = ((*__ec_at_ATask(tasks, i, "tui.ec:14")).done) ? ((__ec_string_t){4,"[x] "}) : ((__ec_string_t){4,"[ ] "});
		term__Style look = (i==current) ? (((term__Style){.reverse = true, .fg = ((term__Color){.code = 0}), .bg = ((term__Color){.code = 0}), .bold = false, .dim = false, .italic = false, .underline = false, .strike = false})) : (term__PLAIN);
		int row = (((int)(i)))+2;
		int after = term__text_0(s, 3, row, mark, look);
		term__text_0(s, after, row, (*__ec_at_ATask(tasks, i, "tui.ec:18")).name, ((*__ec_at_ATask(tasks, i, "tui.ec:18")).done) ? (((term__Style){.dim = true, .strike = true, .fg = ((term__Color){.code = 0}), .bg = ((term__Color){.code = 0}), .bold = false, .italic = false, .underline = false, .reverse = false})) : (look));
	}
	term__text_0(s, 3, s->rows-2, (__ec_string_t){29,"up/down  space: done  q: quit"}, ((term__Style){.fg = term__GRAY, .bg = ((term__Color){.code = 0}), .bold = false, .dim = false, .italic = false, .underline = false, .reverse = false, .strike = false}));
	term__render_0(s);
}

__ec_result_Int_Error main_0(void) {
	__ec_array_ATask tasks = (__ec_array_ATask){3,(Task[]){((Task){.name = (__ec_string_t){15,"Read the README"}, .done = false}), ((Task){.name = (__ec_string_t){15,"Write a program"}, .done = false}), ((Task){.name = (__ec_string_t){7,"Ship it"}, .done = false})}};
	size_t current = 0;
	__ec_result_None_IoError __ec_tmp210 = term__enterFullscreen_1();
	if (__ec_tmp210.code != 0) {
		return ((__ec_result_Int_Error){.code = 1, .error = __ec_up_IoError__Error(__ec_tmp210.error)});
	}
	term__enableMouse_0();
	term__Screen* s = term__screen_1();
	while (true) {
		draw_0(s, tasks, current);
		__ec_union_t216 e = term__readEvent_0();
		if (((e).tag == 0)) {
			if (term__isChar_0((e).v.m0, 'q')||(e).v.m0.name==__ec_atom_escape||((e).v.m0.name==__ec_atom_char&&term__isCtrl_0((e).v.m0, 'c'))) {
				break;
			}
			if ((e).v.m0.name==__ec_atom_up&&current>0) {
				current -= 1;
			}
 else if ((e).v.m0.name==__ec_atom_down&&current+1<tasks.len) {
				current += 1;
			}
 else if (term__isChar_0((e).v.m0, ' ')) {
				Task __ec_tmp211;
				(*__ec_at_ATask(tasks, current, "tui.ec:40")) = ((__ec_tmp211 = (*__ec_at_ATask(tasks, current, "tui.ec:40"))), ((Task){.done = !(*__ec_at_ATask(tasks, current, "tui.ec:40")).done, .name = __ec_tmp211.name}));
			}
		}
		else if (((__ec_cv_t216__term__Mouse_term__Resize_term__Eof(e, "tui.ec:32")).tag == 0)) {
			int i = (e).v.m1.row-2;
			if ((e).v.m1.action==__ec_atom_press&&i>=0&&(((size_t)(i)))<tasks.len) {
				current = ((size_t)(i));
			}
		}
		else if (((__ec_cv_t216__term__Resize_term__Eof(e, "tui.ec:32")).tag == 0)) {
			term__resize_0(s, (e).v.m2.size);
		}
		else {
			break;
		}
	}
	__ec_result_Int_Error __ec_tmp212 = ((__ec_result_Int_Error){.code = 0, .value = 0});
	term__leaveFullscreen_0();
	return __ec_tmp212;
}

// Main Entry
int main(int argc, char** argv) {
	__ec_watch_stack();
	__ec_argc = argc;
	__ec_argv = argv;
	apr_initialize();
	__ec_pool = __ec_pool_create(NULL);
	term__input = term__openStdin_0();
	term__output = term__openStdout_0();
	term__original = term__noSettings_0();
	term__HANDLED = (__ec_array_AInt){9,(int[]){SIGINT, SIGTERM, SIGHUP, SIGQUIT, SIGABRT, SIGSEGV, SIGBUS, SIGFPE, SIGILL}};
	term__pending = __ec_alloc_AChar(__ec_pool, 1024);
	int __ec_ret;
	__ec_result_Int_Error __ec_result = main_0();
	if (__ec_result.code != 0) { fprintf(stderr, "error: main failed: %.*s\n", (int)__ec_result.error.message.len, __ec_result.error.message.data); __ec_ret = 1; }
	else __ec_ret = __ec_result.value;
	apr_pool_destroy(__ec_pool);
	return __ec_ret;
}
