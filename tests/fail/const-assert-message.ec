// error: `const assert` is checked when compiling, so it can't contain `${...}`
const N = 3
const assert N > 2, "N is ${N}"
fn main(): Int do
    ret 0
