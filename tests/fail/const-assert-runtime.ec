// error: `const assert` needs a condition known when compiling
fn main(): Int do
    val n = 3
    const assert n > 2
    ret 0
