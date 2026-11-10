use "core:io.ec"
use "getopt.h"

// glibc's `struct option`, whose name is a `const char*`: read as a nullable CString, it is cast to eclang's type.
declare struct LongOption extern "option" as
    name: CString?
    has_arg: Int

declare fn zeroed extern "calloc" as (count: Size, size: Size): @Any

fn main(): Int do
    val o = zeroed(1, sizeof LongOption) !as @LongOption
    val missing = o.name
    io.log(if missing == null do "no name"c else "a name"c)
    o.name = "verbose"c
    val name = o.name
    if name != null do
        io.log(name)
    ret 0
