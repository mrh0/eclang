// unistd.h: a few POSIX system calls.
use "unistd.h"

declare type pid_t as Int

declare val _SC_NPROCESSORS_ONLN as Int
declare val _SC_PAGESIZE as Int
declare val _SC_PHYS_PAGES as Int
declare val _SC_AVPHYS_PAGES as Int
declare val X_OK as Int
declare val STDIN_FILENO as Int
declare val STDOUT_FILENO as Int
declare val STDERR_FILENO as Int

// A system setting, or -1 when it isn't known.
declare fn sysconf as (name: Int): Long
declare fn getpid as (): pid_t
declare fn getppid as (): pid_t
// 1 when fd is a terminal.
declare fn isatty as (fd: Int): Int
// 0 when the file can be used as `mode` (X_OK: run), -1 when not.
declare fn access as (path: CString, mode: Int): Int
// Writes where a symbolic link points to in buf, without a final 0; gives its length, or -1.
declare fn readlink as (path: CString, buf: CString, size: Size): Long
// Writes straight to fd, without stdio's buffer, so it is safe in a signal handler. The bytes written, or -1.
declare fn write as (fd: Int, data: @Any, count: Size): Long
