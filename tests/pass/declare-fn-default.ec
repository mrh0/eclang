// A `declare fn` can have default values. C has none, so a call that leaves one out passes the default itself.
// Defaults are compiled at each call, in the module of the declaration, and can use its names.
use "core:io.ec"
use "stdlib.h"
use "string.h"

const DECIMAL = 10
declare fn strtol as (s: CString, end: @CString? = null, base: Int = DECIMAL): Long
declare fn strncmp as (a: CString, b: CString, n: Size = 3): Int

module "hex"
const BASE = 16
declare fn parse extern "strtol" as (s: CString, end: @CString? = null, base: Int = BASE): Long

fn main() do
    io.log(strtol("42"c))
    io.log(strtol("ff"c, null, 16))
    io.log(strtol("17"c, base = 8))
    io.log(hex.parse("ff"c))
    io.log(strncmp("abcx"c, "abcy"c))
    io.log(strncmp("abcx"c, "abcy"c, 4) < 0)
