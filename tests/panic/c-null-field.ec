// panic: c-null-field.ec:15: the field ZoneTm.tm_zone of a C struct is NULL, but its `declare rec` says it never is
use "core:io.ec"
use "time.h"

// glibc's `struct tm`, with its time zone name, which is a pointer.
declare struct ZoneTm extern "tm" as
    tm_sec: Int
    tm_zone: CString

declare fn zeroed extern "calloc" as (count: Size, size: Size): @Any

fn main(): Int do
    val t = zeroed(1, sizeof ZoneTm) !as @ZoneTm
    io.log("before"c)
    io.log(t.tm_zone)
    ret 0
