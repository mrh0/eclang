// sys/utsname.h: the name and version of the system.
use "sys/utsname.h"

// The fields are char arrays in C: read them with `!as CString`.
declare struct utsname as
    sysname: Char[]c
    nodename: Char[]c
    release: Char[]c
    version: Char[]c
    machine: Char[]c

// 0, or -1 when it fails.
declare fn uname as (out buf: utsname): Int
