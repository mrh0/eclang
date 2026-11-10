// `name extern "c_name": T` in a `declare rec`: a field under another name than C's, for a C name eclang can't use
// (a keyword such as `in`) or doesn't want. Reads, writes, record literals and `offsetof` all use the C name.
use "core:io.ec"
use "stdlib.h"

declare mut rec Division extern "div_t" as
    quotient extern "quot": Int
    rem: Int

declare fn divide extern "div" as (numer: Int, denom: Int): Division

fn main() do
    var d = divide(17, 5)
    io.log("${d.quotient} ${d.rem}")
    d.quotient = 9
    io.log("${d.quotient}")
    val e = Division { quotient = 1, rem = 2 }
    io.log("${e.quotient} ${e.rem} ${offsetof(Division, rem) > offsetof(Division, quotient)}")
    val p = addrof d
    p.quotient += 1
    io.log("${p.quotient}")
