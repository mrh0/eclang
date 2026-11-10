// error: div_t is a C type declared without `mut`, so eclang code can't change its fields. Write `declare mut rec div_t`
use "core:c/stdlib.ec"

fn main() do
    var d = div(17, 5)
    d.rem = 0
