// panic: c-null-result.ec:8: the C function getenv returned NULL, but its `declare fn` says it never does
use "core:io.ec"

declare fn getenvRaw extern "getenv" as (name: CString): CString

fn main(): Int do
    io.log("before"c)
    io.log(getenvRaw("ECLANG_TEST_NOT_SET"c))
    ret 0
