// panic: c-null-field-write-through.ec:18: the field LinkedTm.tm_zone of a C struct is NULL, but its `declare rec` says it never is
// `p.next.x = 1` reads `p.next`, so a NULL pointer field of a C struct is caught there too. (`tm_zone` stands in for
// a pointer to another struct: any pointer field is checked the same way.)
use "core:io.ec"
use "time.h"

declare struct LinkedTm extern "tm" as
    tm_sec: Int
    tm_zone: @LinkedTm

declare fn zeroed extern "calloc" as (count: Size, size: Size): @Any

fn main(): Int do
    val t = zeroed(1, sizeof LinkedTm) !as @LinkedTm
    io.log("before"c)
    t.tm_sec = 2
    io.log("${t.tm_sec}")
    t.tm_zone.tm_sec = 1
    ret 0
