// `use "define:NAME"` (or `NAME=value`) writes a C #define before every #include, for headers that look for one:
// with _GNU_SOURCE, string.h declares strchrnul. A macro with a value can be read with `declare val`.
use "define:_GNU_SOURCE"
use "define:EC_ANSWER=42"
use "define:EC_RATIO=-1.5"
use "core:io.ec"
use "string.h"

declare fn strchrnul as (s: CString, c: Int): CString
declare val EC_ANSWER as Int
declare val EC_RATIO as Double

fn main() do
    io.log(fromC(strchrnul("key=value"c, '=' as Int)))
    io.log(fromC(strchrnul("no sign"c, '=' as Int)).len)
    io.log("${EC_ANSWER} ${EC_RATIO}")
