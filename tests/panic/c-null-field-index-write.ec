// panic: c-null-field-index-write.ec:16: the field ZoneTm.tm_zone of a C struct is NULL, but its `declare rec` says it never is
// Writing an element through a C struct's pointer field reads the pointer, so it is checked like a read.
use "core:io.ec"
use "time.h"

// glibc's `struct tm`, with its time zone name, which is a pointer.
declare struct ZoneTm extern "tm" as
    tm_sec: Int
    tm_zone: Char[]c

declare fn zeroed extern "calloc" as (count: Size, size: Size): @Any

fn main(): Int do
    val t = zeroed(1, sizeof ZoneTm) !as @ZoneTm
    io.log("before"c)
    unsafe t.tm_zone[0] = 'a'
    ret 0
