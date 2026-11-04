// error: Can't call 'f' because it may be null. Check it first, e.g. `if f != null do`.
fn apply(f: ((Int) => Int)?): Int = f(1)

fn main(): Int do
    ret 0
