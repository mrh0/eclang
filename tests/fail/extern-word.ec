// error: To give the name C uses, write `extern "abs"`
declare fn cAbs intern "abs" as (x: Int): Int
fn main() do
    cAbs(1)
