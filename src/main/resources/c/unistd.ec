// unistd.h: a few POSIX system calls.
use "unistd.h"

declare type pid_t as Int

declare val _SC_NPROCESSORS_ONLN as Int
declare val _SC_PAGESIZE as Int
declare val STDIN_FILENO as Int
declare val STDOUT_FILENO as Int
declare val STDERR_FILENO as Int

// A system setting, or -1 when it isn't known.
declare fn sysconf as (name: Int): Long
declare fn getpid as (): pid_t
declare fn getppid as (): pid_t
// 1 when fd is a terminal.
declare fn isatty as (fd: Int): Int
// Writes straight to fd, without stdio's buffer, so it is safe in a signal handler. The bytes written, or -1.
declare fn write as (fd: Int, data: @Any, count: Size): Long
