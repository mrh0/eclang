// A use of freed memory that the compiler takes `unsafe`'s word for: `--check-memory` must report it, at line 13.
use "core:io.ec"

fn drop(p: Pool) do
    unsafe destroy p

fn main(): Int do
    val p = new Pool
    val s = format(p, "in p ${1}")
    io.log(s)
    drop(p)
    // The caller can still use what was in p (see KNOWN_ISSUES.md): AddressSanitizer reports it.
    io.log(s)
    ret 0
